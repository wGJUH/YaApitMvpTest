package test.ya.translater.wgjuh.yaapitmvptest.presenter;


import android.os.Bundle;

import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.IEventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateListView;


/**
 * Created by wGJUH on 07.04.2017.
 */

public class TranslatePresenter extends BasePresenter<TranslateListView> {

    private final IModel iModel;
    private final IEventBus eventBus;
    private DictDTO lastTranslate;

    public TranslatePresenter(IModel iModel, IEventBus eventBus) {

        this.iModel = iModel;
        this.eventBus = eventBus;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        addSubscription(eventBus.getEventBus().subscribe(event -> {
            switch (event.eventType) {
                case BTN_CLEAR_CLICKED:
                    clearTranslate();
                    setFavorite(false);
                    lastTranslate = null;
                    break;
                case WORD_TRANSLATED:
                    // TODO: 09.04.2017  как тут абстрагироваться от конкретной реализации ?
                    restoreState((DictDTO) event.content[0]);
                    break;
                case WORD_UPDATED:
                    restoreState((DictDTO) event.content[0]);
                    break;
                case ADD_FAVORITE:

                    break;
                case UPDATE_FAVORITE:
                    if(lastTranslate != null && lastTranslate.equals(event.content[0])){
                        setFavorite(!((DictDTO) event.content[0]).getFavorite().equals("-1"));
                    }
                    break;
                default:
                    break;
            }
        }));
    }

    private void setFavorite(boolean isFavorite) {
        view.setBtnFavoriteSelected(isFavorite);
    }

    /**
     * Метод вызывает у View метод вывода переведенного текста на экран
     *
     * @param s - строка которую необходимо вывести
     */
    private void updateTranslateView(String s) {
        view.showTranslate(s);
    }

    private void updateChecboxFavorite(boolean isFavorite){
        view.setBtnFavoriteSelected(isFavorite);
    }

    /**
     * Метод для очистки поля переведенного текста
     */
    private void clearTranslate() {
        view.showTranslate("");
    }

    public void addToFavorites() {
        if (lastTranslate != null) {
            lastTranslate.setFavorite(Long.toString(iModel.setFavorites(lastTranslate)));
            eventBus
                    .getEventBus()
                    .onNext(eventBus.createEvent(Event.EventType.UPDATE_FAVORITE, lastTranslate));
        }
    }

    public void saveOutState(Bundle outState) {
        if (lastTranslate != null) {
            outState.putSerializable(DATA.OUT_STATE, lastTranslate);
        }
    }

    private void setLastTranslate(DictDTO lastTranslate) {
        this.lastTranslate = lastTranslate;
    }

    public void restoreState(DictDTO dictDTO) {
        setLastTranslate(dictDTO);
        updateChecboxFavorite(!lastTranslate.getFavorite().equals("-1"));
        updateTranslateView(dictDTO.getCommonTranslate());
    }
}

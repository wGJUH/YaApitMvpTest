package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;


import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.IEventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefRecyclerItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefTranslateItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.Translate;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.inter.ITranslatePrsenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.inter.TranslateView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;


/**
 * Created by wGJUH on 07.04.2017.
 */

public class TranslatePresenterImpl extends BasePresenter<TranslateView> implements ITranslatePrsenter {

    private final IModel iModel;
    private final IEventBus eventBus;
    private DictDTO lastTranslate;
    private List<DefRecyclerItem> defRecyclerItems = new ArrayList<>();
    private Subscription subscription;

    public TranslatePresenterImpl(IModel iModel, IEventBus eventBus) {

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
                case START_TRANSLATE:
                    startTranslate((String) event.content[0]);
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
                    if (lastTranslate != null && lastTranslate.equals(event.content[0])) {
                        setFavorite(!((DictDTO) event.content[0]).getFavorite().equals("-1"));
                    }
                    break;
                default:
                    break;
            }
        }));
    }

    void startTranslate(String targetText){
        view.showProgressBar(true);
        if(subscription!= null && !subscription.isUnsubscribed()){
            subscription.isUnsubscribed();
        }

        String translateDirection =iModel.getTranslateLangPair();

        DictDTO historyTranslate =iModel.getHistoryTranslate(targetText, translateDirection);

        if(historyTranslate != null){
           iModel.updateHistoryDate(historyTranslate.getId());
            eventBus.getEventBusForPost().onNext(eventBus.createEvent(Event.EventType.WORD_UPDATED,historyTranslate));
            return;
        }
        Observable<DictDTO> dictDTOObservable =iModel
                .getDicTionaryTranslateForLanguage(targetText, translateDirection)
                .onErrorReturn(throwable -> {
                    Log.e(TAG, "dictDTOObservable: Сервис недоступен или запрос неверен",throwable);
                    return new DictDTO();});

        Observable<TranslateDTO> translatePojoObservable =iModel
                .getTranslateForLanguage(targetText, translateDirection)
                .doOnError(throwable ->
                        Log.e(TAG, "translatePojoObservable: Сервис недоступен или запрос неверен",throwable )
                );

        // TODO: 08.04.2017 спросить про проверку при зиппе
        Observable zipObservable = Observable.zip(dictDTOObservable, translatePojoObservable, (dictDTO, translatePojo) -> {

            dictDTO.setCommonTranslate(translatePojo.getText());
            dictDTO.setTarget(targetText);
            dictDTO.setLangs(translatePojo.getLang());

            return dictDTO;
        });
        subscription = zipObservable
                .subscribe(new Observer<DictDTO>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.showError(e.getMessage());
                    }
                    @Override
                    public void onNext(DictDTO dictDTO) {
                       iModel.saveToDBAndNotify(dictDTO);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void setFavorite(boolean isFavorite) {
        view.setBtnFavoriteSelected(isFavorite);
    }

    @Override
    public void updateTranslateView(String s) {
        view.showTranslate(s);
    }

    @Override
    public void updateRecylcerView(DictDTO dictDTO) {
        dictDTO.getDef().subscribe(def -> {
            DefRecyclerItem defRecyclerItem = new DefRecyclerItem();
            defRecyclerItem.setText(def.getText());
            defRecyclerItem.setTs("["+def.getTranscription()+"]");
            defRecyclerItem.setPos(def.getPos());
            if (def.getTranslate() != 0){
                def.getTranslateObservable().subscribe(translate -> {
                    DefTranslateItem defTranslateItem = new DefTranslateItem();
                    SetExamples(translate, defTranslateItem);
                    SetMeans(translate, defTranslateItem);
                    defTranslateItem.getTextAndSyn().add(translate.getText());
                    SetSyns(translate, defTranslateItem);
                    defRecyclerItem.getDefTranslateItems().add(defTranslateItem);
                });
            }
            insertItemInTaleOfAdapterListAndNotify(defRecyclerItem);
        });
        view.showProgressBar(false);
    }

    private void insertItemInTaleOfAdapterListAndNotify(DefRecyclerItem defRecyclerItem){
        defRecyclerItems.add(defRecyclerItem);
        view.getViewAdapter().notifyItemInserted(defRecyclerItems.size());
        view.getViewAdapter().notifyDataSetChanged();
        view.getRecyclerView().invalidate();
    }

    private void SetSyns(Translate translate, DefTranslateItem defTranslateItem) {
        if(translate.getSynonyme() != null && translate.getSynonyme().size() != 0) {
            translate.getSynonymeObservable().subscribe(syn -> {
                defTranslateItem.getTextAndSyn().add(syn.getText());
            });
        }
    }

    private void SetMeans(Translate translate, DefTranslateItem defTranslateItem) {
        if(translate.getMean() != null && translate.getMean().size() != 0) {
            translate.getMeanObservable().subscribe(mean -> {
                defTranslateItem.getMeans().add(mean.getText());
            });
        }
    }

    private void SetExamples(Translate translate, DefTranslateItem defTranslateItem) {
        if(translate.getExample() != null && translate.getExample().size() != 0) {
            translate.getExampleObservable().subscribe(example -> {
                defTranslateItem.getExamples().add(example.toString());
            });
        }
    }

    @Override
    public void updateChecboxFavorite(boolean isFavorite) {
        view.setBtnFavoriteSelected(isFavorite);
    }

    @Override
    public void clearTranslate() {
        view.showTranslate("");

        // TODO: 16.04.2017 плохой план ?
        view.getViewAdapter().notifyItemRangeRemoved(0,defRecyclerItems.size());
        defRecyclerItems.clear();
        view.getRecyclerView().removeAllViews();
        view.getViewAdapter().notifyDataSetChanged();
    }

    @Override
    public void addToFavorites() {
        if (lastTranslate != null) {
            lastTranslate.setFavorite(Long.toString(iModel.setFavorites(lastTranslate)));
            eventBus
                    .getEventBusForPost()
                    .onNext(eventBus.createEvent(Event.EventType.UPDATE_FAVORITE, lastTranslate));
        }
    }

    @Override
    public void saveOutState(Bundle outState) {
        if (lastTranslate != null) {
            outState.putSerializable(DATA.OUT_STATE, lastTranslate);
        }
    }

    @Override
    public void setLastTranslate(DictDTO lastTranslate) {
        this.lastTranslate = lastTranslate;
    }

    @Override
    public void restoreState(DictDTO dictDTO) {
        setLastTranslate(dictDTO);
        updateChecboxFavorite(!lastTranslate.getFavorite().equals("-1"));
        updateTranslateView(dictDTO.getCommonTranslate());
        updateRecylcerView(dictDTO);
    }

    @Override
    public List<DefRecyclerItem> getDictionarySate() {
        return defRecyclerItems;
    }
}

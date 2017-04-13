package test.ya.translater.wgjuh.yaapitmvptest.presenter;



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
            switch (event.eventType){
                case BTN_CLEAR_CLICKED:
                    clearTranslate();
                    break;
                case WORD_TRANSLATED:
                    // TODO: 09.04.2017  как тут абстрагироваться от конкретной реализации ?
                    lastTranslate = (DictDTO)event.content[0];
                    updateTranslateView(lastTranslate.getCommonTranslate());
                case WORD_UPDATED:
                    lastTranslate = (DictDTO)event.content[0];
                    updateTranslateView(lastTranslate.getCommonTranslate());
                default:
                    break;
            }
        }));
    }

    /**
     * Метод вызывает у View метод вывода переведенного текста на экран
     * @param s - строка которую необходимо вывести
     */
    private void updateTranslateView(String s) {
        view.showTranslate(s);
    }

    /**
     * Метод для очистки поля переведенного текста
     */
    private  void clearTranslate(){
        view.showTranslate("");
    }

    public void addToFavorites(){
        if(lastTranslate != null) {
            view.setBtnFavoriteSelected(true);
            iModel.addToFavorites(lastTranslate);
        }
    }
}

package test.ya.translater.wgjuh.yaapitmvptest.presenter;


import android.util.Log;

import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateListView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;


/**
 * Created by wGJUH on 07.04.2017.
 */

public class TranslatePresenter extends BasePresenter {

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        addSubscription(ModelImpl.getEventBus().subscribe(event -> {
            switch (event.getEventType()){
                case BTN_CLEAR_CLICKED:
                    clearTranslate();
                    break;
                case WORD_TRANSLATED:
                    updateTranslateView(((DictDTO)event.getEventObject()[0]).getCommonTranslate());
                   // Log.d(TAG, "WORD_TRANSLATED: " + ((DictDTO)event.getEventObject()[0]).toString());

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
        ((TranslateListView) view).showTranslate(s);
    }


    /**
     * Метода для очистки поля переведенного текста
     */
    private  void clearTranslate(){
        ((TranslateListView) view).showTranslate("");
    }
}

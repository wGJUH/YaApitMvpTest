package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import rx.functions.Action1;
import rx.subjects.PublishSubject;
import test.ya.translater.wgjuh.yaapitmvptest.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.Model;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.data.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateListView;

/**
 * Created by wGJUH on 07.04.2017.
 */

public class TranslatePresenter extends BasePresenter {

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        addSubscription(ModelImpl.getTranslatePojoPublishSubject().subscribe(t->updateTranslateView(t.getText().toString())));
        addSubscription(ModelImpl.getEventBus().subscribe(new Action1<Event>() {
            @Override
            public void call(Event event) {
                clearTranslate();
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

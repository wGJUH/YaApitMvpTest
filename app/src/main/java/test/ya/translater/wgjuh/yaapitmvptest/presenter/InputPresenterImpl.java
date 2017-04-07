package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import android.util.Log;

import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.Model;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.data.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 07.04.2017.
 */

public class InputPresenterImpl extends BasePresenter {
    PublishSubject<Event> eventPublishSubject;

    public boolean onButtonTranslateClick() {
        eventPublishSubject.onNext(Event.BTN_CLEAR_CLICKED);

        // TODO: 06.04.2017 попробовать найти способ уйти от такого способа удержания подписанных fragments
        Subscription subscription = model
                .getTranslateForLanguage(((InputTranslateView) view).getTargetText(), "en-ru")
                .subscribe(new Observer<TranslatePojo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TranslatePojo translatePojo) {
                        Log.d(DATA.TAG, translatePojo.getText().toString());
                        ModelImpl.getTranslatePojoPublishSubject().onNext(translatePojo);
                    }
                });

        addSubscription(subscription);
        return true;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        eventPublishSubject = ModelImpl.getEventBus();
    }

    /**
     * Метод для очистки поля ввода переводимого текста
     */
    public void clearInput(){
        ((InputTranslateFragment) view).clearText();
        eventPublishSubject.onNext(Event.BTN_CLEAR_CLICKED);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + this.getClass().getName());
    }
}

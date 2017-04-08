package test.ya.translater.wgjuh.yaapitmvptest.presenter;


import android.util.EventLog;
import android.util.Log;

import java.net.HttpRetryException;

import retrofit2.HttpException;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.PublishSubject;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.Model;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.data.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 07.04.2017.
 */

public class InputPresenterImpl extends BasePresenter {
    private Throwable throwable;
    public boolean onButtonTranslateClick() {
        ModelImpl.getEventBus().onNext(new Event<>(Event.EventType.BTN_CLEAR_CLICKED, null));

        // TODO: 06.04.2017 пока что оставил оба способа получения перевода
        Observable<DictDTO> dictDTOObservable = model
                .getDicTionaryTranslateForLanguage(((InputTranslateView) view).getTargetText(), "en-ru");

        Observable<TranslatePojo> translatePojoObservable = model
                .getTranslateForLanguage(((InputTranslateView) view).getTargetText(), "en-ru");

        // TODO: 08.04.2017 спросить про проверку при зиппе
        Observable zipObservable = Observable.zip(dictDTOObservable,translatePojoObservable,(dictDTO, translatePojo) -> {
                dictDTO.setCommonTranslate(translatePojo.getText());
            return dictDTO;
        });
        Subscription subscription = zipObservable
                .subscribe(new Observer<DictDTO>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        DictDTO dictDTO   = new DictDTO();
                        dictDTO.setCommonTranslate("BAD");
                        ModelImpl.getEventBus().onNext(new Event<>(Event.EventType.WORD_TRANSLATED, dictDTO));
                    }

                    @Override
                    public void onNext(DictDTO dictDTO) {
                            Log.d(DATA.TAG, dictDTO.getCommonTranslate());
                            ModelImpl.getEventBus().onNext(new Event<>(Event.EventType.WORD_TRANSLATED, dictDTO));
                    }
                });

        addSubscription(subscription);
        return true;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
    }

    /**
     * Метод для очистки поля ввода переводимого текста
     */
    public void clearInput() {
        ((InputTranslateFragment) view).clearText();
        ModelImpl.getEventBus().onNext(new Event(Event.EventType.BTN_CLEAR_CLICKED, null));
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + this.getClass().getName());
    }
}

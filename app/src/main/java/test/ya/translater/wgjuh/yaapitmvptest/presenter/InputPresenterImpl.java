package test.ya.translater.wgjuh.yaapitmvptest.presenter;


import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslatePojo;
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
    private String lang = "en-en";

    public boolean onButtonTranslateClick() {
        ModelImpl.getEventBus().onNext(new Event<>(Event.EventType.BTN_CLEAR_CLICKED, null));
        startTranslate();
        return true;
    }

    private void startTranslate() {

        // TODO: 06.04.2017 пока что оставил оба способа получения перевода
        Observable<DictDTO> dictDTOObservable = model
                .getDicTionaryTranslateForLanguage(((InputTranslateView) view).getTargetText(), lang);

        Observable<TranslatePojo> translatePojoObservable = model
                .getTranslateForLanguage(((InputTranslateView) view).getTargetText(), lang);

        // TODO: 08.04.2017 спросить про проверку при зиппе
        /**
         * Получаем объект сохраняем его ввиде строки в бд
         */
        Observable zipObservable = Observable.zip(dictDTOObservable, translatePojoObservable, (dictDTO, translatePojo) -> {
            dictDTO.setCommonTranslate(translatePojo.getText());
            dictDTO.setLangs(translatePojo.getLang());
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
                        DictDTO dictDTO = new DictDTO();
                        dictDTO.setCommonTranslate("BAD");
                        ModelImpl.getEventBus().onNext(new Event<>(Event.EventType.WORD_TRANSLATED, dictDTO));
                    }

                    @Override
                    public void onNext(DictDTO dictDTO) {
                        ((ModelImpl) model).saveToDB(dictDTO, ((Fragment)view).getActivity().getBaseContext());
                        ModelImpl.getEventBus().onNext(new Event<>(Event.EventType.WORD_TRANSLATED, dictDTO));


                    }
                });

        addSubscription(subscription);
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        addSubscription(ModelImpl.getEventBus().subscribe(event -> {
            switch (event.getEventType()) {
                case ON_LANGUAGE_CHNAGED:
                    lang = ((String) event.getEventObject()[0]);
                    startTranslate();
                    break;
                default:
                    break;
            }
        }));
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

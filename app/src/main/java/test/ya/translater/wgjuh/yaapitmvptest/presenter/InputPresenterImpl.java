package test.ya.translater.wgjuh.yaapitmvptest.presenter;


import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.IEventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 07.04.2017.
 */

public class InputPresenterImpl extends BasePresenter<InputTranslateView> {
    private final IModel model;
    private final IEventBus eventBus;

    public InputPresenterImpl(IModel model,
                              IEventBus eventBus) {
        this.model = model;
        this.eventBus = eventBus;
    }

    public boolean onButtonTranslateClick() {
        eventBus.getEventBus().onNext(new Event<>(Event.EventType.BTN_CLEAR_CLICKED, null));
        startTranslate();
        return true;
    }

    private void startTranslate() {


        DictDTO historyTranslate = model.getHistoryTranslate(view.getTargetText(), model.getTranslateLang());
        if(historyTranslate != null){
            eventBus.getEventBus().onNext(new Event<>(Event.EventType.WORD_TRANSLATED,historyTranslate));
            return;
        }
        Observable<DictDTO> dictDTOObservable = model
                .getDicTionaryTranslateForLanguage(view.getTargetText(), model.getTranslateLang())
                .onErrorReturn(throwable -> {throwable.printStackTrace();
                return new DictDTO();});

        Observable<TranslatePojo> translatePojoObservable = model
                .getTranslateForLanguage(view.getTargetText(), model.getTranslateLang());

        // TODO: 08.04.2017 спросить про проверку при зиппе
        Observable zipObservable = Observable.zip(dictDTOObservable, translatePojoObservable, (dictDTO, translatePojo) -> {
            dictDTO.setCommonTranslate(translatePojo.getText());
            dictDTO.setTarget(view.getTargetText());
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
                        eventBus.getEventBus().onNext(eventBus.createEvent(Event.EventType.WORD_TRANSLATED, dictDTO));
                    }
                    @Override
                    public void onNext(DictDTO dictDTO) {
                        model.saveToDBAndNotify(dictDTO);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        addSubscription(eventBus.getEventBus().subscribe(event -> {
            switch (event.eventType) {
                case ON_LANGUAGE_CHANGED:
                    model.setTranslateLang((String)event.content[0]);
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
        view.clearText();
        eventBus.getEventBus().onNext(eventBus.createEvent(Event.EventType.BTN_CLEAR_CLICKED,null));
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + this.getClass().getName());
    }
}

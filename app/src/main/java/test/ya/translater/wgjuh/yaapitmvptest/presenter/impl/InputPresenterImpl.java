package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;


import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.IEventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.inter.IInputPresenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.inter.InputView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 07.04.2017.
 */

public class InputPresenterImpl extends BasePresenter<InputView> implements IInputPresenter {
    private final IModel model;
    private final IEventBus eventBus;
    private  Subscription subscription;

    public InputPresenterImpl(IModel model,
                              IEventBus eventBus) {
        this.model = model;
        this.eventBus = eventBus;
    }

    @Override
    public boolean onButtonTranslateClick() {
        if(!view.getTargetText().isEmpty()) {
            eventBus.getEventBusForPost().onNext(new Event<>(Event.EventType.BTN_CLEAR_CLICKED));
            startTranslate();
        }
        return true;
    }

    @Override
    public void startTranslate() {
        if(subscription!= null && !subscription.isUnsubscribed()){
            subscription.isUnsubscribed();
        }

        String translateDirection = model.getTranslateLangPair();

        DictDTO historyTranslate = model.getHistoryTranslate(view.getTargetText().trim(), translateDirection);

        if(historyTranslate != null){
            model.updateHistoryDate(historyTranslate.getId());
            eventBus.getEventBusForPost().onNext(eventBus.createEvent(Event.EventType.WORD_UPDATED,historyTranslate));
            return;
        }
        Observable<DictDTO> dictDTOObservable = model
                .getDicTionaryTranslateForLanguage(view.getTargetText(), translateDirection)
                .onErrorReturn(throwable -> {
                    Log.e(TAG, "dictDTOObservable: Сервис недоступен или запрос неверен",throwable);
                return new DictDTO();});

        Observable<TranslateDTO> translatePojoObservable = model
                .getTranslateForLanguage(view.getTargetText(), translateDirection)
                .doOnError(throwable ->
                    Log.e(TAG, "translatePojoObservable: Сервис недоступен или запрос неверен",throwable )
                );

        // TODO: 08.04.2017 спросить про проверку при зиппе
        Observable zipObservable = Observable.zip(dictDTOObservable, translatePojoObservable, (dictDTO, translatePojo) -> {

            dictDTO.setCommonTranslate(translatePojo.getText());
            dictDTO.setTarget(view.getTargetText());
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
                case CHANGE_LANGUAGES:
                    startTranslate();
                    break;
                default:
                    break;
            }
        }));
    }

    @Override
    public void clearInput() {
        view.clearText();
        eventBus.getEventBusForPost().onNext(eventBus.createEvent(Event.EventType.BTN_CLEAR_CLICKED));
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + this.getClass().getName());
    }
}

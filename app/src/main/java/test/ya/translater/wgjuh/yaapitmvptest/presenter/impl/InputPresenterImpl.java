package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;


import android.util.Log;

import java.util.Locale;

import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.IEventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.IInputPresenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 07.04.2017.
 */

public class InputPresenterImpl extends BasePresenter<InputView> implements IInputPresenter {
    private final IModel model;
    private final IEventBus eventBus;


    public InputPresenterImpl(IModel model,
                              IEventBus eventBus) {
        this.model = model;
        this.eventBus = eventBus;
    }

    @Override
    public boolean onButtonTranslateClick() {
        if (!view.getTargetText().isEmpty()) {
            eventBus.post(new Event(Event.EventType.BTN_CLEAR_CLICKED));
            startTranslate();
        }
        return true;
    }

    @Override
    public void startTranslate() {
        eventBus.post(eventBus.createEvent(Event.EventType.BTN_CLEAR_CLICKED));
        eventBus.post(eventBus.createEvent(Event.EventType.START_TRANSLATE, view.getTargetText().toLowerCase(Locale.getDefault()).trim()));

    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        addSubscription(eventBus.subscribe(event -> {
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
        eventBus.post(eventBus.createEvent(Event.EventType.BTN_CLEAR_CLICKED));
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + this.getClass().getName());
    }
}

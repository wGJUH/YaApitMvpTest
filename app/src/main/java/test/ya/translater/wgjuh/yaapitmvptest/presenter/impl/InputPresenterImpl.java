package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;


import java.util.Locale;

import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.InputPresenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputView;

import static test.ya.translater.wgjuh.yaapitmvptest.model.Event.EventType.*;


public class InputPresenterImpl extends BasePresenter<InputView> implements InputPresenter {
    private final EventBus eventBus;


    public InputPresenterImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public boolean onButtonTranslateClick() {
        if (!view.getTargetText().isEmpty()) {
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
        addSubscription(eventBus.subscribe(this::onEvent,WORD_TRANSLATED,CHANGE_LANGUAGES));
    }

    @Override
    public void onEvent(Event event) {
        switch (event.eventType) {
            case WORD_TRANSLATED:
                setText(((DictDTO) event.content[0]).getTarget());
                break;
            case CHANGE_LANGUAGES:
                startTranslate();
                break;
            default:
                break;
        }
    }

    private void setText(String text) {
        view.setText(text);

    }

    @Override
    public void clearInput() {
        view.clearText();
        eventBus.post(eventBus.createEvent(Event.EventType.BTN_CLEAR_CLICKED));
    }
}

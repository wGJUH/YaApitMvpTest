package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;

import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.Model;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.LanguageSettingsRecyclerViewAdapter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.SettingLangsFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

/**
 * Created by wGJUH on 11.04.2017.
 */

public class SettingsPresenterImpl extends BasePresenter<SettingLangsFragment> {

    private final Model model;
    private final EventBus eventBus;
    private final Event.EventType direction;

    public SettingsPresenterImpl(Model model, EventBus eventBus, Event.EventType direction) {
        this.model = model;
        this.eventBus = eventBus;
        this.direction = direction;
    }

    public void popBackStack() {
        view.getFragmentManager().popBackStack();
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        initRecyclerView();
    }

    @Override
    public void onEvent(Event event) {

    }

    public void sentLanguageChangedEvent(Event.EventType eventType, String code) {
        eventBus.post(eventBus.createEvent(eventType, code));
    }


    private void initRecyclerView() {
        view.setRecyclerViewAdapter(new LanguageSettingsRecyclerViewAdapter(model.getLangs(), this, direction));
    }

}

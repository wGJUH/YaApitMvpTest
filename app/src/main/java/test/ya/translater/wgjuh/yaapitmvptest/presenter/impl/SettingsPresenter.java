package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;

import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.IEventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.LanguageSettingsRecyclerViewAdapter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.SettingLangsFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

/**
 * Created by wGJUH on 11.04.2017.
 */

public class SettingsPresenter extends BasePresenter<SettingLangsFragment> {

    private final IModel iModel;
    private final IEventBus iEventBus;
    private Event.EventType direction;

    public SettingsPresenter(IModel iModel, IEventBus iEventBus, Event.EventType direction) {
        this.iModel = iModel;
        this.iEventBus = iEventBus;
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

    public void sentLanguageChangedEvent(Event.EventType eventType, String code) {
        iEventBus.post(iEventBus.createEvent(eventType, code));
    }


    public void initRecyclerView() {
        view.setRecyclerViewAdapter(new LanguageSettingsRecyclerViewAdapter(iModel.getLangs(), this, direction));
    }

}

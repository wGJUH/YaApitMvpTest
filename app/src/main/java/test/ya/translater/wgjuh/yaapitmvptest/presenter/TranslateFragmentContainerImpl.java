package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import android.util.Log;

import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IEventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.SettingLangsFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.tabs.TransalteView;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateListFragment;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;


/**
 * Created by wGJUH on 04.04.2017.
 */
// TODO: 06.04.2017 добавить bind, unbind для вьюх срочно !
public class TranslateFragmentContainerImpl extends BasePresenter<TransalteView> {
    private final IModel iModel;
    private final IEventBus iEventBus;

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        updateToolbarLanguages(true);
    }

    /**
     * Данный презентер будет являтся презентером для composite view состоящего из трех фрагментов
     */
    public TranslateFragmentContainerImpl(IModel iModel, IEventBus iEventBus) {
        this.iModel = iModel;
        this.iEventBus = iEventBus;

    }

    public void addFragments(InputTranslateFragment inputTranslateFragment, TranslateListFragment translateListFragment) {
        // TODO: 11.04.2017 Ипсравить баг с падением приложения при недоступности сети
        iModel.updateLanguages();
        view.getTranslateFragmentManager()
                .beginTransaction()
               .add(view.getInputFrame().getId(), inputTranslateFragment, inputTranslateFragment.getClass().getName())
                .add(view.getTranslateFrame().getId(), translateListFragment, translateListFragment.getClass().getName())
                .commit();
    }

    public void onChooseLanguage(Event.EventType eventType) {
        view.getTranslateFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, SettingLangsFragment.newInstance(eventType))
                .addToBackStack(InputTranslateFragment.class.getName())
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .commit();
    }

    public void onChangeLanguages() {
        String from = iModel.getFromLang();
        iModel.setFromLang(iModel.getTranslateLang());
        iModel.setTranslateLang(from);
        EventBus.getInstance()
                .getEventBus()
                .onNext(iEventBus
                        .createEvent(Event
                                .EventType
                                .CHANGE_LANGUAGES));
    }


    public void updateToolbarLanguages(Boolean withSubscribe) {
        if (withSubscribe) {
            if (this.view != null) {
                addSubscription(iEventBus.getEventBus().subscribe(event -> {
                    switch (event.eventType) {
                        case CHANGE_LANGUAGES:
                            view.setToLanguageTextView(iModel.getTranslateLang());
                            view.setFromLanguageTextView(iModel.getFromLang());
                            break;
                        case TARGET_LANGUAGE:
                            iModel.setTranslateLang((String) event.content[0]);
                            EventBus.getInstance()
                                    .getEventBus()
                                    .onNext(iEventBus
                                            .createEvent(Event
                                                    .EventType
                                                    .CHANGE_LANGUAGES));
                            break;
                        case FROM_LANGUAGE:
                            iModel.setFromLang((String) event.content[0]);
                            EventBus.getInstance()
                                    .getEventBus()
                                    .onNext(iEventBus
                                            .createEvent(Event
                                                    .EventType
                                                    .CHANGE_LANGUAGES));
                            break;
                        default:
                            break;
                    }
                }));
            }
        }else {
            view.setToLanguageTextView(iModel.getTranslateLang());
            view.setFromLanguageTextView(iModel.getFromLang());
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + this.getClass().getName());
    }
}

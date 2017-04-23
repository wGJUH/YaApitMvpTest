package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;

import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.Model;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.SettingLangsFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.activity_tabs.TranslateContainerView;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.fragment.InputFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.fragment.TranslateFragment;

import static test.ya.translater.wgjuh.yaapitmvptest.model.Event.EventType.*;

public class TranslateFragmentContainerImpl extends BasePresenter<TranslateContainerView> {
    private final Model model;
    private final EventBus eventBus;

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        updateToolbarLanguages(true);
    }

    @Override
    public void onEvent(Event event) {
        switch (event.eventType) {
            case WORD_TRANSLATED:
                DictDTO dictDTO = (DictDTO) event.content[0];
                String[] langs = dictDTO.getLangs().split("-");

                model.setTranslateLang(langs[1]);
                model.setFromLang(langs[0]);

                view.setToLanguageTextView(model.getLangByCode(langs[1]));
                view.setFromLanguageTextView(model.getLangByCode(langs[0]));

                view.notifyActivityHistoryShown();
                break;
            case CHANGE_LANGUAGES:
                view.setToLanguageTextView(model.getLangByCode(model.getTranslateLang()));
                view.setFromLanguageTextView(model.getLangByCode(model.getFromLang()));
                break;
            case TARGET_LANGUAGE:
                model.setTranslateLang((String) event.content[0]);
                eventBus.post(eventBus
                        .createEvent(Event
                                .EventType
                                .CHANGE_LANGUAGES));
                break;
            case FROM_LANGUAGE:
                model.setFromLang((String) event.content[0]);
                eventBus.post(eventBus
                        .createEvent(Event
                                .EventType
                                .CHANGE_LANGUAGES));
                break;
            default:
                break;
        }
    }

    public TranslateFragmentContainerImpl(Model model, EventBus eventBus) {
        this.model = model;
        this.eventBus = eventBus;

    }

    public void addFragments(InputFragment inputFragment, TranslateFragment translateFragment) {
        model.updateLanguages();
        view.getTranslateFragmentManager()
                .beginTransaction()
                .add(view.getInputFrame().getId(), inputFragment, inputFragment.getClass().getName())
                .add(view.getTranslateFrame().getId(), translateFragment, translateFragment.getClass().getName())
                .commit();
    }

    public void onChooseLanguage(Event.EventType eventType) {
        view.getTranslateFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, SettingLangsFragment.newInstance(eventType))
                .addToBackStack(InputFragment.class.getName())
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .commit();
    }

    public void onChangeLanguages() {
        String from = model.getFromLang();
        model.setFromLang(model.getTranslateLang());
        model.setTranslateLang(from);
        eventBus.post(eventBus
                .createEvent(Event
                        .EventType
                        .CHANGE_LANGUAGES));
    }

    public void updateToolbarLanguages(Boolean withSubscribe) {
        if (withSubscribe) {
            if (this.view != null) {
                addSubscription(eventBus.subscribe(this::onEvent,
                        WORD_TRANSLATED,
                        CHANGE_LANGUAGES,
                        TARGET_LANGUAGE,
                        FROM_LANGUAGE
                ));
            }
        } else {
            view.setToLanguageTextView(model.getLangByCode(model.getTranslateLang()));
            view.setFromLanguageTextView(model.getLangByCode(model.getFromLang()));
        }
    }

    public void updateDataArrays() {
        model.initArrays();
    }
}

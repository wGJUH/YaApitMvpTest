package test.ya.translater.wgjuh.yaapitmvptest.model;


import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbBackEnd;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.LangModel;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelPojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexDictionaryApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexDictionaryApiModule;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiModule;


import rx.Observable;

/**
 * Created by wGJUH on 04.04.2017.
 */
// TODO: 09.04.2017 модель можно реализовать как синглтон
public class ModelImpl implements IModel {

    private static ModelImpl model;
    private final Context context = LeakCanaryApp.getAppContext();
    private final Observable.Transformer schedulersTransformer;
    private final YandexTranslateApiInterface yandexTranslateApiInterface = YandexTranslateApiModule.getYandexTranslateApiInterface();
    private final YandexDictionaryApiInterface yandexDictionaryApiInterface = YandexDictionaryApiModule.getYandexDictionaryApiInterface();
    private final DbBackEnd dbBackEnd;

    private ModelImpl() {
        dbBackEnd = new DbBackEnd(context);
        schedulersTransformer = o -> ((Observable) o).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    // TODO: 10.04.2017 узнать про тестовые Конструкторы
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ModelImpl(DbBackEnd dbBackend) {
        this.dbBackEnd = dbBackend;
        schedulersTransformer = o -> ((Observable) o).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public static IModel getInstance() {
        if (model == null) {
            model = new ModelImpl();
        }
        return model;
    }


    @Override
    public Observable<TranslatePojo> getTranslateForLanguage(String target, String language) {
        return yandexTranslateApiInterface
                .translateForLanguage(DATA.API_KEY, target, language)
                .compose(applySchedulers());
    }

    @Override
    public Observable<DictDTO> getDicTionaryTranslateForLanguage(String target, String language) {
        return yandexDictionaryApiInterface
                .translateForLanguage(DATA.DICT_API_KEY, language, target)
                .compose(applySchedulers());
    }

    @Override
    public DictDTO getHistoryTranslate(String target, String langs) {
        return dbBackEnd.getHistoryTranslate(target, langs);
    }

    /**
     * Обновляем языки в таблице языков для текущей локали
     */
    @Override
    public void updateLanguages() {
        yandexTranslateApiInterface
                .getLangs(DATA.API_KEY, Locale.getDefault().getLanguage())
                .compose(applySchedulers())
                .onErrorReturn(er->null)
                .map(langsDirsModelPojo -> {
                    if(langsDirsModelPojo != null) {
                        dbBackEnd.upateLangs(langsDirsModelPojo);
                    }
                    return langsDirsModelPojo;
                }).subscribe();
    }

    @Override
    public Observable<LangModel> getLangs() {
        return Observable.just(dbBackEnd.getStoredLangs());
    }

    @Override
    public void saveToDBAndNotify(DictDTO dictDTO) {
        Observable
                .just(dbBackEnd.insertHistoryTranslate(dictDTO))
                .compose(applySchedulers())
                .flatMap(aLong -> Observable.just(dbBackEnd.getHistoryTranslate(aLong)))
                .subscribe(dictDTOFromDB -> EventBus
                        .getInstance()
                        .getEventBus()
                        .onNext(EventBus
                                .getInstance()
                                .createEvent(Event
                                        .EventType
                                        .WORD_TRANSLATED, dictDTOFromDB)));

    }

    @Override
    public void addToFavorites(DictDTO dictDTO) {
        dbBackEnd.insertFavoriteFromHistory(dictDTO.getId());
    }

    @Override
    public String getTranslateLangPair() {
        return getFromLang()+"-"+getTranslateLang();
    }


    @Override
    public String getTranslateLang() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Event.EventType.TARGET_LANGUAGE.toString(), "ru");
    }

    @Override
    public void setTranslateLang(String translateLang) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Event.EventType.TARGET_LANGUAGE.toString(),translateLang).commit();
        EventBus.getInstance().getEventBus().onNext(EventBus.getInstance().createEvent(Event.EventType.CHANGE_LANGUAGES));
    }

    @Override
    public String getFromLang() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Event.EventType.FROM_LANGUAGE.toString(), "ru");
    }

    @Override
    public void setFromLang(String translateLang) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Event.EventType.FROM_LANGUAGE.toString(),translateLang).commit();
        EventBus.getInstance().getEventBus().onNext(EventBus.getInstance().createEvent(Event.EventType.CHANGE_LANGUAGES));
    }

    private <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }
}
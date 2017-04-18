package test.ya.translater.wgjuh.yaapitmvptest.model;


import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.util.Locale;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbBackEnd;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.IDbBackEnd;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.LangModel;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;
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
    private final YandexTranslateApiInterface yandexTranslateApiInterface ;
    private final YandexDictionaryApiInterface yandexDictionaryApiInterface ;
    private final DbBackEnd dbBackEnd;
    private IEventBus iEventBus;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ModelImpl(IEventBus iEventBus,YandexTranslateApiInterface yandexTranslateApiInterface,YandexDictionaryApiInterface yandexDictionaryApiInterface) {
        this.iEventBus = iEventBus;
        this.yandexTranslateApiInterface = yandexTranslateApiInterface;
        this.yandexDictionaryApiInterface = yandexDictionaryApiInterface;
        dbBackEnd = new DbBackEnd(context);
        schedulersTransformer = o -> ((Observable) o).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    // TODO: 10.04.2017 узнать про тестовые Конструкторы
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ModelImpl(DbBackEnd dbBackend) {
        this.dbBackEnd = dbBackend;
        this.yandexTranslateApiInterface = null;
        this.yandexDictionaryApiInterface = null;
        schedulersTransformer = o -> ((Observable) o).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public static IModel getInstance() {
        if (model == null) {
                model = new ModelImpl(EventBusImpl.getInstance(),YandexTranslateApiModule.getYandexTranslateApiInterface(),YandexDictionaryApiModule.getYandexDictionaryApiInterface());
        }
        return model;
    }


    @Override
    public Observable<TranslateDTO> getTranslateForLanguage (String target, String language) {
        return yandexTranslateApiInterface
                .translateForLanguage(DATA.API_KEY, target, language)
                .compose(applySchedulers());
    }

    @Override
    public Observable<DictDTO> getDicTionaryTranslateForLanguage(String target, String language) {
        return yandexDictionaryApiInterface
                .translateForLanguage(DATA.DICT_API_KEY, language, target,Locale.getDefault().getLanguage())
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
                .doAfterTerminate(() -> Log.d(DATA.TAG,"Terminated"))
                .subscribe(dictDTOFromDB -> iEventBus
                        .post(iEventBus.createEvent(Event
                                        .EventType
                                        .WORD_TRANSLATED, dictDTOFromDB)),throwable -> Log.e(DATA.TAG,throwable.getMessage()));
    }

    @Override
    public long setFavorites(DictDTO dictDTO) {
        long result = dbBackEnd.getFavoriteId(Integer.parseInt(dictDTO.getId()));
        if(result != -1){
            dbBackEnd.removeHistoryItemFavorite(Long.toString(result));
            result = -1;
        }else{
            result = dbBackEnd.insertFavoriteFromHistory(dictDTO.getId());
            if(result != -1){
                dbBackEnd.setHistoryItemFavorite(dictDTO.getId(),result);
            }
        }
        return result;
    }

    @Override
    public void updateHistoryDate(String id) {
        dbBackEnd.updateHistoryDate(id);
    }

    @Override
    public String getLangByCode(String code) {
        return dbBackEnd.getLangByCode(code);
    }

    @Override
    public Observable<DictDTO> getHistoryListTranslate() {
            return  Observable.from(dbBackEnd.getHistoryListTranslate());
    }

    @Override
    public Observable<DictDTO> getFavoriteListTranslate() {
        return Observable.from(dbBackEnd.getFavoriteListTranslate());
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
    }

    @Override
    public String getFromLang() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Event.EventType.FROM_LANGUAGE.toString(), "ru");
    }

    @Override
    public void setFromLang(String translateLang) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Event.EventType.FROM_LANGUAGE.toString(),translateLang).commit();
    }

    private <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }
}
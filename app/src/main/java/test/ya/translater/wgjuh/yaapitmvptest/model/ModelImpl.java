package test.ya.translater.wgjuh.yaapitmvptest.model;


import android.content.Context;
import android.util.Log;

import java.util.Locale;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbBackEnd;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbOpenHelper;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelPojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexDictionaryApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexDictionaryApiModule;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiModule;


import rx.Observable;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

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


    private ModelImpl() {
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
    public void getLangsDirsForLanguage(String language) {
        Log.d(TAG, "getLangsDirsForLanguage: ");
        yandexTranslateApiInterface.getLangs(DATA.API_KEY, Locale.getDefault().getLanguage()).compose(applySchedulers()).subscribe(new Observer<LangsDirsModelPojo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LangsDirsModelPojo langsDirsModelPojo) {
            }
        });
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
        DbBackEnd dbBackEnd = new DbBackEnd(context);
        return dbBackEnd.getHistoryTranslate(target, langs);
    }

    /**
     * Обновляем языки в таблице языков для текущей локали
     */
    @Override
    public void updateLanguages() {
        DbBackEnd dbBackEnd = new DbBackEnd(context);
        yandexTranslateApiInterface
                .getLangs(DATA.API_KEY, Locale.getDefault().getLanguage())
                .flatMap(langsDirsModelPojo -> {
                    dbBackEnd.upateLangs(langsDirsModelPojo);
                    return null;
                }).compose(applySchedulers()).subscribe();
    }

    @Override
    public void saveToDBAndNotify(DictDTO dictDTO) {
        DbBackEnd dbBackEnd = new DbBackEnd(context);
        Observable.just(dbBackEnd.insertHistoryTranslate(dictDTO))
                .compose(applySchedulers())
                .subscribe(t -> EventBus
                        .getInstance()
                        .getEventBus()
                        .onNext(EventBus
                                .getInstance()
                                .createEvent(Event
                                        .EventType
                                        .WORD_TRANSLATED, dictDTO)));

    }

    @Override
    public String getCurrentLang() {
        return context
                .getSharedPreferences(DATA.APP_PREF, Context.MODE_PRIVATE)
                .getString(DATA.LANG, "en-en");
    }

    private <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }
}
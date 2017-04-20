package test.ya.translater.wgjuh.yaapitmvptest.model;


import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbBackEnd;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexDictionaryApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexDictionaryApiModule;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiModule;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 04.04.2017.
 */
// TODO: 09.04.2017 модель можно реализовать как синглтон
public class ModelImpl implements IModel {

    private static ModelImpl model;
    private final Context context = LeakCanaryApp.getAppContext();
    private final Observable.Transformer schedulersTransformer;
    private final YandexTranslateApiInterface yandexTranslateApiInterface;
    private final YandexDictionaryApiInterface yandexDictionaryApiInterface;
    private final DbBackEnd dbBackEnd;
    private IEventBus iEventBus;
    private Observable cachedRequest;
    private DictDTO lastTranslate;
    private String lastTranslateTarget;
    private LangsDirsModelDTO langsDirsModelDTOs = new LangsDirsModelDTO();
    private List<DictDTO> historyDictDTOs = new ArrayList<>();
    private List<DictDTO> favoriteDictDTOs = new ArrayList<>();

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ModelImpl(IEventBus iEventBus, YandexTranslateApiInterface yandexTranslateApiInterface, YandexDictionaryApiInterface yandexDictionaryApiInterface) {
        this.iEventBus = iEventBus;
        this.yandexTranslateApiInterface = yandexTranslateApiInterface;
        this.yandexDictionaryApiInterface = yandexDictionaryApiInterface;
        dbBackEnd = new DbBackEnd(context);
        schedulersTransformer = o -> ((Observable) o).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
        initArrays();
    }

    private void initArrays() {
        initStoredLangs();
        initHistoryArray();
        initFavoriteArray();
    }

    private void initStoredLangs() {
        if (langsDirsModelDTOs == null) {
            updateLanguages();
        }
    }

    private void initFavoriteArray() {
        if (favoriteDictDTOs.size() == 0) {
            Observable.from(dbBackEnd.getFavoriteListTranslate())
                    //.compose(applySchedulers())
                    .flatMap(s -> Observable.just(new Gson().fromJson(s, DictDTO.class)))
                    .map(dictDTO -> dictDTO.setId(dbBackEnd.getHistoryId(dictDTO)))
                    .map(dictDTO -> dictDTO.setFavorite(dbBackEnd.getFavoriteId(dictDTO)))
                    .subscribe(favoriteDictDTOs::add);
        }
    }

    private void initHistoryArray() {
        if (historyDictDTOs.size() == 0) {
            Observable.from(dbBackEnd.getHistoryListTranslate())
                    //.compose(applySchedulers())
                    .flatMap(s -> Observable.just(new Gson().fromJson(s, DictDTO.class)))
                    .map(dictDTO -> dictDTO.setId(dbBackEnd.getHistoryId(dictDTO)))
                    .map(dictDTO -> dictDTO.setFavorite(dbBackEnd.getFavoriteId(dictDTO)))
                    .subscribe(historyDictDTOs::add);
        }
    }

    @Override
    public LangsDirsModelDTO getLangsDirsModelDTOs() {
        return langsDirsModelDTOs;
    }

    @Override
    public List<DictDTO> getHistoryDictDTOs() {
        return historyDictDTOs;
    }

    @Override
    public List<DictDTO> getFavoriteDictDTOs() {
        return favoriteDictDTOs;
    }

    @Override
    public DictDTO getLastTranslate() {
        return lastTranslate;
    }

    @Override
    public void setLastTranslate(DictDTO lastTranslate) {
        this.lastTranslate = lastTranslate;
    }

    @Override
    public void insertHistoryDictDTOs(DictDTO historyDictDTO) {
        historyDictDTOs.add(0, historyDictDTO);
    }

    @Override
    public void insertHistoryDictDTOToTheTale(DictDTO historyDictDTO) {
        historyDictDTOs.add(historyDictDTO);
    }

    @Override
    public void insertFavoriteDictDTOs(DictDTO favoriteDictDTO) {
        favoriteDictDTOs.add(0, favoriteDictDTO);
    }


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
            model = new ModelImpl(EventBusImpl.getInstance(), YandexTranslateApiModule.getYandexTranslateApiInterface(), YandexDictionaryApiModule.getYandexDictionaryApiInterface());
        }
        return model;
    }


    @Override
    public Observable<TranslateDTO> getTranslateForLanguage(String target, String language) {
        return yandexTranslateApiInterface
                .translateForLanguage(DATA.API_KEY, target, language)
                .compose(applySchedulers());
    }

    @Override
    public Observable<DictDTO> getDicTionaryTranslateForLanguage(String target, String language) {
        return yandexDictionaryApiInterface
                .translateForLanguage(DATA.DICT_API_KEY, language, target, Locale.getDefault().getLanguage())
                .compose(applySchedulers());
    }

    @Override
    public DictDTO getHistoryTranslate(String target, String langs) {
        DictDTO dictDTO = new DictDTO();
        dictDTO.setTarget(target);
        dictDTO.setLangs(langs);
        int position = historyDictDTOs.indexOf(dictDTO);
        if (position != -1) {
            return historyDictDTOs.get(position);
        } else {
            return null;
        }
    }

    @Override
    public void updateLanguages() {
        langsDirsModelDTOs = dbBackEnd.getStoredLangs();
        String[] strings = (LeakCanaryApp.getAppContext()).getResources().getStringArray(R.array.ru_ui_languages);
        if(langsDirsModelDTOs.getLangs().size() == 0) {
            for (String s : strings) {
                String[] singleLang = s.split("\\|");
                langsDirsModelDTOs.getLangs().put(singleLang[1], singleLang[0]);
            }
        }
        yandexTranslateApiInterface
                .getLangs(DATA.API_KEY, Locale.getDefault().getLanguage())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .retryWhen(observable -> observable/*
                        .zipWith(Observable.range(1, 3), (o, integer) -> 0)*/
                        .flatMap(o -> Observable.timer(3000, TimeUnit.MILLISECONDS)))
                .subscribe(langsDirsModelDTO -> {
                    dbBackEnd.upateLangs(langsDirsModelDTO);
                    langsDirsModelDTOs = dbBackEnd.getStoredLangs();
                    iEventBus.post(iEventBus.createEvent(Event.EventType.CHANGE_LANGUAGES));
                },
                        Throwable::printStackTrace);
    }

    @Override
    public LangsDirsModelDTO getLangs() {
        return langsDirsModelDTOs;
    }

    @Override
    public void saveToDBAndNotify(DictDTO dictDTO) {
        if (dbBackEnd.getHistoryId(dictDTO).equals("-1")) {
            Observable
                    .just(dbBackEnd.insertHistoryTranslate(dictDTO))                            //добавляем объект в базу
                    .compose(applySchedulers())
                    .flatMap(aLong -> Observable.just(dictDTO.setId(Long.toString(aLong))))     //Уставнавливаем объекту id из базы
                    .map(dictDTOFromDB -> dictDTO.setFavorite(dbBackEnd.getFavoriteId(dictDTO)))//Устанавливаем favorite id из базы
                    .map(dictDTOFromDB -> {
                        historyDictDTOs.add(dictDTO);                                           //Добавляем в хранилище приложения
                        return dictDTOFromDB;
                    })
                    .subscribe(dictDTOFromDB -> iEventBus
                                    .post(iEventBus.createEvent(Event
                                            .EventType
                                            .WORD_TRANSLATED, dictDTOFromDB)),                              //В случае успех уведомляем об успехе
                            throwable -> Log.e(TAG, throwable.getMessage()));              //В случае ошибки выводим в лог ошибку
        } else {
            iEventBus
                    .post(iEventBus.createEvent(Event
                            .EventType
                            .WORD_TRANSLATED, model.getHistoryTranslate(dictDTO.getTarget(), dictDTO.getLangs())));
        }
    }

    @Override
    public long setFavorites(DictDTO dictDTO) {
        long result = -1;
        if (!dbBackEnd.getFavoriteId(dictDTO).equals("-1")) {
            dbBackEnd.removeFavoriteItemAndUpdateHistory(dictDTO);
            dictDTO.setFavorite("-1");
            favoriteDictDTOs.set(favoriteDictDTOs.indexOf(dictDTO), dictDTO);
            return result;
        } else {
            dictDTO.setFavorite(Long.toString(dbBackEnd.insertFavoriteItem(dictDTO)));
            if (!dictDTO.getFavorite().equals("-1") && !dbBackEnd.getHistoryId(dictDTO).equals("-1")) {
                dictDTO.setId(dbBackEnd.getHistoryId(dictDTO));
                if (favoriteDictDTOs.contains(dictDTO)) {
                    favoriteDictDTOs.set(favoriteDictDTOs.indexOf(dictDTO), dictDTO);
                } else {
                    favoriteDictDTOs.add(0, dictDTO);
                }
                dbBackEnd.setHistoryItemFavorite(dictDTO, result);
            }
        }
        return result;
    }

    @Override
    public void updateHistoryDate(String id) {
        dbBackEnd.updateHistoryDate(id);
    }

    public void initZipTranslate(String target, String lang) {
        cachedRequest = getDicTionaryTranslateForLanguage(target, lang)
                .compose(applySchedulers())
                .zipWith(getTranslateForLanguage(target, lang), (dictDTO, translateDTO) -> {
                    dictDTO.setCommonTranslate(translateDTO.getText());
                    dictDTO.setTarget(target);
                    dictDTO.setLangs(translateDTO.getLang());
                    return dictDTO;
                })
                .flatMap(Observable::just)
                .compose(applySchedulers())
                .cache();
    }

    public Observable<DictDTO> getZipTranslate() {
        return cachedRequest == null ? Observable.empty() : cachedRequest;
    }

    @Override
    public String getLangByCode(String code) {
        if (langsDirsModelDTOs.getLangs().size() > 0) {
            return langsDirsModelDTOs.getLangs().get(code);
        } else return code;
    }

    @Override
    public int removeHistoryItem(DictDTO dictDTO) {
        int position = historyDictDTOs.indexOf(dictDTO);
        historyDictDTOs.remove(position);
        dbBackEnd.removeHistoryItem(dictDTO);
        return position;
    }

    @Override
    public DictDTO getFavoriteTranslate(String targetText, String translateDirection) {
        DictDTO dictDTO = new DictDTO();
        dictDTO.setTarget(targetText);
        dictDTO.setLangs(translateDirection);
        int position = favoriteDictDTOs.indexOf(dictDTO);
        if (position != -1) {
            return favoriteDictDTOs.get(position);
        } else {
            return null;
        }
    }

    @Override
    public void freeCachedOBservable() {
        cachedRequest = null;
    }

    @Override
    public String getTranslateLangPair() {
        return getFromLang() + "-" + getTranslateLang();
    }


    @Override
    public String getTranslateLang() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Event.EventType.TARGET_LANGUAGE.toString(), "ru");
    }

    @Override
    public void setTranslateLang(String translateLang) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Event.EventType.TARGET_LANGUAGE.toString(), translateLang).commit();
    }

    @Override
    public String getFromLang() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Event.EventType.FROM_LANGUAGE.toString(), "ru");
    }

    @Override
    public void setFromLang(String translateLang) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Event.EventType.FROM_LANGUAGE.toString(), translateLang).commit();
    }

    private <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }

    @Override
    public String getLastTranslateTarget() {
        return lastTranslateTarget;
    }

    @Override
    public void setLastTranslateTarget(String lastTranslateTarget) {
        this.lastTranslateTarget = lastTranslateTarget;
    }

    @Override
    public void updateHistoryDto(int position, DictDTO dictDTO) {
        historyDictDTOs.set(position, dictDTO);
    }

    @Override
    public void moveHistoryDictDto(int oldPosition, DictDTO historyDictDTO) {
        int position = historyDictDTOs.indexOf(historyDictDTO);
        if (position != -1) {
            historyDictDTOs.remove(position);
            historyDictDTOs.add(0, historyDictDTO);
        }
    }

    @Override
    public void insertFavoriteDictDTOToTheTale(DictDTO dictDTO) {
        favoriteDictDTOs.add(dictDTO);
    }

    @Override
    public void updateFavoriteDto(int position, DictDTO dictDTO) {
        favoriteDictDTOs.set(position, dictDTO);
    }

    @Override
    public void moveFavoriteDictDto(int oldPosition, DictDTO dictDTO) {
        int position = favoriteDictDTOs.indexOf(dictDTO);
        if (position != -1) {
            favoriteDictDTOs.remove(position);
            favoriteDictDTOs.add(0, dictDTO);
        }
    }
}
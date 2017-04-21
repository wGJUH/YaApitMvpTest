package test.ya.translater.wgjuh.yaapitmvptest.model;


import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.App;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbBackEndImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexDictionaryApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexDictionaryApiModule;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiModule;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;


public class ModelImpl implements Model {

    private static ModelImpl model;
    private final Context context = App.getAppContext();
    private final Observable.Transformer schedulersTransformer;
    private final YandexTranslateApiInterface yandexTranslateApiInterface;
    private final YandexDictionaryApiInterface yandexDictionaryApiInterface;
    private final DbBackEndImpl dbBackEndImpl;
    private EventBus eventBus;
    private Observable cachedRequest;
    private DictDTO lastTranslate;
    private String lastTranslateTarget;
    private LangsDirsModelDTO langsDirsModelDTOs = new LangsDirsModelDTO();
    private final List<DictDTO> historyDictDTOs = new ArrayList<>();
    private final List<DictDTO> favoriteDictDTOs = new ArrayList<>();

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ModelImpl(EventBus eventBus, YandexTranslateApiInterface yandexTranslateApiInterface, YandexDictionaryApiInterface yandexDictionaryApiInterface) {
        this.eventBus = eventBus;
        this.yandexTranslateApiInterface = yandexTranslateApiInterface;
        this.yandexDictionaryApiInterface = yandexDictionaryApiInterface;
        dbBackEndImpl = new DbBackEndImpl(context);
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
            Observable.from(dbBackEndImpl.getFavoriteListTranslate())
                    //.compose(applySchedulers())
                    .flatMap(s -> Observable.just(new Gson().fromJson(s, DictDTO.class)))
                    .map(dictDTO -> dictDTO.setId(dbBackEndImpl.getHistoryId(dictDTO)))
                    .map(dictDTO -> dictDTO.setFavorite(dbBackEndImpl.getFavoriteId(dictDTO)))
                    .subscribe(favoriteDictDTOs::add);
        }
    }

    private void initHistoryArray() {
        if (historyDictDTOs.size() == 0) {
            Observable.from(dbBackEndImpl.getHistoryListTranslate())
                    //.compose(applySchedulers())
                    .flatMap(s -> Observable.just(new Gson().fromJson(s, DictDTO.class)))
                    .map(dictDTO -> dictDTO.setId(dbBackEndImpl.getHistoryId(dictDTO)))
                    .map(dictDTO -> dictDTO.setFavorite(dbBackEndImpl.getFavoriteId(dictDTO)))
                    .subscribe(historyDictDTOs::add);
        }
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
    public void insertFavoriteDictDTOs(DictDTO favoriteDictDTO) {
        favoriteDictDTOs.add(0, favoriteDictDTO);
    }


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ModelImpl(DbBackEndImpl dbBackendImpl) {
        this.dbBackEndImpl = dbBackendImpl;
        this.yandexTranslateApiInterface = null;
        this.yandexDictionaryApiInterface = null;
        schedulersTransformer = o -> ((Observable) o).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public static Model getInstance() {
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
        langsDirsModelDTOs = dbBackEndImpl.getStoredLangs();
        String[] strings = (App.getAppContext()).getResources().getStringArray(R.array.ru_ui_languages);
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
                .subscribe(langsDirsModelDTO -> {
                            dbBackEndImpl.upateLangs(langsDirsModelDTO);
                            langsDirsModelDTOs = dbBackEndImpl.getStoredLangs();
                            eventBus.post(eventBus.createEvent(Event.EventType.CHANGE_LANGUAGES));
                        },
                        Throwable::printStackTrace);
    }

    @Override
    public LangsDirsModelDTO getLangs() {
        return langsDirsModelDTOs;
    }

    @Override
    public Observable<DictDTO> saveToDB(DictDTO dictDTO) {
        if (dbBackEndImpl.getHistoryId(dictDTO).equals("-1")) {
            return Observable
                    .just(dbBackEndImpl.insertHistoryTranslate(dictDTO))                            //добавляем объект в базу
                    .compose(applySchedulers())
                    .flatMap(aLong -> Observable.just(dictDTO.setId(Long.toString(aLong))))     //Уставнавливаем объекту id из базы
                    .map(dbBackEndImpl::getFavoriteId)
                    .map(dictDTO::setFavorite);
        }else {
            return Observable.just(model.getHistoryTranslate(dictDTO.getTarget(), dictDTO.getLangs()));
        }
    }

    @Override
    public void setFavorites(DictDTO dictDTO) {
        long result = -1;
        if (!dbBackEndImpl.getFavoriteId(dictDTO).equals("-1")) {
            dbBackEndImpl.removeFavoriteItemAndUpdateHistory(dictDTO);
            dictDTO.setFavorite("-1");
            favoriteDictDTOs.set(favoriteDictDTOs.indexOf(dictDTO), dictDTO);
        } else {
            dictDTO.setFavorite(Long.toString(dbBackEndImpl.insertFavoriteItem(dictDTO)));
            if (!dictDTO.getFavorite().equals("-1") && !dbBackEndImpl.getHistoryId(dictDTO).equals("-1")) {
                dictDTO.setId(dbBackEndImpl.getHistoryId(dictDTO));
                if (favoriteDictDTOs.contains(dictDTO)) {
                    favoriteDictDTOs.set(favoriteDictDTOs.indexOf(dictDTO), dictDTO);
                } else {
                    favoriteDictDTOs.add(0, dictDTO);
                }
                dbBackEndImpl.setHistoryItemFavorite(dictDTO, result);
            }else if (favoriteDictDTOs.contains(dictDTO)) {
                favoriteDictDTOs.set(favoriteDictDTOs.indexOf(dictDTO), dictDTO);
            }else{
                favoriteDictDTOs.add(0, dictDTO);
            }
        }
    }

    @Override
    public void updateHistoryDate(String id) {
        dbBackEndImpl.updateHistoryDate(id);
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
        dbBackEndImpl.removeHistoryItem(dictDTO);
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
        if (oldPosition != -1) {
            historyDictDTOs.remove(oldPosition);
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
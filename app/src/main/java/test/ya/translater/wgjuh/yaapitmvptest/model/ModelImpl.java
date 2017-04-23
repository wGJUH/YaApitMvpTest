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

/**
 * Модель для работы с данными приложения
 */
public class ModelImpl implements Model {

    private static ModelImpl model;
    private Context context = App.getAppContext();
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
    ModelImpl(EventBus eventBus,
              YandexTranslateApiInterface yandexTranslateApiInterface,
              YandexDictionaryApiInterface yandexDictionaryApiInterface,
              DbBackEndImpl dbBackEnd,
              Context context) {
        this.eventBus = eventBus;
        this.yandexTranslateApiInterface = yandexTranslateApiInterface;
        this.yandexDictionaryApiInterface = yandexDictionaryApiInterface;
        dbBackEndImpl = dbBackEnd;
        this.context = context;
        schedulersTransformer = o -> o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
        initArrays();
    }

    @Override
    public void initArrays() {
        initStoredLangs();
        initHistoryArray();
        initFavoriteArray();
    }

    private void initStoredLangs() {
        if (langsDirsModelDTOs.getLangs().size() == 0) {
            updateLanguages();
        }
    }

    private void initFavoriteArray() {
        if (favoriteDictDTOs.size() == 0) {
            Observable.from(dbBackEndImpl.getFavoriteListTranslate())
                    .flatMap(s -> Observable.just(new Gson().fromJson(s, DictDTO.class)))
                    .map(dictDTO -> dictDTO.setId(dbBackEndImpl.getHistoryId(dictDTO)))
                    .map(dictDTO -> dictDTO.setFavorite(dbBackEndImpl.getFavoriteId(dictDTO)))
                    .subscribe(favoriteDictDTOs::add);
        }
    }

    private void initHistoryArray() {
        if (historyDictDTOs.size() == 0) {
            Observable.from(dbBackEndImpl.getHistoryListTranslate())
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
        schedulersTransformer = o -> o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public static Model getInstance() {
        if (model == null) {
            model = new ModelImpl(EventBusImpl.getInstance(),
                    YandexTranslateApiModule.getYandexTranslateApiInterface(),
                    YandexDictionaryApiModule.getYandexDictionaryApiInterface(),
                    new DbBackEndImpl(App.getAppContext()),
                    App.getAppContext());
        }
        return model;
    }

    /**
     * Метод создает запрос на перевод через Yandex translate api
     *
     * @param target   цель перевода
     * @param language направление перевода в формате "en-ru"
     * @return Результат запроса.
     * @see <a href="https://tech.yandex.ru/translate/">API Переводчика</a>
     */
    @Override
    public Observable<TranslateDTO> getTranslateForLanguage(String target, String language) {
        return yandexTranslateApiInterface
                .translateForLanguage(DATA.API_KEY, target, language)
                .compose(applySchedulers());
    }

    /**
     * Метод создает запрос на первеод через Yandex dictionary api
     *
     * @param target   цель перевода
     * @param language направление перевода в формате "en-ru"
     * @return Результат запроса.
     * @see <a href="https://tech.yandex.ru/dictionary/">API Словаря</a>
     */
    @Override
    public Observable<DictDTO> getDicTionaryTranslateForLanguage(String target, String language) {
        return yandexDictionaryApiInterface
                .translateForLanguage(DATA.DICT_API_KEY, language, target, Locale.getDefault().getLanguage())
                .compose(applySchedulers());
    }

    /**
     * Получает перевод и базы истории
     *
     * @param target цель перевода
     * @param langs  направление перевода в формате "en-ru"
     * @return Хранимое в базе значение или Null
     */
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

    /**
     * Метод запрашивает список языков из базы данных.
     * <p>В случае если в базе нет языков они будут немедленно получены
     * из локального хранилища, после этого делается запрос
     * к yandex translate api для получения поддерживаемых языков
     * относительно текущей локализации телефона
     *
     * @see <a href="https://tech.yandex.ru/translate/">API Переводчика</a>
     */
    @Override
    public void updateLanguages() {
        langsDirsModelDTOs = dbBackEndImpl.getStoredLangs();
        String[] strings = context.getResources().getStringArray(R.array.ru_ui_languages);
        if (langsDirsModelDTOs.getLangs().size() == 0) {
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

    /**
     * Метод проверяет есть ли в базе данный объект,
     * если да, то возвращает данный объект.
     * <p>В случае если
     * объект отсутствует, он будет сохранен в бд.
     *
     * @param dictDTO объект который необходмимо сохранить в бд.
     */
    @Override
    public Observable<DictDTO> saveToDB(DictDTO dictDTO) {
        if (dbBackEndImpl.getHistoryId(dictDTO).equals("-1")) {
            return Observable
                    .just(dbBackEndImpl.insertHistoryTranslate(dictDTO))                            //добавляем объект в базу
                    .compose(applySchedulers())
                    .flatMap(aLong -> Observable.just(dictDTO.setId(Long.toString(aLong))))     //Уставнавливаем объекту id из базы
                    .map(dbBackEndImpl::getFavoriteId)
                    .map(dictDTO::setFavorite);
        } else {
            return Observable.just(model.getHistoryTranslate(dictDTO.getTarget(), dictDTO.getLangs()));
        }
    }

    /**
     * Метод устанавливает флаг избранное для целевого объекта
     * <ul>
     * <li><B>В случае когда объект есть в таблице избранное:</B></li>
     * <ul>
     * <li>Объект удаляется из таблицы избранное</li>
     * <li>Объекту выставляется признак отсутствия в избранном</li>
     * <li>В локальном хранилище избранного обновляется объект</li>
     * </ul>
     * <li><B>В случае когда объекта нет в таблице избранное:</B></li>
     * <ul>
     * <li>Объект добавляется в таблицу избранное</li>
     * <li>Добавляется в локальное хранилище или обновляется уже существующий элемент</li>
     * </ul>
     * </ul>
     */
    @Override
    public void setFavorites(DictDTO dictDTO) {
        if (!dbBackEndImpl.getFavoriteId(dictDTO).equals("-1")) {
            dbBackEndImpl.removeFavoriteItem(dictDTO);
            dictDTO.setFavorite("-1");
            favoriteDictDTOs.set(favoriteDictDTOs.indexOf(dictDTO), dictDTO);
        } else {
            dictDTO.setFavorite(Long.toString(dbBackEndImpl.insertFavoriteItem(dictDTO)));
            dictDTO.setId(dbBackEndImpl.getHistoryId(dictDTO));
            if (favoriteDictDTOs.contains(dictDTO)) {
                favoriteDictDTOs.set(favoriteDictDTOs.indexOf(dictDTO), dictDTO);
            } else {
                insertFavoriteDictDTOs(dictDTO);
            }
        }
    }

    /**
     * Метод обновляет дату последнего просмотра слова в таблице истории
     * @param id идентификатор объекта в таблице который необходимо обновить
     */
    @Override
    public void updateHistoryDate(String id) {
        dbBackEndImpl.updateHistoryDate(id);
    }

    /**
     * Метод инициализирует запрос на перевод с использованием
     * <a href="https://tech.yandex.ru/dictionary/">API Словаря</a>,
     * <a href="https://tech.yandex.ru/translate/">API Переводчика</a>
     * @param target цель перевода
     * @param lang   направление перевода в виде "en-ru"
     */
    public void initZipTranslate(String target, String lang) {
        cachedRequest = getDicTionaryTranslateForLanguage(target, lang)
                .compose(applySchedulers())
                .onErrorReturn(throwable -> new DictDTO())
                .zipWith(getTranslateForLanguage(target, lang), (dictDTO, translateDTO) -> {
                    dictDTO.setCommonTranslate(translateDTO.getText());
                    dictDTO.setTarget(target);
                    dictDTO.setLangs(translateDTO.getLang());
                    return dictDTO;
                })
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

    /**
     * Методполучает объект из временного локального хранилища favoriteDictDTOs
     * @param targetText               цель перевода
     * @param translateDirection    направление перевода в виде "en-ru"
     * @return результат перевода или null
     */
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
    public void updateFavoriteDto(int position, DictDTO dictDTO) {
        favoriteDictDTOs.set(position, dictDTO);
    }
}
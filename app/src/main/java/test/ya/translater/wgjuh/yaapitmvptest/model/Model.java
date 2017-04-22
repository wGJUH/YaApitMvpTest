package test.ya.translater.wgjuh.yaapitmvptest.model;

import java.util.List;

import rx.Observable;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;


public interface Model {

    Observable<TranslateDTO> getTranslateForLanguage (String target, String language);

    Observable<DictDTO> getZipTranslate();

    Observable<DictDTO> saveToDB(DictDTO dictDTO);

    Observable<DictDTO> getDicTionaryTranslateForLanguage(String target, String language);

    int removeHistoryItem(DictDTO dictDTO);

    LangsDirsModelDTO getLangs();

    String getTranslateLangPair();

    String getTranslateLang();

    String getFromLang();

    String getLangByCode(String code);

    String getLastTranslateTarget();

    DictDTO getFavoriteTranslate(String targetText, String translateDirection);

    DictDTO getHistoryTranslate(String target, String langs);

    DictDTO getLastTranslate();

    List<DictDTO> getFavoriteDictDTOs();

    List<DictDTO> getHistoryDictDTOs();

    void initArrays();

    void insertHistoryDictDTOs(DictDTO historyDictDTO);

    void insertFavoriteDictDTOs(DictDTO favoriteDictDTO);

    void setLastTranslate(DictDTO lastTranslate);

    void setTranslateLang(String translateLang);

    void setFromLang(String translateLang);

    void updateLanguages();

    void setFavorites(DictDTO dictDTO);

    void updateHistoryDate(String id);

    void initZipTranslate(String target, String lang);

    void freeCachedOBservable();

    void setLastTranslateTarget(String lastTranslateTarget);

    void updateHistoryDto(int position, DictDTO dictDTO);

    void moveHistoryDictDto(int oldPosition, DictDTO historyDictDTO);

    void updateFavoriteDto(int position, DictDTO dictDTO);
}

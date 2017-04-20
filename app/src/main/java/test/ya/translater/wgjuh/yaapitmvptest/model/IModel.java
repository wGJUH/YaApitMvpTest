package test.ya.translater.wgjuh.yaapitmvptest.model;

import java.util.List;

import rx.Observable;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;

/**
 * Created by wGJUH on 04.04.2017.
 */

public interface IModel {

    List<DictDTO> getHistoryDictDTOs();

    List<DictDTO> getFavoriteDictDTOs();

    DictDTO getLastTranslate();

    void setLastTranslate(DictDTO lastTranslate);

    void insertHistoryDictDTOs(DictDTO historyDictDTO);

    void insertFavoriteDictDTOs(DictDTO favoriteDictDTO);

    Observable<TranslateDTO> getTranslateForLanguage (String target, String language);

    Observable<DictDTO> getDicTionaryTranslateForLanguage(String target, String language);

    DictDTO getHistoryTranslate(String target, String langs);

    String getTranslateLangPair();

    String getTranslateLang();

    String getFromLang();

    void setTranslateLang(String translateLang);

    void setFromLang(String translateLang);

    void updateLanguages();

    LangsDirsModelDTO getLangs();

    DictDTO setFavorites(DictDTO dictDTO);

    void updateHistoryDate(String id);

    void initZipTranslate(String target, String lang);

    Observable<DictDTO> getZipTranslate();

    String getLangByCode(String code);

    int removeHistoryItem(DictDTO dictDTO);

    DictDTO getFavoriteTranslate(String targetText, String translateDirection);

    void freeCachedOBservable();

    String getLastTranslateTarget();

    void setLastTranslateTarget(String lastTranslateTarget);

    void updateHistoryDto(int position, DictDTO dictDTO);

    void moveHistoryDictDto(int oldPosition, DictDTO historyDictDTO);

    void insertFavoriteDictDTOToTheTale(DictDTO dictDTO);

    void updateFavoriteDto(int position, DictDTO dictDTO);

    void moveFavoriteDictDto(int oldPosition, DictDTO dictDTO);


    Observable<DictDTO> saveToDB(DictDTO dictDTO);
}

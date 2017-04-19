package test.ya.translater.wgjuh.yaapitmvptest.model;

import rx.Observable;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;

/**
 * Created by wGJUH on 04.04.2017.
 */

public interface IModel {

    Observable<TranslateDTO> getTranslateForLanguage (String target, String language);

    Observable<DictDTO> getDicTionaryTranslateForLanguage(String target, String language);

    Observable<DictDTO> getHistoryListTranslate();

    Observable<DictDTO> getFavoriteListTranslate();

    Observable<LangsDirsModelDTO> getLangs();

    DictDTO getHistoryTranslate(String target, String langs);

    String getTranslateLangPair();

    String getTranslateLang();

    String getFromLang();

    void setTranslateLang(String translateLang);

    void setFromLang(String translateLang);

    void updateLanguages();

    void saveToDBAndNotify(DictDTO dictDTO);

    long setFavorites(DictDTO dictDTO);

    void updateHistoryDate(String id);

    String getLangByCode(String code);

    int removeHistoryItem(DictDTO dictDTO);

    DictDTO getFavoriteTranslate(String targetText, String translateDirection);
}

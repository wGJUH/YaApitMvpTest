package test.ya.translater.wgjuh.yaapitmvptest.model.db;

import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;

/**
 * Created by wGJUH on 16.04.2017.
 */

interface DbBackEnd {

    long insertHistoryTranslate(DictDTO dictDTO);

    long insertFavoriteItem(DictDTO dictDTO);

    void removeHistoryItem(DictDTO dictDTO);

    void removeFavoriteItem(DictDTO dictDTO);

    List<String> getHistoryListTranslate();

    List<String> getFavoriteListTranslate();

    String getHistoryTranslate(String target, String langs);

    void upateLangs(LangsDirsModelDTO langsDirsModelDTO);

    LangsDirsModelDTO getStoredLangs();

    void updateHistoryDate(String id);

    String getHistoryId(DictDTO dictDTO);

    String getFavoriteId(DictDTO dictDTO);
}

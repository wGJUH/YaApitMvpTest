package test.ya.translater.wgjuh.yaapitmvptest.model.db;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

/**
 * Created by wGJUH on 25.03.2017.
 */

public interface    Contractor {
    String DB_NAME = "YaApiTest.sqlite";
    String DB_TABLE_LANGS = "Langs";
    String DB_TABLE_FAVORITES = "Bookmark";
    String DB_TABLE_HISTORY = "History";

    long updateHistoryDate(String id);

    String getHistoryId(DictDTO dictDTO);

    String getFavoriteId(DictDTO dictDTO);

    interface Bookmark {
        String ID = "id";
    }

    interface Langs {
        String ID = "id";
        String NAME = "name";
        String CODE = "code";
    }

    interface Translate {
        String ID = "id";
        String TARGET = "target";
        String LANGS = "langs";
        String JSON = "json";
        String DATE = "date";
        String FAVORITE = "favorite";
    }
    interface Favorite{
        String ID = "id";
        String TARGET = "target";
        String LANGS = "langs";
        String JSON = "json";
        String DATE = "date";
    }
}
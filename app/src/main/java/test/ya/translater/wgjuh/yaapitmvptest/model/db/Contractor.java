package test.ya.translater.wgjuh.yaapitmvptest.model.db;

/**
 * Created by wGJUH on 25.03.2017.
 */

public interface Contractor {
    String DB_NAME = "YaApiTest.sqlite";
    String DB_TABLE_LANGS = "LANGS";
    String DB_TABLE_BOOKMARKS = "Bookmark";
    String DB_TABLE_HISTORY = "History";

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
    }
}
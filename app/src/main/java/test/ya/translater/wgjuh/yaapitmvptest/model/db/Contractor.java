package test.ya.translater.wgjuh.yaapitmvptest.model.db;

interface Contractor {
    String DB_NAME = "YaApiTest.sqlite";
    String DB_TABLE_LANGS = "Langs";
    String DB_TABLE_FAVORITES = "Bookmark";
    String DB_TABLE_HISTORY = "History";

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
    }

    interface Favorite {
        String ID = "id";
        String TARGET = "target";
        String LANGS = "langs";
        String JSON = "json";
        String DATE = "date";
    }
}
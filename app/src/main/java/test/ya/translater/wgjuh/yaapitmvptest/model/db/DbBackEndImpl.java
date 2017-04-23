package test.ya.translater.wgjuh.yaapitmvptest.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;


public class DbBackEndImpl implements Contractor, DbBackEnd {
    private final DbOpenHelper mDbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;


    public DbBackEndImpl(Context context) {
        mDbOpenHelper = new DbOpenHelper(context);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public DbBackEndImpl(DbOpenHelper dbOpenHelper) {
        mDbOpenHelper = dbOpenHelper;
    }
    /**
     * Метод добавления объекта в историю
     *
     * @param dictDTO объект добавляемый в историю
     * @return положение объекта в таблице DB_TABLE_HISTORY
     * @see test.ya.translater.wgjuh.yaapitmvptest.model.db.Contractor.Translate
     */
    @Override
    public long insertHistoryTranslate(DictDTO dictDTO) {
        Long inserted;
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Translate.TARGET, dictDTO.getTarget());
        contentValues.put(Translate.LANGS, dictDTO.getLangs());
        contentValues.put(Translate.JSON, new Gson().toJson(dictDTO));
        contentValues.put(Translate.DATE, System.currentTimeMillis());
        inserted = sqLiteDatabase.insert(DB_TABLE_HISTORY, null, contentValues);
        if (inserted != -1) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
        return inserted;
    }
    /**
     * Метод добавления объекта в избранное
     *
     * @param dictDTO объект добавляемый в избранное
     * @return положение объекта в таблице DB_TABLE_FAVORITES
     * @see test.ya.translater.wgjuh.yaapitmvptest.model.db.Contractor.Translate
     */
    @Override
    public long insertFavoriteItem(DictDTO dictDTO) {
        Long inserted;
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Favorite.TARGET, dictDTO.getTarget());
        contentValues.put(Favorite.LANGS, dictDTO.getLangs());
        contentValues.put(Favorite.JSON, new Gson().toJson(dictDTO));
        contentValues.put(Favorite.DATE, System.currentTimeMillis());
        inserted = sqLiteDatabase.insert(DB_TABLE_FAVORITES, null, contentValues);
        if (inserted != -1) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
        return inserted;
    }

    /**
     * Метод получения перевода из таблицы истории по слову перевода и языку
     *
     * @param target слово которое переводили
     * @param langs  направление перевода в формате "en-ru"
     * @return json строку перевода
     */
    @Override
    public String getHistoryTranslate(String target, String langs) {
        String json = null;
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor c = sqLiteDatabase.query(
                DB_TABLE_HISTORY, new String[]{Translate.JSON},  // => SELECT page_id FROM pages
                Translate.TARGET + "=? AND " + Translate.LANGS + " =?", new String[]{target, langs},  // => WHERE page_url='url'
                null, null, null);
        if (c.moveToFirst()) {
            json = c.getString(c.getColumnIndex(Favorite.JSON));
        }
        c.close();
        return json;
    }

    /**
     * Метод обновляет список доступных языков в таблице Языки
     *
     * @param langsDirsModelDTO объект хранящий языки
     * @see test.ya.translater.wgjuh.yaapitmvptest.model.db.Contractor.Langs
     */
    @Override
    public void upateLangs(LangsDirsModelDTO langsDirsModelDTO) {
        long inserted = -1;
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete(DB_TABLE_LANGS, null, null);
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, String> stringStringMap
                : langsDirsModelDTO.getLangs().entrySet()) {
            contentValues.put(Langs.CODE, stringStringMap.getKey());
            contentValues.put(Langs.NAME, stringStringMap.getValue());
            inserted = sqLiteDatabase.insert(DB_TABLE_LANGS, null, contentValues);
        }
        if (inserted != -1) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
    }

    /**
     * Метод возвращает хранимые в базе языки
     * @return хранимые в базе языки
     */
    @Override
    public LangsDirsModelDTO getStoredLangs() {
        LangsDirsModelDTO langModel = new LangsDirsModelDTO();
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_LANGS, new String[]{Langs.CODE, Langs.NAME}, null, null, null, null, Langs.NAME);
        if (cursor.moveToFirst()) {
            do {
                langModel.addLang(cursor.getString(cursor.getColumnIndex(Langs.CODE)), cursor.getString(cursor.getColumnIndex(Langs.NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return langModel;
    }

    /**
     * Метод удаления объекта из таблицы избранное
     *
     * @param dictDTO объект который необходимо удалить
     */
    @Override
    public void removeHistoryItem(DictDTO dictDTO) {
        int deleted;
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        deleted = sqLiteDatabase.delete(DB_TABLE_HISTORY, Translate.ID + " =? ", new String[]{dictDTO.getId()});
        if (deleted != 0) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
    }

    /**
     * Метод удаления объекта из таблицы истории
     *
     * @param dictDTO объект который необходимо удалить
     */
    @Override
    public void removeFavoriteItem(DictDTO dictDTO) {
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        int i = sqLiteDatabase.delete(DB_TABLE_FAVORITES, Favorite.ID + "=?", new String[]{dictDTO.getFavorite()});
        if (i != 0) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
    }

    /**
     * Метод получения списка переведенных слов из таблицы история
     * @return список всех слов из таблицы история в формате json
     */
    @Override
    public List<String> getHistoryListTranslate() {
        ArrayList<String> dictDTOs = new ArrayList<>();
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_HISTORY, null, null, null, null, null, Translate.DATE + " DESC");
        if (cursor.moveToFirst()) {
            do {
                String json = cursor.getString(cursor.getColumnIndex(Translate.JSON));
                dictDTOs.add(json);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dictDTOs;
    }
    /**
     * Метод получения списка переведенных слов из таблицы избранное
     * @return список всех слов из таблицы избранное в формате json
     */
    @Override
    public List<String> getFavoriteListTranslate() {
        ArrayList<String> dictDTOs = new ArrayList<>(getCountForTable(DB_TABLE_HISTORY));
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_FAVORITES, null, null, null, null, null, Favorite.DATE + " DESC");
        if (cursor.moveToFirst()) {
            do {
                String json = cursor.getString(cursor.getColumnIndex(Favorite.JSON));
                dictDTOs.add(json);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dictDTOs;
    }

    /**
     * Обновляет поле дата переведенного слова в таблице история
     * @param id идентификатор слова в таблице история
     */
    @Override
    public void updateHistoryDate(String id) {
        long update;
        ContentValues contentValues = new ContentValues();
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        contentValues.put(Translate.DATE, System.currentTimeMillis());
        update = sqLiteDatabase.update(DB_TABLE_HISTORY, contentValues, "id=?", new String[]{id});
        if (update != 0) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
    }

    /**
     * Метод получения идентификатора из таблицы история по целевому объекту
     * @param dictDTO цель поиска
     * @return идентиыикатор
     */
    @Override
    public String getHistoryId(DictDTO dictDTO) {
        String historyId = "-1";
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_HISTORY,
                new String[]{Translate.ID},
                Translate.TARGET + " =? AND " + Translate.LANGS + " =? ",
                new String[]{dictDTO.getTarget(), dictDTO.getLangs()},
                null, null, null);
        if (cursor.moveToFirst()) {
            historyId = cursor.getString(cursor.getColumnIndex(Translate.ID));
        }
        cursor.close();
        return historyId;
    }
    /**
     * Метод получения идентификатора из таблицы избранное по целевому объекту
     * @param dictDTO цель поиска
     * @return идентиыикатор
     */
    @Override
    public String getFavoriteId(DictDTO dictDTO) {
        String favoriteId = "-1";
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_FAVORITES,
                new String[]{Favorite.ID},
                Favorite.TARGET + " =? AND " + Favorite.LANGS + " =?",
                new String[]{dictDTO.getTarget(), dictDTO.getLangs()},
                null, null, null);
        if (cursor.moveToFirst()) {
            favoriteId = cursor.getString(cursor.getColumnIndex(Favorite.ID));
        }
        cursor.close();
        return favoriteId;
    }

    /**
     * Метод получения количества строк
     * @param tableName название таблицы для счета
     * @return количество строк
     */
    private int getCountForTable(String tableName) {
        int count = 0;
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(tableName, new String[]{"COUNT(*)"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

}

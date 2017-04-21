package test.ya.translater.wgjuh.yaapitmvptest.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 02.04.2017.
 */

public class DbBackEnd implements Contractor, IDbBackEnd {
    private final DbOpenHelper mDbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;


    // TODO: 10.04.2017 убрать один из конструкторов
    public DbBackEnd(Context context) {
        // TODO: 02.04.2017 в конструктор добавить проверку на наличие бд
        mDbOpenHelper = new DbOpenHelper(context);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public DbBackEnd(DbOpenHelper dbOpenHelper) {
        // TODO: 02.04.2017 в конструктор добавить проверку на наличие бд
        mDbOpenHelper = dbOpenHelper;
    }

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
        sqLiteDatabase.close();
        return inserted;
    }

    @Override
    public long insertFavoriteItem(DictDTO dictDTO) {
        Long inserted;
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Translate.TARGET, dictDTO.getTarget());
        contentValues.put(Translate.LANGS, dictDTO.getLangs());
        contentValues.put(Translate.JSON, new Gson().toJson(dictDTO));
        contentValues.put(Translate.DATE, System.currentTimeMillis());
        inserted = sqLiteDatabase.insert(DB_TABLE_FAVORITES, null, contentValues);
        if (inserted != -1) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
        Log.d(TAG, "insertFavoriteItem: " + inserted);
        sqLiteDatabase.close();
        return inserted;
    }

    @Override
    public void setHistoryItemFavorite(DictDTO dictDTO, long favoriteId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Translate.FAVORITE, favoriteId);
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        if (sqLiteDatabase.update(DB_TABLE_HISTORY, contentValues, "id=?", new String[]{dictDTO.getId()}) != -1) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }

    @Override
    public String getFavoriteTranslate(String targetText, String translateDirection) {
        String json = null;
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor c = sqLiteDatabase.query(
                DB_TABLE_FAVORITES, new String[]{Favorite.JSON},  // => SELECT page_id FROM pages
                Favorite.TARGET + "=? AND " + Favorite.LANGS + " =?", new String[]{targetText, translateDirection},  // => WHERE page_url='url'
                null, null, null);
        if(c.moveToFirst()){
            json = c.getString(c.getColumnIndex(Favorite.JSON));
        }
        return json;
    }

    @Override
    public String getHistoryTranslate(String target, String langs) {
        String json = null;
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor c = sqLiteDatabase.query(
                DB_TABLE_HISTORY, new String[]{Translate.JSON},  // => SELECT page_id FROM pages
                Translate.TARGET + "=? AND " + Translate.LANGS + " =?", new String[]{target, langs},  // => WHERE page_url='url'
                null, null, null);
        if(c.moveToFirst()){
            json = c.getString(c.getColumnIndex(Favorite.JSON));
        }
        return json;
    }

    @Override
    public String getHistoryTranslate(long id) {
        String json = null;
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        Cursor c = sqLiteDatabase.query(
                DB_TABLE_HISTORY,
                new String[]{Translate.JSON},
                Translate.ID + " =?",
                new String[]{"" + id},
                null, null, null);
        if(c.moveToFirst()){
            json = c.getString(c.getColumnIndex(Favorite.JSON));
        }
        return json;
    }

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
            sqLiteDatabase.close();
    }

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
        sqLiteDatabase.close();
        return langModel;
    }

    @Override
    public int removeHistoryItem(DictDTO dictDTO) {
        int deleted;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Translate.FAVORITE, "-1");
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        deleted = sqLiteDatabase.delete(DB_TABLE_HISTORY, Translate.ID + " =? ", new String[]{dictDTO.getId()});
        if (deleted != 0) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        return deleted;
    }

    @Override
    public void removeFavoriteItemAndUpdateHistory(DictDTO dictDTO) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Translate.FAVORITE, "-1");
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        int i = sqLiteDatabase.delete(DB_TABLE_FAVORITES, Favorite.ID + "=?", new String[]{dictDTO.getFavorite()});
        int j = sqLiteDatabase.update(DB_TABLE_HISTORY, contentValues, Translate.FAVORITE + "=?", new String[]{dictDTO.getFavorite()});
        if (i != 0 | j  != 0) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }

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
        sqLiteDatabase.close();
        return dictDTOs;
    }

    @Override
    public List<String> getFavoriteListTranslate() {
        ArrayList<String> dictDTOs = new ArrayList<>(getCountForTable(DB_TABLE_HISTORY));
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_FAVORITES, null, null, null, null, null, Translate.DATE + " DESC");
        if (cursor.moveToFirst()) {
            do {
                String json = cursor.getString(cursor.getColumnIndex(Favorite.JSON));
                dictDTOs.add(json);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return dictDTOs;
    }

    @Override
    public long updateHistoryDate(String id) {
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
        sqLiteDatabase.close();
        return update;
    }

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
        sqLiteDatabase.close();
        return historyId;
    }

    @Override
    public String getFavoriteId(DictDTO dictDTO) {
        String historyId = "-1";
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_FAVORITES,
                new String[]{Favorite.ID},
                Favorite.TARGET + " =? AND " + Favorite.LANGS + " =?",
                new String[]{dictDTO.getTarget(), dictDTO.getLangs()},
                null, null, null);
        if (cursor.moveToFirst()) {
            historyId = cursor.getString(cursor.getColumnIndex(Favorite.ID));
        }
        cursor.close();
        sqLiteDatabase.close();
        return historyId;
    }

    @Override
    public String getLangByCode(String code) {
        String lang = code;
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_LANGS, new String[]{Langs.NAME}, Langs.CODE + "=?", new String[]{code}, null, null, null);
        if (cursor.moveToFirst()) {
            lang = cursor.getString(cursor.getColumnIndex(Langs.NAME));
        }
        cursor.close();
        sqLiteDatabase.close();
        return lang;
    }

    private int getCountForTable(String tableName) {
        int count = 0;
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(tableName, new String[]{"COUNT(*)"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return count;
    }

}

package test.ya.translater.wgjuh.yaapitmvptest.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelPojo;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 02.04.2017.
 */

public class DbBackEnd implements Contractor {
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

    public long insertHistoryTranslate(DictDTO dictDTO) {
        Long inserted;
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Translate.TARGET, dictDTO.getTarget());
        contentValues.put(Translate.LANGS, dictDTO.getLangs());
        contentValues.put(Translate.JSON, new Gson().toJson(dictDTO));
        contentValues.put(Translate.DATE, System.currentTimeMillis());
        inserted = sqLiteDatabase.insertOrThrow(DB_TABLE_HISTORY, null, contentValues);
        if (inserted != -1)
            sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.d(TAG, "insertHistoryTranslate: " + inserted);
        return inserted;
    }

    public DictDTO getHistoryTranslate(String target, String langs) {
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        Log.d(TAG, "getHistoryTranslate");
        sqLiteDatabase.beginTransaction();
        Cursor c = sqLiteDatabase.query(
                DB_TABLE_HISTORY, new String[]{Translate.JSON},  // => SELECT page_id FROM pages
                Translate.TARGET + "=? AND " + Translate.LANGS + " =?", new String[]{target, langs},  // => WHERE page_url='url'
                null, null, null);
        Log.d(TAG, "getHistoryTranslate: cursor: " + c);
        if (c.moveToFirst()) {
            String json = c.getString(c.getColumnIndex(Translate.JSON));
            Log.d(TAG, "getHistoryTranslate: " + json);
            c.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return new Gson().fromJson(json, DictDTO.class);
        }
        sqLiteDatabase.endTransaction();
        return null;
    }

    public int insertNewValue(ContentValues contentValues) {
        return 0;
    }

    public void upateLangs(LangsDirsModelPojo langsDirsModelPojo) {
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete(DB_TABLE_LANGS, null, null);
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, String> stringStringMap
                : langsDirsModelPojo.getLangs().entrySet()) {
            contentValues.put(Langs.CODE, stringStringMap.getKey());
            contentValues.put(Langs.NAME, stringStringMap.getValue());
            sqLiteDatabase.insertOrThrow(DB_TABLE_LANGS, null, contentValues);
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public LangModel getStoredLangs() {

        LangModel langModel = new LangModel(getCountForTable(DB_TABLE_LANGS));
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        sqLiteDatabase.beginTransaction();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_LANGS,new String[]{Langs.CODE,Langs.NAME},null,null,null,null,Langs.NAME);
        if(cursor.moveToFirst()){
            do {
                langModel.code.add(cursor.getString(cursor.getColumnIndex(Langs.CODE)));
                langModel.lang.add(cursor.getString(cursor.getColumnIndex(Langs.NAME)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return langModel;
    }
    private int getCountForTable(String tableName){
        int count = 0;
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(tableName,new String[]{"COUNT(*)"},null,null,null,null,null);
        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}

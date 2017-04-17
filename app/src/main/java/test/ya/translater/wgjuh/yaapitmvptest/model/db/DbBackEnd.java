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
        Log.d(TAG, "insertHistoryTranslate: " + inserted);
        sqLiteDatabase.close();
        return inserted;
    }

    @Override
    public long insertFavoriteFromHistory(String id){
        long inserted;
        ContentValues contentValues = new ContentValues();
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_HISTORY,new String[]{Translate.TARGET,Translate.LANGS,Translate.JSON},"id =?",new String[]{id},null,null,null);
        if(cursor.moveToFirst()){
            contentValues.put(Favorite.TARGET,cursor.getString(cursor.getColumnIndex(Translate.TARGET)));
            contentValues.put(Favorite.LANGS,cursor.getString(cursor.getColumnIndex(Translate.LANGS)));
            contentValues.put(Favorite.JSON,cursor.getString(cursor.getColumnIndex(Translate.JSON)));
            contentValues.put(Favorite.DATE,System.currentTimeMillis());
        }
        cursor.close();
        inserted = sqLiteDatabase.insert(DB_TABLE_FAVORITES,null,contentValues);
        if( inserted != -1) {
            sqLiteDatabase.setTransactionSuccessful();
        }
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        return inserted;
    }
    @Override
    public void setHistoryItemFavorite(String historyId, long favoriteId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Translate.FAVORITE,favoriteId);
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        if( sqLiteDatabase.update(DB_TABLE_HISTORY,contentValues,"id=?",new String[]{historyId}) != -1) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }
    @Override
    public void removeHistoryItemFavorite(String favoriteId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Translate.FAVORITE,"-1");
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        if( sqLiteDatabase.delete(DB_TABLE_FAVORITES,"id=?",new String[]{favoriteId}) != 0 &&
                sqLiteDatabase.update(DB_TABLE_HISTORY,contentValues,Translate.FAVORITE+"=?",new String[]{favoriteId}) != 0) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }

    @Override
    public DictDTO getHistoryTranslate(String target, String langs) {
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        Log.d(TAG, "getHistoryTranslate");
        Cursor c = sqLiteDatabase.query(
                DB_TABLE_HISTORY, new String[]{Translate.ID,Translate.JSON,Translate.FAVORITE},  // => SELECT page_id FROM pages
                Translate.TARGET + "=? AND " + Translate.LANGS + " =?", new String[]{target, langs},  // => WHERE page_url='url'
                null, null, null);
        Log.d(TAG, "getHistoryTranslate: cursor: " + c);
        if (c.moveToFirst()) {
            String json = c.getString(c.getColumnIndex(Translate.JSON));
            Log.d(TAG, "getHistoryTranslate: " + json);
            DictDTO dictDTO = getDictDTO(new Gson(),c);
            c.close();
            sqLiteDatabase.close();
            return dictDTO;
        }
        sqLiteDatabase.close();
        return null;
    }
    @Override
    public DictDTO getHistoryTranslate(long id) {
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        Log.d(TAG, "getHistoryTranslate");
        Cursor c = sqLiteDatabase.query(
                DB_TABLE_HISTORY, new String[]{Translate.ID,Translate.JSON,Translate.FAVORITE}, Translate.ID + " =?", new String[]{""+id},  // => WHERE page_url='url'
                null, null, null);
        Log.d(TAG, "getHistoryTranslate: cursor: " + c);
        if (c.moveToFirst()) {
            String json = c.getString(c.getColumnIndex(Translate.JSON));
            Log.d(TAG, "getHistoryTranslate: " + json);
            DictDTO dictDTO = getDictDTO(new Gson(),c);
            c.close();
            sqLiteDatabase.close();
            return dictDTO;
        }
        sqLiteDatabase.close();
        return null;
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
        if(inserted != -1){
        sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }
    @Override
    public LangModel getStoredLangs() {
        LangModel langModel = new LangModel(getCountForTable(DB_TABLE_LANGS));
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_LANGS,new String[]{Langs.CODE,Langs.NAME},null,null,null,null,Langs.NAME);
        if(cursor.moveToFirst()){
            do {
                langModel.code.add(cursor.getString(cursor.getColumnIndex(Langs.CODE)));
                langModel.lang.add(cursor.getString(cursor.getColumnIndex(Langs.NAME)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
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
        sqLiteDatabase.close();
        return count;
    }

    public List<DictDTO> getHistoryListTranslate() {
        ArrayList<DictDTO> dictDTOs = new ArrayList<>(getCountForTable(DB_TABLE_HISTORY));
        DictDTO dictDTO;
        Gson gson = new Gson();
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_HISTORY,null,null,null,null,null,Translate.DATE + " DESC");
        if(cursor.moveToFirst()){
            do {
                dictDTO = getDictDTO(gson,cursor);
                dictDTOs.add(dictDTO);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return dictDTOs;
    }

    public List<DictDTO> getFavoriteListTranslate() {
        ArrayList<DictDTO> dictDTOs = new ArrayList<>(getCountForTable(DB_TABLE_HISTORY));
        DictDTO dictDTO;
        Gson gson = new Gson();
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_FAVORITES,null,null,null,null,null,Translate.DATE + " DESC");
        if(cursor.moveToFirst()){
            do {
                dictDTO = getDictDTO(gson, cursor);
                dictDTOs.add(dictDTO);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return dictDTOs;
    }

    public int getFavoriteId(int id){
        int favoriteId = -1;
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_HISTORY,new String[]{Translate.FAVORITE},Translate.FAVORITE+" IS NOT NULL AND " + Translate.ID + " =? ",new String[]{""+id},null,null,null);
        if(cursor.moveToFirst()){
            favoriteId = cursor.getInt(cursor.getColumnIndex(Translate.FAVORITE));
            cursor.close();
            return favoriteId;
        }
        cursor.close();
        sqLiteDatabase.close();
        return favoriteId;
    }

    private String getHistoryIdByFavoriteId(String id){
        String historyId = "-1";
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_HISTORY,new String[]{Translate.ID},Translate.FAVORITE+" IS NOT NULL AND " + Translate.FAVORITE + " =? ",new String[]{id},null,null,null);
        if(cursor.moveToFirst()){
            historyId = cursor.getString(cursor.getColumnIndex(Translate.ID));
            cursor.close();
            return historyId;
        }
        cursor.close();
        sqLiteDatabase.close();
        return historyId;
    }

    public long updateHistoryDate(String id) {
        long update;
        ContentValues contentValues = new ContentValues();
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        contentValues.put(Translate.DATE,System.currentTimeMillis());
        update = sqLiteDatabase.update(DB_TABLE_HISTORY,contentValues,"id=?",new String[]{id});
        if( update != 0) {
            sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        return update;
    }

    @NonNull
    private DictDTO getDictDTO(Gson gson, Cursor cursor) {
        DictDTO dictDTO;
        dictDTO = gson.fromJson(cursor.getString(cursor.getColumnIndex(Translate.JSON)), DictDTO.class);

        if(cursor.getColumnIndex(Translate.FAVORITE) != -1) {
            dictDTO.setId(cursor.getString(cursor.getColumnIndex(Translate.ID)));
            dictDTO.setFavorite(cursor.getString(cursor.getColumnIndex(Translate.FAVORITE)));
        }else{
            dictDTO.setId(getHistoryIdByFavoriteId(cursor.getString(cursor.getColumnIndex(Favorite.ID))));
            dictDTO.setFavorite(cursor.getString(cursor.getColumnIndex(Favorite.ID)));
        }
        return dictDTO;
    }

    public String getLangByCode(String code) {
        String lang = code;
        sqLiteDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE_LANGS,new String[]{Langs.NAME},Langs.CODE+"=?",new String[]{code},null,null,null);
        if(cursor.moveToFirst()){
            lang = cursor.getString(cursor.getColumnIndex(Langs.NAME));
        }
        cursor.close();
        sqLiteDatabase.close();
        return lang;
    }
}

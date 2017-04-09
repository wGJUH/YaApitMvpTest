package test.ya.translater.wgjuh.yaapitmvptest.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

/**
 * Created by wGJUH on 02.04.2017.
 */

public class DbBackEnd implements Contractor {
    private final DbOpenHelper mDbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DbBackEnd(Context context) {
        // TODO: 02.04.2017 в конструктор добавить проверку на наличие бд
        mDbOpenHelper = new DbOpenHelper(context);
    }

    private void opendatabase() throws SQLException {
        //Open the database
        // TODO: 02.04.2017  проверить нужно ли написать path в виде
        sqLiteDatabase = mDbOpenHelper.getWritableDatabase();

    }

    public long insertHistoryTranslate(DictDTO dictDTO){
        opendatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Translate.TARGET,dictDTO.getDef().get(0).getText());
        contentValues.put(Translate.LANGS,dictDTO.getLangs());
        contentValues.put(Translate.JSON,new Gson().toJson(dictDTO));
        return sqLiteDatabase.insert(DB_TABLE_HISTORY,null,contentValues);
    }

    public int insertNewValue(ContentValues contentValues) {
        return 0;
    }

}

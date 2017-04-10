package test.ya.translater.wgjuh.yaapitmvptest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.mock.MockContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbBackEnd;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbOpenHelper;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

import static org.mockito.Mockito.when;

/**
 * Created by wGJUH on 10.04.2017.
 */
@RunWith(RobolectricTestRunner.class)
public class DbBackendTest {

    private DbBackEnd dbBackEnd;
    private DbOpenHelper dbOpenHelper;
    private SQLiteDatabase database;
    @Before
    public void setUp(){
        dbOpenHelper = new DbOpenHelper(RuntimeEnvironment.application);
        dbBackEnd = new DbBackEnd(dbOpenHelper);
    }

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule(); // говорим мокито, что надо создавать моки с аннотацией @Mock
    @Test
    public void testInsertIntoDb()  {
    DictDTO dictDTO = new DictDTO();
        dictDTO.setTarget("test");
        dictDTO.setCommonTranslate("тест");
        dictDTO.setLangs("en-ru");
        dbBackEnd.insertHistoryTranslate(dictDTO);

        dictDTO = dbBackEnd.getHistoryTranslate("test","en-ru");
        Assert.assertEquals(dictDTO.getCommonTranslate(),"тест");
        Assert.assertEquals(dictDTO.getLangs(),"en-ru");
        Assert.assertEquals(dictDTO.getTarget(),"test");
    }
}
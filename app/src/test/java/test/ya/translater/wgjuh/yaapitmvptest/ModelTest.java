package test.ya.translater.wgjuh.yaapitmvptest;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbBackEnd;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbOpenHelper;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

/**
 * Created by wGJUH on 10.04.2017.
 */

public class ModelTest {

    @Mock
    Context context;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule(); // говорим мокито, что надо создавать моки с аннотацией @Mock
    @Test
    public void test1()  {
        //  create mock


        DbBackEnd test = new DbBackEnd(new MockContext());
        DictDTO dictDTO = test.getHistoryTranslate("test","en-ru");
        Assert.assertEquals(dictDTO.getCommonTranslate(),"тест");
    }


}

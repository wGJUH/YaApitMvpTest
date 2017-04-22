package test.ya.translater.wgjuh.yaapitmvptest;

import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import rx.Observable;
import rx.observers.TestSubscriber;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbBackEndImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbOpenHelper;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

/**
 * Created by wGJUH on 10.04.2017.
 */
@RunWith(RobolectricTestRunner.class)
public class DbBackendTest {

    private DbBackEndImpl dbBackEndImpl;
    private SQLiteDatabase database;
    private DictDTO dictDTO;

    @Before
    public void setUp() {
        DbOpenHelper dbOpenHelper = new DbOpenHelper(RuntimeEnvironment.application);
        dbBackEndImpl = new DbBackEndImpl(dbOpenHelper);
        MockitoAnnotations.initMocks(this);
        dictDTO = new DictDTO();
        dictDTO.setTarget("test");
        dictDTO.setCommonTranslate("тест");
        dictDTO.setLangs("en-ru");
    }

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule(); // говорим мокито, что надо создавать моки с аннотацией @Mock

    @Test
    public void insertHistoryTranslateTest() {

        dbBackEndImpl.insertHistoryTranslate(dictDTO);

        String json = dbBackEndImpl.getHistoryTranslate("test", "en-ru");



        DictDTO result = new Gson().fromJson(json,DictDTO.class);

        Assert.assertEquals(dictDTO,result);
/*//        Assert.assertEquals(dictDTO.getCommonTranslate(), "тест");
//        Assert.assertEquals(dictDTO.getLangs(), "en-ru");
//        Assert.assertEquals(dictDTO.getTarget(), "test");

        TestSubscriber testSubscriber = new TestSubscriber();
        Observable.create(subscriber -> {
            subscriber.onNext("");
            subscriber.onCompleted();
        }).subscribe(testSubscriber);

        testSubscriber.assertValue("");
        testSubscriber.assertCompleted();*/
    }

    @Test
    public void setHistoryItemFavoriteTest(){
        dbBackEndImpl.insertHistoryTranslate(dictDTO);
        String json = dbBackEndImpl.getHistoryTranslate("test", "en-ru");
        DictDTO result = new Gson().fromJson(json,DictDTO.class);
        Assert.assertEquals(dictDTO,result);
        dbBackEndImpl.setHistoryItemFavorite(dictDTO,10);

    }
}

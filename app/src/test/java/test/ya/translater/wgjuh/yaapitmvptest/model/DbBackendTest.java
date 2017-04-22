package test.ya.translater.wgjuh.yaapitmvptest.model;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbBackEndImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbOpenHelper;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;


@RunWith(RobolectricTestRunner.class)
public class DbBackendTest {
    private final int ARRAY_SIZE = 3;
    private DbBackEndImpl dbBackEndImpl;
    private DictDTO dictDTO;

    @Before
    public void setUp() {
        DbOpenHelper dbOpenHelper = new DbOpenHelper(RuntimeEnvironment.application);
        dbBackEndImpl = new DbBackEndImpl(dbOpenHelper);
        dictDTO = new DictDTO();
        dictDTO.setTarget("test");
        dictDTO.setCommonTranslate("тест");
        dictDTO.setLangs("en-ru");
    }

    @After
    public void teardown() {
        dbBackEndImpl = null;
        dictDTO = null;
    }

    @Test
    public void insertHistoryTranslateTest() {
        dbBackEndImpl.insertHistoryTranslate(dictDTO);
        String json = dbBackEndImpl.getHistoryTranslate("test", "en-ru");
        DictDTO result = new Gson().fromJson(json, DictDTO.class);
        Assert.assertEquals(dictDTO, result);
    }

    @Test
    public void insertHistoryTranslateUniqueTest() {
        dbBackEndImpl.insertHistoryTranslate(dictDTO);
        dbBackEndImpl.insertHistoryTranslate(dictDTO);
        Assert.assertEquals(1, dbBackEndImpl.getHistoryListTranslate().size());
    }

    @Test
    public void insertFavoriteUniqueTest() {
        dbBackEndImpl.insertFavoriteItem(dictDTO);
        dbBackEndImpl.insertFavoriteItem(dictDTO);
        Assert.assertEquals(1, dbBackEndImpl.getFavoriteListTranslate().size());
    }

    @Test
    public void getHistoryId() {
        dbBackEndImpl.insertHistoryTranslate(dictDTO);
        String position = dbBackEndImpl.getHistoryId(dictDTO);
        Assert.assertEquals("1", position);
    }

    @Test
    public void getHistoryIdEmpty() {
        String position = dbBackEndImpl.getHistoryId(dictDTO);
        Assert.assertEquals("-1", position);
    }

    @Test
    public void getFavoriteId() {
        dbBackEndImpl.insertFavoriteItem(dictDTO);
        String position = dbBackEndImpl.getFavoriteId(dictDTO);
        Assert.assertEquals("1", position);
    }

    @Test
    public void getFavoriteIdEmpty() {
        String position = dbBackEndImpl.getFavoriteId(dictDTO);
        Assert.assertEquals("-1", position);
    }

    @Test
    public void removeFavoriteItemTest() {
        dbBackEndImpl.insertFavoriteItem(dictDTO);
        dictDTO.setFavorite(dbBackEndImpl.getFavoriteId(dictDTO));
        Assert.assertEquals("1", dictDTO.getFavorite());
        dbBackEndImpl.removeFavoriteItem(dictDTO);
        Assert.assertEquals("-1", dbBackEndImpl.getFavoriteId(dictDTO));
    }

    @Test
    public void removeHistoryItemTest() {
        dbBackEndImpl.insertHistoryTranslate(dictDTO);
        dictDTO.setId(dbBackEndImpl.getHistoryId(dictDTO));
        Assert.assertEquals("1", dictDTO.getId());
        dbBackEndImpl.removeHistoryItem(dictDTO);
        Assert.assertEquals("-1", dbBackEndImpl.getHistoryId(dictDTO));
    }

    @Test
    public void getHistoryList() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            dbBackEndImpl.insertHistoryTranslate(dictDTO.setFavorite(Integer.toString(i)));
        }
        List<String> dicts = dbBackEndImpl.getHistoryListTranslate();

        Assert.assertEquals(ARRAY_SIZE, dicts.size());

        for (int i = 0; i < ARRAY_SIZE; i++) {
            Assert.assertEquals(dictDTO.setFavorite(Integer.toString(i)), getDictDto(dicts.get(i)));
        }
    }

    @Test
    public void getFavoriteList() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            dbBackEndImpl.insertFavoriteItem(dictDTO.setId(Integer.toString(i)));
        }
        List<String> dicts = dbBackEndImpl.getFavoriteListTranslate();

        Assert.assertEquals(ARRAY_SIZE, dicts.size());

        for (int i = 0; i < ARRAY_SIZE; i++) {
            Assert.assertEquals(dictDTO.setId(Integer.toString(i)), getDictDto(dicts.get(i)));
        }
    }

    @Test
    public void updateHistoryDate(){
        dictDTO.setTarget("test0");
        dbBackEndImpl.insertHistoryTranslate(dictDTO);
        dictDTO.setTarget("test1");
        dbBackEndImpl.insertHistoryTranslate(dictDTO);
        Assert.assertEquals(dictDTO,getDictDto(dbBackEndImpl.getHistoryListTranslate().get(0)));
        dictDTO.setTarget("test0");
        dbBackEndImpl.updateHistoryDate(dbBackEndImpl.getHistoryId(dictDTO));
        Assert.assertEquals(dictDTO,getDictDto(dbBackEndImpl.getHistoryListTranslate().get(0)));

    }

    private DictDTO getDictDto(String json) {
        return new Gson().fromJson(json, DictDTO.class);
    }
}

package test.ya.translater.wgjuh.yaapitmvptest.model;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import rx.Observable;
import test.ya.translater.wgjuh.yaapitmvptest.BuildConfig;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbBackEndImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexDictionaryApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
public class ModelTest {

    Model model;
    YandexDictionaryApiInterface yandexDictionaryApiInterface;
    YandexTranslateApiInterface yandexTranslateApiInterface;
    @Mock
    DbBackEndImpl dbBackEndImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        yandexDictionaryApiInterface = Mockito.mock(YandexDictionaryApiInterface.class);
        yandexTranslateApiInterface = Mockito.mock(YandexTranslateApiInterface.class);
        when(dbBackEndImpl.getStoredLangs()).thenReturn(new LangsDirsModelDTO());
        when(yandexTranslateApiInterface.getLangs(anyString(), anyString())).thenReturn(Observable.empty());
        model = new ModelImpl(EventBusImpl.getInstance(),
                yandexTranslateApiInterface,
                yandexDictionaryApiInterface,
                dbBackEndImpl,
                RuntimeEnvironment.application);

    }

    @After
    public void tearDown() {
        yandexDictionaryApiInterface = null;
        yandexTranslateApiInterface = null;
        model = null;
    }

    @Test
    public void initArraysTest() {
        ArrayList<String> arrys = new ArrayList();
        DictDTO dictDTO = new DictDTO();
        dictDTO.setTarget("initArraysTest");
        dictDTO.setLangs("en-ru");
        dictDTO.setId("test").setFavorite("test");
        arrys.add(new Gson().toJson(dictDTO));
        when(dbBackEndImpl.getStoredLangs()).thenReturn(new LangsDirsModelDTO());
        when(dbBackEndImpl.getHistoryListTranslate()).thenReturn(arrys);
        when(dbBackEndImpl.getFavoriteListTranslate()).thenReturn(arrys);
        when(dbBackEndImpl.getHistoryId(any(DictDTO.class))).thenReturn("test");
        when(dbBackEndImpl.getFavoriteId(any(DictDTO.class))).thenReturn("test");
        model.initArrays();
        ArrayList dictDTOs = (ArrayList) model.getHistoryDictDTOs();
        Assert.assertEquals(arrys.size(), dictDTOs.size());
        DictDTO result = (DictDTO) dictDTOs.get(0);
        Assert.assertEquals(dictDTO, result);
        Assert.assertEquals(dictDTO.getId(), result.getId());
        Assert.assertEquals(dictDTO.getFavorite(), result.getFavorite());
        dictDTOs = (ArrayList) model.getFavoriteDictDTOs();
        Assert.assertEquals(arrys.size(), dictDTOs.size());
        result = (DictDTO) dictDTOs.get(0);
        Assert.assertEquals(dictDTO, result);
        Assert.assertEquals(dictDTO.getId(), result.getId());
        Assert.assertEquals(dictDTO.getFavorite(), result.getFavorite());
    }
    @Test
    public void setFavoritesWhenHistoryExistFavoriteNotExistTest() {
        DictDTO target = new DictDTO();
        target.setTarget("insertHistoryDictDTOsTest0");
        target.setLangs("en-ru");
        target.setId("-1").setFavorite("-1");
        when(dbBackEndImpl.getHistoryId(any(DictDTO.class))).thenReturn("0");
        when(dbBackEndImpl.getFavoriteId(any(DictDTO.class))).thenReturn("-1");
        ArrayList dictDTOs = (ArrayList) model.getFavoriteDictDTOs();
        Assert.assertEquals(0, dictDTOs.size());
        model.setFavorites(target);
        dictDTOs = (ArrayList) model.getFavoriteDictDTOs();
        Assert.assertEquals(1, dictDTOs.size());
        DictDTO result = (DictDTO) dictDTOs.get(0);
        Assert.assertEquals("0",result.getId());
        Assert.assertEquals("0",result.getFavorite());
    }
    @Test
    public void setFavoritesWhenHistoryNotExistsFavoriteNotExistTest() {
        DictDTO target = new DictDTO();
        target.setTarget("insertHistoryDictDTOsTest0");
        target.setLangs("en-ru");
        target.setId("-1").setFavorite("-1");
        when(dbBackEndImpl.getHistoryId(any(DictDTO.class))).thenReturn("-1");
        when(dbBackEndImpl.getFavoriteId(any(DictDTO.class))).thenReturn("-1");
        ArrayList dictDTOs = (ArrayList) model.getFavoriteDictDTOs();
        Assert.assertEquals(0, dictDTOs.size());
        model.setFavorites(target);
        dictDTOs = (ArrayList) model.getFavoriteDictDTOs();
        Assert.assertEquals(1, dictDTOs.size());
        DictDTO result = (DictDTO) dictDTOs.get(0);
        Assert.assertEquals(target.getId(),result.getId());
        Assert.assertEquals("0",result.getFavorite());
    }
    @Test
    public void setFavoritesWhenHistoryNotExistsFavoriteExistTest() {
        DictDTO target = new DictDTO();
        target.setTarget("insertHistoryDictDTOsTest0");
        target.setLangs("en-ru");
        target.setId("-1").setFavorite("1");
        model.insertFavoriteDictDTOs(target);
        when(dbBackEndImpl.getHistoryId(any(DictDTO.class))).thenReturn("-1");
        when(dbBackEndImpl.getFavoriteId(any(DictDTO.class))).thenReturn("1");
        model.setFavorites(target);
        DictDTO result = model.getFavoriteDictDTOs().get(0);
        Assert.assertEquals(target.getId(),result.getId());
        Assert.assertEquals("-1",result.getFavorite());
    }
    @Test
    public void setFavoritesWhenHistoryExistFavoriteExistTest() {
        DictDTO target = new DictDTO();
        target.setTarget("insertHistoryDictDTOsTest0");
        target.setLangs("en-ru");
        target.setId("0").setFavorite("0");
        model.insertFavoriteDictDTOs(target);
        model.insertHistoryDictDTOs(target);
        when(dbBackEndImpl.getHistoryId(any(DictDTO.class))).thenReturn("0");
        when(dbBackEndImpl.getFavoriteId(any(DictDTO.class))).thenReturn("0");
        model.setFavorites(target);
        ArrayList dictDTOs  = (ArrayList) model.getFavoriteDictDTOs();
        Assert.assertEquals(1, dictDTOs.size());
        DictDTO result = (DictDTO) dictDTOs.get(0);
        Assert.assertEquals("0",result.getId());
        Assert.assertEquals("-1",result.getFavorite());
        dictDTOs  = (ArrayList) model.getHistoryDictDTOs();
        Assert.assertEquals(1, dictDTOs.size());
        result = (DictDTO) dictDTOs.get(0);
        Assert.assertEquals("0",result.getId());
        Assert.assertEquals("-1",result.getFavorite());
    }
    @Test
    public void getHistoryTranslatePositiveTest() throws Exception {
        DictDTO target = new DictDTO();
        target.setTarget("insertHistoryDictDTOsTest0");
        target.setLangs("en-ru");
        target.setId("0").setFavorite("0");
        model.insertHistoryDictDTOs(target);
        Assert.assertEquals(target,model.getHistoryTranslate(target.getTarget(),target.getLangs()));
    }
    @Test
    public void getHistoryTranslateNegativeTest() throws Exception {
        Assert.assertEquals(null,model.getHistoryTranslate("test","en-ru"));
    }
    @Test
    public void translateLangTest() throws Exception {
        Assert.assertEquals("ru",model.getTranslateLang());
        model.setTranslateLang("test");
        Assert.assertEquals("test",model.getTranslateLang());
    }
    @Test
    public void fromLangTest() throws Exception {
        Assert.assertEquals("ru",model.getFromLang());
        model.setFromLang("test");
        Assert.assertEquals("test",model.getFromLang());
    }
    @Test
    public void getTranslateLangPairTest(){
        Assert.assertEquals("ru-ru",model.getTranslateLangPair());
        model.setTranslateLang("test");
        model.setFromLang("test");
        Assert.assertEquals("test-test",model.getTranslateLangPair());
    }
}
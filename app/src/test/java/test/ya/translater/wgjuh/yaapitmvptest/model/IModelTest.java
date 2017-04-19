package test.ya.translater.wgjuh.yaapitmvptest.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.TestSubscriber;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.DbBackEnd;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexDictionaryApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by wGJUH on 18.04.2017.
 */
@RunWith(RobolectricTestRunner.class)
public class IModelTest {

    IModel iModel;
    YandexDictionaryApiInterface yandexDictionaryApiInterface;
    YandexTranslateApiInterface yandexTranslateApiInterface;
    @Mock
    DbBackEnd dbBackEnd;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        yandexDictionaryApiInterface = Mockito.mock(YandexDictionaryApiInterface.class);
        yandexTranslateApiInterface = Mockito.mock(YandexTranslateApiInterface.class);
        iModel = new ModelImpl(EventBusImpl.getInstance(),yandexTranslateApiInterface,yandexDictionaryApiInterface);
    }

    @After
    public void tearDown(){
        yandexDictionaryApiInterface = null;
        yandexTranslateApiInterface = null;
        iModel = null;
    }

    @Test
    public void getTranslateForLanguage() throws Exception {
        TranslateDTO translateDTO = new TranslateDTO();
        when(yandexTranslateApiInterface.translateForLanguage(anyString(), anyString(), anyString())).thenReturn(Observable.just(translateDTO));
        TestSubscriber<TranslateDTO> testSubscriber = new TestSubscriber<TranslateDTO>();
        iModel.getTranslateForLanguage("test", "en-ru")
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(testSubscriber);
        testSubscriber.assertValuesAndClear(translateDTO);
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
    }
    @Test
    public void getDicTionaryTranslateForLanguageTest() throws Exception{
        DictDTO dictDTO = new DictDTO();
        dictDTO.setTarget("test");
        dictDTO.setLangs("en-ru");
        when(yandexDictionaryApiInterface.translateForLanguage(anyString(), anyString(), anyString(), anyString())).thenReturn(Observable.just(dictDTO));
        TestSubscriber<DictDTO> testSubscriber = new TestSubscriber<DictDTO>();
        iModel.getDicTionaryTranslateForLanguage("test", "en-ru")
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(testSubscriber);
        testSubscriber.assertValuesAndClear(dictDTO);
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
    }

    @Test
    public void getHistoryListTranslateTest() throws Exception{
        TestSubscriber<DictDTO> testSubscriber = new TestSubscriber<>();
        List<DictDTO> dictDTOs = initDictoDtoList(10);
        iModel = new ModelImpl(dbBackEnd);
       // when(dbBackEnd.getHistoryListTranslate()).thenReturn(dictDTOs);
      //  iModel.getHistoryListTranslate().subscribe(testSubscriber);
        testSubscriber.assertReceivedOnNext(dictDTOs);
        testSubscriber.assertUnsubscribed();
    }
    @Test
    public void getHistoryListTranslateTestError() throws Exception{
        TestSubscriber<DictDTO> testSubscriber = new TestSubscriber<>();
        List<DictDTO> dictDTOs = initDictoDtoList(10);
        iModel = new ModelImpl(dbBackEnd);
       // when(dbBackEnd.getHistoryListTranslate()).thenReturn(dictDTOs);
      //  iModel.getHistoryListTranslate().subscribe(testSubscriber);
        testSubscriber.assertReceivedOnNext(dictDTOs);
        testSubscriber.assertUnsubscribed();
    }
    @Test
    public void getFavoriteListTranslateTest(){
        TestSubscriber<DictDTO> testSubscriber = new TestSubscriber<>();
        List<DictDTO> dictDTOs = initDictoDtoList(10);
        iModel = new ModelImpl(dbBackEnd);
       // when(dbBackEnd.getFavoriteListTranslate()).thenReturn(dictDTOs);
      //  iModel.getFavoriteListTranslate().subscribe(testSubscriber);
        testSubscriber.assertReceivedOnNext(dictDTOs);
        testSubscriber.assertUnsubscribed();
    }

    @Test
    public void getLangsTest(){
/*        TestSubscriber<LangModel> testSubscriber = new TestSubscriber<>();
        LangModel langModel = initLangModels(10);
        iModel = new ModelImpl(dbBackEnd);
        when(dbBackEnd.getStoredLangs()).thenReturn(langModel);
        iModel.getLangs().subscribe(testSubscriber);
        testSubscriber.assertValue(langModel);
        testSubscriber.assertUnsubscribed();*/
    }

    @Test
    public void updateLanguagesTest(){
       // LangModel langModel = initLangModels(10);
        //when(yandexTranslateApiInterface.getLangs(anyString(),anyString())).thenReturn(Observable.just(langModel));
    }

    private List<DictDTO> initDictoDtoList(int count){
        DictDTO dictDTO;
        List<DictDTO> dictDTOs = new ArrayList<>();
        for (int i = 0; i < count;i++){
            dictDTO = new DictDTO();
            dictDTO.setTarget("test");
            dictDTO.setLangs("en-ru");
            dictDTOs.add(dictDTO);
        }
        return dictDTOs;
    }
/*    private LangModel initLangModels(int count){
        LangModel langModel = new LangModel(count);
        for (int i = 0; i < count;i++){
            langModel = new LangModel(i);
            langModel.lang.add("English");
            langModel.code.add("en");
        }
        return langModel;
    }*/
}
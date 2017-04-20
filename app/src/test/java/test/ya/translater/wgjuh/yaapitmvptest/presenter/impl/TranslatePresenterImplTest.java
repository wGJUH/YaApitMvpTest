package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.observers.TestSubscriber;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.IEventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.Def;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.Translate;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.ITranslatePrsenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(RobolectricTestRunner.class)
@Implements(ShadowNetwork.class)
public class TranslatePresenterImplTest {


    private ITranslatePrsenter translatePresenter;

    TranslateView translateView;
    IModel iModel;
    IEventBus iEventBus;


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        translateView = Mockito.mock(TranslateView.class);

        iModel = Mockito.mock(IModel.class);

        iEventBus = EventBusImpl.getInstance();

        translatePresenter = new TranslatePresenterImpl(iModel,iEventBus);

        translatePresenter.onBindView(translateView);
    }

    @After
    public void tearDown() throws Exception {
        translatePresenter = null;

    }

    @Test
    public void startTranslateWhenDictionaryAndTranslate() throws Exception {
        TestSubscriber testSubscriber = new TestSubscriber();
        iEventBus.subscribe(testSubscriber);
        String langs = "en-ru";
        String target = "test";
        DictDTO dictDTO = new DictDTO();
        Def def = new Def();
        Translate translate = new Translate();
        List<Translate> translates = new ArrayList<>();
        List<Def> defs = new ArrayList<>();

        TranslateDTO translateDTO = new TranslateDTO();
        translateDTO.setCode(200);
        translateDTO.setLang(langs);
        translateDTO.setText(target);



        translate.setText("тест");
        translates.add(translate);
        def.setTranslate(translates);
        defs.add(def);

        dictDTO.setDef(defs);

        Mockito.when(iModel.getTranslateLangPair()).thenReturn("en-ru");

        Mockito.when(iModel.getDicTionaryTranslateForLanguage("test", "en-ru"))
                .thenReturn(Observable.just(dictDTO));

        Mockito.when(iModel.getTranslateForLanguage("test", "en-ru"))
                .thenReturn(Observable.just(translateDTO));

        //translatePresenter.startTranslate("test");



       // Mockito.verify(iModel,times(1)).saveToDBAndNotify(any(DictDTO.class));
        Mockito.verify(iModel,times(1)).getTranslateLangPair();
        Mockito.verify(iModel,times(1)).getHistoryTranslate(anyString(),anyString());
        Mockito.verify(iModel,times(1)).getDicTionaryTranslateForLanguage(anyString(),anyString());
        Mockito.verify(iModel,times(1)).getTranslateForLanguage(anyString(),anyString());
        Mockito.verify(translateView,times(1)).showProgressBar(true);
        testSubscriber.assertValue(new Event(Event.EventType.WORD_TRANSLATED,any(DictDTO.class)));
        //Mockito.verify(translateView,times(1)).showProgressBar(false);
    }
    @Test
    public void startTranslateWhenNoDictionaryAndCommonTranslate() throws TimeoutException {
        TranslateDTO translateDTO = new TranslateDTO();
        translateDTO.setCode(200);
        translateDTO.setLang("en-ru");
        translateDTO.setText("тест");

        DictDTO dictDTO = new DictDTO();
        dictDTO.setCommonTranslate("тест");
        dictDTO.setFavorite("-1");

        Mockito.when(iModel.getTranslateLangPair()).thenReturn("en-ru");

        Mockito.when(iModel.getDicTionaryTranslateForLanguage("test", "en-ru"))
                .thenReturn(Observable.just(new DictDTO()));

        Mockito.when(iModel.getTranslateForLanguage("test", "en-ru"))
                .thenReturn(Observable.just(translateDTO));

      //  translatePresenter.startTranslate("test");
        iEventBus.post(iEventBus.createEvent(Event.EventType.WORD_TRANSLATED,dictDTO));


      //  Mockito.verify(iModel,times(1)).saveToDBAndNotify(any(DictDTO.class));
        Mockito.verify(iModel,times(1)).getTranslateLangPair();
        Mockito.verify(iModel,times(1)).getHistoryTranslate(anyString(),anyString());
        Mockito.verify(iModel,times(1)).getDicTionaryTranslateForLanguage(anyString(),anyString());
        Mockito.verify(iModel,times(1)).getTranslateForLanguage(anyString(),anyString());
        Mockito.verify(translateView,times(1)).showProgressBar(true);
        Mockito.verify(translateView,times(1)).showProgressBar(false);
    }
    @Test
    public void startTranslateWhenDictionaryAndNoCommonTranslate() throws TimeoutException {
        TestSubscriber testSubscriber = new TestSubscriber();

        Mockito.when(iModel.getTranslateLangPair()).thenReturn("en-ru");

        Mockito.when(iModel.getDicTionaryTranslateForLanguage("test", "en-ru"))
                .thenReturn(Observable.just(new DictDTO()));

        Mockito.when(iModel.getTranslateForLanguage("test", "en-ru"))
                .thenReturn(Observable.error(new TimeoutException("Ошибка")));

        iModel.getDicTionaryTranslateForLanguage("test", "en-ru").subscribe(testSubscriber);

        testSubscriber.assertCompleted();

        iModel.getTranslateForLanguage("test", "en-ru").subscribe(testSubscriber);

        testSubscriber.assertError(TimeoutException.class);

      //  translatePresenter.startTranslate("test");

        Mockito.verify(translateView,times(1)).showError(anyString());
    }
    @Test
    public void startTranslateWhenNoDictionaryAndNoCommonTranslate() throws TimeoutException {
        Mockito.when(iModel.getTranslateLangPair()).thenReturn("en-ru");

        Mockito.when(iModel.getDicTionaryTranslateForLanguage("test", "en-ru"))
                .thenReturn(Observable.error(new TimeoutException("Ошибка")));

        Mockito.when(iModel.getTranslateForLanguage("test", "en-ru"))
                .thenReturn(Observable.error(new TimeoutException("Ошибка")));

      //  translatePresenter.startTranslate("test");

        Mockito.verify(translateView,times(1)).showError(anyString());
    }
    @Test
    public void setFavorite() throws Exception {
        translatePresenter.setFavorite(true);
        Mockito.verify(translateView,times(1)).setBtnFavoriteSelected(true);
        translatePresenter.setFavorite(false);
        Mockito.verify(translateView,times(1)).setBtnFavoriteSelected(false);

    }

    @Test
    public void addToFavorites() throws Exception {

        translatePresenter.addFavorite();
    }

    @Test
    public void saveOutState() throws Exception {

    }

    @Test
    public void restoreState() throws Exception {

    }

    @Test
    public void getDictionarySate() throws Exception {

    }

}
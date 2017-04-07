package test.ya.translater.wgjuh.yaapitmvptest.model;



import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.data.LangsDirsModelPojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.data.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiInterface;
import test.ya.translater.wgjuh.yaapitmvptest.model.network.YandexTranslateApiModule;


import rx.Observable;

/**
 * Created by wGJUH on 04.04.2017.
 */

public class LangsModelImpl implements Model {

    private final Observable.Transformer schedulersTransformer;
    private final YandexTranslateApiInterface yandexTranslateApiInterface = YandexTranslateApiModule.getYandexTranslateApiInterface();
    private Observable<TranslatePojo> c;
    public LangsModelImpl(){
        schedulersTransformer = o -> ((Observable)o).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
        ;

    }

    @Override
    public Observable<LangsDirsModelPojo> getLangsDirsForLanguage(String language) {
        return yandexTranslateApiInterface.getLangs(DATA.API_KEY,language)
                .compose(this.applySchedulers());
    }

    @Override
    public Observable<TranslatePojo> getTranslateForLanguage(String target, String language) {
        c = yandexTranslateApiInterface
                .translateForLanguage(DATA.API_KEY,target,language)
                .compose(this.applySchedulers());

        return yandexTranslateApiInterface
                .translateForLanguage(DATA.API_KEY,target,language)
                .compose(this.applySchedulers());
    }

    public Observable<TranslatePojo> getC() {
        return c;
    }


    @SuppressWarnings("unchecked")
    private <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }
}

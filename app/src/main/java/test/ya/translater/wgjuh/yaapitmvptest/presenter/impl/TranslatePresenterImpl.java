package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import test.ya.translater.wgjuh.yaapitmvptest.App;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.Model;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.Def;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefRecyclerItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefTranslateItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.Translate;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.TranslatePrsenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

public class TranslatePresenterImpl extends BasePresenter<TranslateView> implements TranslatePrsenter {

    private final Model model;
    private final EventBus eventBus;
    private final List<DefRecyclerItem> defRecyclerItems = new ArrayList<>();
    private Subscription subscription;

    public TranslatePresenterImpl(Model model, EventBus eventBus) {

        this.model = model;
        this.eventBus = eventBus;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        addSubscription(eventBus.subscribe(event -> {
            switch (event.eventType) {
                case BTN_CLEAR_CLICKED:
                    showLicenseUnderCommonTranslate(false);
                    clearTranslate();
                    setFavorite(false);
                    model.setLastTranslate(null);
                    model.setLastTranslateTarget(null);
                    break;
                case START_TRANSLATE:
                    if (!event.content[0].toString().equals("")) {
                        model.setLastTranslateTarget(event.content[0].toString());
                        initTranslateCache(model.getLastTranslateTarget(), model.getTranslateLangPair());
                        startTranslate();
                    }
                    break;
                case WORD_TRANSLATED:
                    restoreState((DictDTO) event.content[0]);
                    model.updateHistoryDate(((DictDTO) event.content[0]).getId());
                    break;
                case WORD_UPDATED:
                    restoreState((DictDTO) event.content[0]);
                    break;
                case UPDATE_FAVORITE:
                    if (model.getLastTranslate() != null && model.getLastTranslate().equals(event.content[0])) {
                        setFavorite(!((DictDTO) event.content[0]).getFavorite().equals("-1"));
                    }
                    break;
                default:
                    break;
            }
        }));
    }

    private void showLicenseUnderCommonTranslate(boolean show) {
        view.showLicenseUnderCommonTranslate(show);
    }

    private void initTranslateCache(String target, String langs) {
        model.initZipTranslate(target, langs);
    }

    @Override
    public void startTranslate() {
        view.hideError();
        view.showProgressBar(true);

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.isUnsubscribed();
        }

        String translateDirection = model.getTranslateLangPair();

        DictDTO historyTranslate = model.getHistoryTranslate(model.getLastTranslateTarget(), translateDirection);

        DictDTO favoriteTranslate = model.getFavoriteTranslate(model.getLastTranslateTarget(), translateDirection);

        if (historyTranslate != null) {
            model.updateHistoryDate(historyTranslate.getId());
            eventBus.post(eventBus.createEvent(Event.EventType.WORD_UPDATED, historyTranslate));
        } else if (favoriteTranslate != null) {
            model.saveToDB(favoriteTranslate)
                    .subscribe(dictDTO -> eventBus
                                    .post(eventBus
                                            .createEvent(Event.EventType.WORD_TRANSLATED, dictDTO)),
                            throwable -> Log.e(TAG, "startTranslate: " + throwable.getMessage()));
        } else {
            translateFromInternet();
        }
    }

    private void translateFromInternet() {
        subscription = model
                .getZipTranslate()
                .doOnSubscribe(() -> view.showProgressBar(true))
                .doOnTerminate(() -> view.showProgressBar(false)).flatMap(model::saveToDB)
                .subscribe(dictDTO -> eventBus.post(eventBus.createEvent(Event.EventType.WORD_TRANSLATED, dictDTO))
                        , throwable -> {
                            view.showError("Ошибка соединения.\n\nПроверьте подключение к\nИнтернету и повторите попытку.");
                            throwable.printStackTrace();
                        },
                        model::freeCachedOBservable);
        addSubscription(subscription);
    }

    @Override
    public void setFavorite(boolean isFavorite) {
        view.setBtnFavoriteSelected(isFavorite);

    }

    @Override
    public void updateTranslateView(String s) {
        view.showTranslate(s);
    }

    @Override
    public void updateRecylcerView(DictDTO dictDTO) {
        dictDTO.getDef()
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(this::getDefRecyclerItemObservable)
                .subscribe(this::insertItemInTaleOfAdapterListAndNotify,throwable -> {
                    Log.e(TAG, "updateRecylcerView: " + throwable.getMessage() );
                    if(throwable.getMessage().equals("emptyDef")){
                        showLicenseUnderCommonTranslate(true);
                    }
                });
        view.showProgressBar(false);
    }

    private Observable<DefRecyclerItem> getDefRecyclerItemObservable(Def def) {
        DefRecyclerItem defRecyclerItem = getDefRecyclerItem(def);
        if (def.getTranslate() != 0) {
            def.getTranslateObservable().subscribe(translate -> {
                DefTranslateItem defTranslateItem = getDefTranslateItem(translate);
                defRecyclerItem.getDefTranslateItems().add(defTranslateItem);
            });
        }
        return Observable.just(defRecyclerItem);
    }

    private DefTranslateItem getDefTranslateItem(Translate translate) {
        DefTranslateItem defTranslateItem = new DefTranslateItem();
        SetExamples(translate, defTranslateItem);
        SetMeans(translate, defTranslateItem);
        defTranslateItem.getTextAndSyn().add(translate.getText());
        SetSyns(translate, defTranslateItem);
        return defTranslateItem;
    }

    private DefRecyclerItem getDefRecyclerItem(Def def) {
        DefRecyclerItem defRecyclerItem = new DefRecyclerItem();
        defRecyclerItem.setText(def.getText());
        defRecyclerItem.setPos(def.getPos());
        return defRecyclerItem;
    }

    private void insertItemInTaleOfAdapterListAndNotify(DefRecyclerItem defRecyclerItem) {
        defRecyclerItems.add(defRecyclerItem);
        view.updateAdapterTale(0);
    }

    private void SetSyns(Translate translate, DefTranslateItem defTranslateItem) {
        if (translate.getSynonyme() != null && translate.getSynonyme().size() != 0) {
            translate.getSynonymeObservable().subscribe(syn -> defTranslateItem.getTextAndSyn().add(syn.getText()));
        }
    }

    private void SetMeans(Translate translate, DefTranslateItem defTranslateItem) {
        if (translate.getMean() != null && translate.getMean().size() != 0) {
            translate.getMeanObservable().subscribe(mean -> defTranslateItem.getMeans().add(mean.getText()));
        }
    }

    private void SetExamples(Translate translate, DefTranslateItem defTranslateItem) {
        if (translate.getExample() != null && translate.getExample().size() != 0) {
            translate.getExampleObservable().subscribe(example -> defTranslateItem.getExamples().add(example.toString()));
        }
    }

    @Override
    public void updateChecboxFavorite(boolean isFavorite) {
        view.setBtnFavoriteSelected(isFavorite);
    }

    @Override
    public void clearTranslate() {
        view.hideError();
        view.showTranslate("");
        view.setBtnFavoriteEnabled(false);
        view.clearAdapter(defRecyclerItems.size());
        model.freeCachedOBservable();
        defRecyclerItems.clear();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            view.showProgressBar(false);
            model.freeCachedOBservable();
        }
    }

    @Override
    public void addFavorite() {
        if (model.getLastTranslate() != null) {
            eventBus.post(eventBus.createEvent(Event.EventType.UPDATE_FAVORITE, model.getLastTranslate()));
        }
    }

    @Override
    public void restoreState() {
        if (model.getLastTranslate() != null) {
            view.setBtnFavoriteEnabled(true);
            updateChecboxFavorite(!model.getLastTranslate().getFavorite().equals("-1"));
            updateTranslateView(model.getLastTranslate().getCommonTranslate());
            updateRecylcerView(model.getLastTranslate());

        } else {
            view.setBtnFavoriteEnabled(false);
            translateFromInternet();
        }
    }

    @Override
    public void restoreState(DictDTO dictDTO) {
        model
                .saveToDB(dictDTO)
                .subscribe(model::setLastTranslate,
                        throwable -> Log.e(TAG, "restoreStateError: " + throwable.getMessage()),
                        this::restoreState);

    }

    @Override
    public List<DefRecyclerItem> getDictionarySate() {
        return defRecyclerItems;
    }

    @Override
    public void startRetry() {
        if (hasConnection(App.getAppContext())) {
            initTranslateCache(model.getLastTranslateTarget(), model.getTranslateLangPair());
            startTranslate();
        }
    }

    @Override
    public void deleteFavorite() {
        eventBus.post(eventBus.createEvent(Event.EventType.UPDATE_FAVORITE, model.getLastTranslate()));
        eventBus.post(eventBus.createEvent(Event.EventType.DELETE_FAVORITE));
    }


    private boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isConnected();
    }

}

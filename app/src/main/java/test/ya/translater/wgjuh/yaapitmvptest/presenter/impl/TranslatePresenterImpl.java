package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.IEventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.Def;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefRecyclerItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefTranslateItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.Translate;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.ITranslatePrsenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;


/**
 * Created by wGJUH on 07.04.2017.
 */

public class TranslatePresenterImpl extends BasePresenter<TranslateView> implements ITranslatePrsenter {

    private final IModel iModel;
    private final IEventBus eventBus;
    private DictDTO lastTranslate;
    private List<DefRecyclerItem> defRecyclerItems = new ArrayList<>();
    private Subscription subscription;

    public TranslatePresenterImpl(IModel iModel, IEventBus eventBus) {

        this.iModel = iModel;
        this.eventBus = eventBus;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        addSubscription(eventBus.subscribe(event -> {
            switch (event.eventType) {
                case BTN_CLEAR_CLICKED:
                    clearTranslate();
                    setFavorite(false);
                    lastTranslate = null;
                    break;
                case START_TRANSLATE:
                    if (!event.content[0].toString().equals("")) {
                        initTranslateCache(event.content[0].toString(),iModel.getTranslateLangPair());
                        startTranslate(event.content[0].toString());
                    }
                    break;
                case WORD_TRANSLATED:
                    restoreState((DictDTO) event.content[0]);
                    iModel.updateHistoryDate(((DictDTO) event.content[0]).getId());
                    break;
                case WORD_UPDATED:
                    restoreState((DictDTO) event.content[0]);
                    break;
                case UPDATE_FAVORITE:
                    if (lastTranslate != null && lastTranslate.equals(event.content[0])) {
                        setFavorite(!((DictDTO) event.content[0]).getFavorite().equals("-1"));
                    }
                    break;
                default:
                    break;
            }
        }));
    }

    private void initTranslateCache(String target, String langs){
        iModel.initZipTranslate(target, langs);
    }

    @Override
    public void startTranslate(String targetText) {
        view.showProgressBar(true);

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.isUnsubscribed();
        }

        String translateDirection = iModel.getTranslateLangPair();

        DictDTO historyTranslate = iModel.getHistoryTranslate(targetText, translateDirection);

        DictDTO favoriteTranslate = iModel.getFavoriteTranslate(targetText, translateDirection);

        if (historyTranslate != null) {
            iModel.updateHistoryDate(historyTranslate.getId());
            eventBus.post(eventBus.createEvent(Event.EventType.WORD_UPDATED, historyTranslate));
            return;
        } else if (favoriteTranslate != null) {
            iModel.saveToDBAndNotify(favoriteTranslate);
            return;
        }else {
            translateFromInternet();
        }
    }

    private void translateFromInternet() {
        subscription = iModel
                .getZipTranslate()
                .doOnSubscribe(() -> view.showProgressBar(true))
                .doOnTerminate(() -> view.showProgressBar(false))
                .doOnCompleted(() -> iModel.freeCachedOBservable())
                .subscribe(iModel::saveToDBAndNotify
                        ,throwable -> view.showError(throwable.getMessage()));

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
                .delay(150, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(this::getDefRecyclerItemObservable)
                .subscribe(this::insertItemInTaleOfAdapterListAndNotify);
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
        defRecyclerItem.setTs("[" + def.getTranscription() + "]");
        defRecyclerItem.setPos(def.getPos());
        return defRecyclerItem;
    }

    private void insertItemInTaleOfAdapterListAndNotify(DefRecyclerItem defRecyclerItem) {
        defRecyclerItems.add(defRecyclerItem);
        view.updateAdapterTale(0);
    }

    private void SetSyns(Translate translate, DefTranslateItem defTranslateItem) {
        if (translate.getSynonyme() != null && translate.getSynonyme().size() != 0) {
            translate.getSynonymeObservable().subscribe(syn -> {
                Log.d(TAG, "Synonyms: " + syn.getText());
                defTranslateItem.getTextAndSyn().add(syn.getText());
                Log.d(TAG, "Synonyms After add: " + TextUtils.join(", ", defTranslateItem.getTextAndSyn()));
            });
        }
    }

    private void SetMeans(Translate translate, DefTranslateItem defTranslateItem) {
        if (translate.getMean() != null && translate.getMean().size() != 0) {
            translate.getMeanObservable().subscribe(mean -> {
                Log.d(TAG, "Means: " + mean.getText());
                defTranslateItem.getMeans().add(mean.getText());
            });
        }
    }

    private void SetExamples(Translate translate, DefTranslateItem defTranslateItem) {
        if (translate.getExample() != null && translate.getExample().size() != 0) {
            translate.getExampleObservable().subscribe(example -> {
                Log.d(TAG, "Example: " + example.toString());
                defTranslateItem.getExamples().add(example.toString());
            });
        }
    }

    @Override
    public void updateChecboxFavorite(boolean isFavorite) {
        view.setBtnFavoriteSelected(isFavorite);
    }

    @Override
    public void clearTranslate() {
        view.showTranslate("");
        view.clearAdapter(defRecyclerItems.size());
        defRecyclerItems.clear();
    }

    @Override
    public void addToFavorites() {
        if (lastTranslate != null) {
            lastTranslate.setFavorite(Long.toString(iModel.setFavorites(lastTranslate)));
            if (lastTranslate.getFavorite().equals("-1")) {
                eventBus.post(eventBus.createEvent(Event.EventType.DELETE_FAVORITE));
            }
            eventBus.post(eventBus.createEvent(Event.EventType.UPDATE_FAVORITE, lastTranslate));
        }
    }

    @Override
    public void saveOutState(Bundle outState) {
        if (lastTranslate != null) {
            outState.putParcelable(DATA.OUT_STATE, lastTranslate);
        }
    }

    @Override
    public void setLastTranslate(DictDTO lastTranslate) {
        this.lastTranslate = lastTranslate;
    }

    @Override
    public void restoreState(DictDTO dictDTO) {
        setLastTranslate(dictDTO);
        updateChecboxFavorite(!lastTranslate.getFavorite().equals("-1"));
        updateTranslateView(dictDTO.getCommonTranslate());
        updateRecylcerView(dictDTO);
    }

    @Override
    public void restoreState() {
        translateFromInternet();
    }

    @Override
    public List<DefRecyclerItem> getDictionarySate() {
        return defRecyclerItems;
    }
}

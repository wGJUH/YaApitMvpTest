package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
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
                    startTranslate((String) event.content[0]);
                    break;
                case WORD_TRANSLATED:
                    // TODO: 09.04.2017  как тут абстрагироваться от конкретной реализации ?
                    restoreState((DictDTO) event.content[0]);
                    break;
                case WORD_UPDATED:
                    restoreState((DictDTO) event.content[0]);
                    break;
                case ADD_FAVORITE:

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

    @Override
    public void startTranslate(String targetText) {
        view.showProgressBar(true);

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.isUnsubscribed();
        }

        String translateDirection = iModel.getTranslateLangPair();

        DictDTO historyTranslate = iModel.getHistoryTranslate(targetText, translateDirection);

        if (historyTranslate != null) {
            iModel.updateHistoryDate(historyTranslate.getId());
            eventBus.post(eventBus.createEvent(Event.EventType.WORD_UPDATED, historyTranslate));
            return;
        }
        Observable<DictDTO> dictDTOObservable = getDictDTOObservable(targetText, translateDirection);

        Observable<TranslateDTO> translatePojoObservable = getTranslateDTOObservable(targetText, translateDirection);

        Observable zipObservable = getZipTranslates(targetText, dictDTOObservable, translatePojoObservable);

        subscription = getTranslateSubscribe(zipObservable);

        addSubscription(subscription);
    }

    private Subscription getTranslateSubscribe(Observable zipObservable) {
        return zipObservable
                .subscribe(new Observer<DictDTO>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(DictDTO dictDTO) {
                        iModel.saveToDBAndNotify(dictDTO);
                    }
                });
    }

    @NonNull
    private Observable<DictDTO> getZipTranslates(String targetText, Observable<DictDTO> dictDTOObservable, Observable<TranslateDTO> translatePojoObservable) {
        return Observable.zip(dictDTOObservable, translatePojoObservable, (dictDTO, translatePojo) -> {
            dictDTO.setCommonTranslate(translatePojo.getText());
            dictDTO.setTarget(targetText);
            dictDTO.setLangs(translatePojo.getLang());
            return dictDTO;
        });
    }

    @NonNull
    private Observable<TranslateDTO> getTranslateDTOObservable(String targetText, String translateDirection) {
        return iModel
                .getTranslateForLanguage(targetText, translateDirection)
                .doOnError(throwable ->
                        Log.e(TAG, "translatePojoObservable: Сервис недоступен или запрос неверен", throwable)
                );
    }

    @NonNull
    private Observable<DictDTO> getDictDTOObservable(String targetText, String translateDirection) {
        return iModel
                .getDicTionaryTranslateForLanguage(targetText, translateDirection)
                .onErrorReturn(throwable -> {
                    Log.e(TAG, "dictDTOObservable: Сервис недоступен или запрос неверен", throwable);
                    return new DictDTO();
                });
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
        dictDTO
                .getDef()
                .flatMap(this::getDefRecyclerItemObservable).subscribe(this::insertItemInTaleOfAdapterListAndNotify);
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
        view.updateAdapterTale(defRecyclerItems.size());
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
    public List<DefRecyclerItem> getDictionarySate() {
        return defRecyclerItems;
    }
}

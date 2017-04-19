package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.IHistoryFavoritePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite.HistoryFavoriteView;

/**
 * Created by wGJUH on 16.04.2017.
 */

public class HistoryPresenterImpl extends BasePresenter<HistoryFavoriteView> implements IHistoryFavoritePresenter {

    private final IModel iModel;
    private final EventBusImpl eventBusImpl;

    private ArrayList<DictDTO> dictDTOs = new ArrayList<>();


    public HistoryPresenterImpl(IModel iModel, EventBusImpl eventBusImpl) {
        this.iModel = iModel;
        this.eventBusImpl = eventBusImpl;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        subscribeToBusEvents();
    }

    @Override
    public List<DictDTO> getTranslateList() {
        return dictDTOs;
    }

    @Override
    public void deleteFavorite(DictDTO dictDTO) {
        dictDTO.setFavorite(Long.toString(iModel.setFavorites(dictDTO)));
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.UPDATE_FAVORITE,dictDTO));
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.DELETE_FAVORITE));
    }

    @Override
    public void insertItemInTaleOfAdapterListAndNotify(DictDTO dictDTO) {
        dictDTOs.add(dictDTO);
        view.updateAdapterTale(dictDTOs.size()-1);

    }

    @Override
    public void initTranslateList() {
        iModel.getHistoryListTranslate()
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribe(this::insertItemInTaleOfAdapterListAndNotify);
    }

    @Override
    public void addItem(DictDTO dictDTO) {
        int oldPosition = dictDTOs.indexOf(dictDTO);
        if(oldPosition != -1){
            replaceItemsInAdapterData(oldPosition,dictDTO);
        }else {
            insertItemInNoseOfAdapterDataAndNotify(dictDTO);
        }
    }

    @Override
    public void updateFavorite(DictDTO dictDTO) {
        int position = dictDTOs.indexOf(dictDTO);
        if(position != -1) {
            dictDTOs.set(position, dictDTO);
            view.updateAdapterItemOnPosition(position);
        }
    }

    @Override
    public void addFavorite(DictDTO dictDTO) {
        dictDTO.setFavorite(Long.toString(iModel.setFavorites(dictDTO)));
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.UPDATE_FAVORITE,dictDTO));
    }

    @Override
    public void subscribeToBusEvents() {
        addSubscription(eventBusImpl.subscribe(this::onEvent));
    }

    @Override
    public void replaceItemsInAdapterData(int oldPosition, DictDTO dictDTO) {
        dictDTOs.remove(oldPosition);
        dictDTOs.add(0, dictDTO);
        view.changeAdapterItemPosition(oldPosition,0);
    }

    @Override
    public void insertItemInNoseOfAdapterDataAndNotify(DictDTO dictDTO) {
        dictDTOs.add(0,dictDTO);
        view.updateAdapterNose();
    }

    @Override
    public void onEvent(Event event) {
        switch (event.eventType){
            case WORD_TRANSLATED:
                addItem((DictDTO)event.content[0]);
                break;
            case WORD_UPDATED:
                addItem((DictDTO)event.content[0]);
                break;
            case UPDATE_FAVORITE:
                updateFavorite((DictDTO)event.content[0]);
                break;
            default:
                break;
        }
    }

    @Override
    public int deleteItem(DictDTO dictDTO) {
        int removed = iModel.removeHistoryItem(dictDTO);
        int position = dictDTOs.indexOf(dictDTO);
        dictDTOs.remove(dictDTO);
        view.removeItemOnPosition(position);
        return removed;
    }

    @Override
    public void showTranslate(DictDTO dictDTO) {
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.BTN_CLEAR_CLICKED));
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.WORD_TRANSLATED,dictDTO));
    }

    @Override
    public void saveOutState(Bundle outState) {
        outState.putParcelableArrayList("HISTORY_PARCEL",dictDTOs);
    }

    @Override
    public void restoreArray(Bundle savedInstanceState) {
        dictDTOs = savedInstanceState.getParcelableArrayList("HISTORY_PARCEL");
    }
}

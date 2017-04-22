package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;


import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.Model;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.HistoryFavoritePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite.HistoryFavoriteView;

/**
 * Created by wGJUH on 16.04.2017.
 */

public class HistoryPresenterImpl extends BasePresenter<HistoryFavoriteView> implements HistoryFavoritePresenter {

    private final Model model;
    private final EventBusImpl eventBusImpl;


    public HistoryPresenterImpl(Model model, EventBusImpl eventBusImpl) {
        this.model = model;
        this.eventBusImpl = eventBusImpl;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        subscribeToBusEvents();
    }

    @Override
    public List<DictDTO> getTranslateList() {
        return model.getHistoryDictDTOs();
    }

    @Override
    public void deleteFavorite(DictDTO dictDTO) {
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.UPDATE_FAVORITE,dictDTO));
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.DELETE_FAVORITE));
    }


    @Override
    public void addItem(DictDTO dictDTO) {
            insertItemInNoseOfAdapterDataAndNotify(dictDTO);

    }

    @Override
    public void updateFavorite(DictDTO dictDTO) {
        int position = model.getHistoryDictDTOs().indexOf(dictDTO);
        if(position != -1) {
            model.updateHistoryDto(position, dictDTO);
            view.updateAdapterItemOnPosition(position);
        }
    }

    @Override
    public void addFavorite(DictDTO dictDTO) {
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.UPDATE_FAVORITE,dictDTO));
    }

    @Override
    public void subscribeToBusEvents() {
        addSubscription(eventBusImpl.subscribe(this::onEvent));
    }

    @Override
    public void insertItemInNoseOfAdapterDataAndNotify(DictDTO dictDTO) {
        int position = model.getHistoryDictDTOs().indexOf(dictDTO);
        if(position > -1){
            model.moveHistoryDictDto(position,dictDTO);
            view.changeAdapterItemPosition(position,0);
        }else {
            model.insertHistoryDictDTOs(dictDTO);
            view.updateAdapterNose();
            view.scrollToPosition(0);
        }
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

    public void onLongItemClick(DictDTO dictDTO){
        view.showDeleteDialog(dictDTO);
    }

    @Override
    public void deleteItem(DictDTO dictDTO) {
        int removed = model.removeHistoryItem(dictDTO);
        view.removeItemOnPosition(removed);
    }

    @Override
    public void showTranslate(DictDTO dictDTO) {
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.BTN_CLEAR_CLICKED));
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.WORD_TRANSLATED,dictDTO));
    }
}

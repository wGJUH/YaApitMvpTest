package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;

import java.util.List;

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
        return iModel.getHistoryDictDTOs();
    }

    @Override
    public void deleteFavorite(DictDTO dictDTO) {
        iModel.setFavorites(dictDTO);
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.UPDATE_FAVORITE,dictDTO));
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.DELETE_FAVORITE));
    }


    @Override
    public void addItem(DictDTO dictDTO) {
        int oldPosition = iModel.getHistoryDictDTOs().indexOf(dictDTO);
        if(oldPosition != -1){
            replaceItemsInAdapterData(oldPosition,dictDTO);
        }else {
            insertItemInNoseOfAdapterDataAndNotify(dictDTO);
        }
    }

    @Override
    public void updateFavorite(DictDTO dictDTO) {
        int position = iModel.getHistoryDictDTOs().indexOf(dictDTO);
        if(position != -1) {
            iModel.updateHistoryDto(position, dictDTO);
            view.updateAdapterItemOnPosition(position);
        }
    }

    @Override
    public void addFavorite(DictDTO dictDTO) {
        iModel.setFavorites(dictDTO);
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.UPDATE_FAVORITE,dictDTO));
    }

    @Override
    public void subscribeToBusEvents() {
        addSubscription(eventBusImpl.subscribe(this::onEvent));
    }

    @Override
    public void replaceItemsInAdapterData(int oldPosition, DictDTO dictDTO) {
        iModel.moveHistoryDictDto(oldPosition,dictDTO);
        view.changeAdapterItemPosition(oldPosition,0);
    }

    @Override
    public void insertItemInNoseOfAdapterDataAndNotify(DictDTO dictDTO) {
        iModel.insertHistoryDictDTOs(dictDTO);
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
        view.removeItemOnPosition(removed);
        return removed;
    }

    @Override
    public void showTranslate(DictDTO dictDTO) {
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.BTN_CLEAR_CLICKED));
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.WORD_TRANSLATED,dictDTO));
    }
}

package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;

import java.util.Iterator;
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

public class FavoritePresenterImpl extends BasePresenter<HistoryFavoriteView> implements IHistoryFavoritePresenter {

    private final IModel iModel;
    private final EventBusImpl eventBusImpl;

    public FavoritePresenterImpl(IModel iModel, EventBusImpl eventBusImpl) {
        this.iModel = iModel;
        this.eventBusImpl = eventBusImpl;
    }


    @Override
    public List<DictDTO> getTranslateList() {
        return iModel.getFavoriteDictDTOs();
    }


    @Override
    public void deleteFavorite(DictDTO dictDTO) {
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.UPDATE_FAVORITE, dictDTO));
    }

    @Override
    public void insertItemInNoseOfAdapterDataAndNotify(DictDTO dictDTO) {
         view.updateAdapterNose(iModel.getFavoriteDictDTOs().indexOf(dictDTO));
         view.scrollToPosition(0);
    }

    /**
     * Метод позволяющий помести новый объект избранного в начало списка
     *
     * @param dictDTO новая запись в избранном
     */
    @Override
    public void addItem(DictDTO dictDTO) {
        insertItemInNoseOfAdapterDataAndNotify(dictDTO);
    }

    @Override
    public void updateFavorite(DictDTO dictDTO) {
        int position = iModel.getFavoriteDictDTOs().indexOf(dictDTO);
        if (position != -1) {
            iModel.updateFavoriteDto(position, dictDTO);
            view.updateAdapterItemOnPosition(position);
        }
    }

    @Override
    public void addFavorite(DictDTO dictDTO) {
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.UPDATE_FAVORITE, dictDTO));
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        subscribeToBusEvents();
    }

    @Override
    public void subscribeToBusEvents() {
        addSubscription(eventBusImpl.subscribe(this::onEvent));
    }

    public void removeAllNotFavorite() {
        for (Iterator<DictDTO> it = iModel.getFavoriteDictDTOs().iterator(); it.hasNext(); ) {
            if (it.next().getFavorite().equals("-1")) {
                it.remove();
            }
        }
        view.updateAllData();
    }

    @Override
    public void onEvent(Event event) {
        switch (event.eventType) {
            case UPDATE_FAVORITE:
                DictDTO dictDTO = (DictDTO) event.content[0];
                int position = iModel.getFavoriteDictDTOs().indexOf(dictDTO);
                iModel.setFavorites(dictDTO);
                if (dictDTO.getFavorite().equals("-1")) {
                    updateFavorite(dictDTO);
                } else {
                    if (position == -1) {
                        addItem(dictDTO);
                    }else {
                        updateFavorite(dictDTO);
                    }
                }
                break;
            case DELETE_FAVORITE:
                removeAllNotFavorite();
                break;
            default:
                break;

        }
    }

    @Override
    public int deleteItem(DictDTO dictDTO) {
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.UPDATE_FAVORITE, dictDTO));
        return 0;
    }

    @Override
    public void showTranslate(DictDTO dictDTO) {
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.BTN_CLEAR_CLICKED));
        eventBusImpl.post(eventBusImpl.createEvent(Event.EventType.WORD_TRANSLATED,dictDTO));
    }

    @Override
    public void onLongItemClick(DictDTO dictDTO) {
        deleteItem(dictDTO);
        //view.showDeleteDialog(dictDTO);
    }
}

package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;

import java.util.ArrayList;
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
    private final List<DictDTO> dictDTOs = new ArrayList<>();


    public HistoryPresenterImpl(IModel iModel, EventBusImpl eventBusImpl) {
        this.iModel = iModel;
        this.eventBusImpl = eventBusImpl;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        initTranslateList();
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
    }

    @Override
    public void insertItemInTaleOfAdapterListAndNotify(DictDTO dictDTO) {
        dictDTOs.add(dictDTO);
        view.updateAdapterTale(dictDTOs.size()-1);

    }

    @Override
    public void initTranslateList() {
        iModel.getHistoryListTranslate().subscribe(this::insertItemInTaleOfAdapterListAndNotify);
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
        dictDTOs.set(position,dictDTO);
        view.updateAdapterItemOnPosition(position);

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
            case ADD_FAVORITE:
                break;
            case UPDATE_FAVORITE:
                updateFavorite((DictDTO)event.content[0]);
                break;
            default:
                break;
        }
    }
}

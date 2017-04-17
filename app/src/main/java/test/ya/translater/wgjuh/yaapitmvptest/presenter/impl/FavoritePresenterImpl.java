package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;

import java.util.ArrayList;
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
    private final List<DictDTO> dictDTOs = new ArrayList<>();

    public FavoritePresenterImpl(IModel iModel, EventBusImpl eventBusImpl){
        this.iModel = iModel;
        this.eventBusImpl = eventBusImpl;
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
    public void insertItemInNoseOfAdapterDataAndNotify(DictDTO dictDTO) {
        dictDTOs.add(0,dictDTO);
        view.updateAdapterNose();
        view.scrollToPosition(0);

    }

    @Override
    public void initTranslateList() {
        iModel.getFavoriteListTranslate().subscribe(this::insertItemInTaleOfAdapterListAndNotify);

    }

    /**
     * Метод позволяющий помести новый объект избранного в начало списка
     * @param dictDTO новая запись в избранном
     */
    @Override
    public void addItem(DictDTO dictDTO) {
        insertItemInNoseOfAdapterDataAndNotify(dictDTO);
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
    public void onBindView(View view) {
        super.onBindView(view);
        initTranslateList();
        subscribeToBusEvents();
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

    public void removeAllNotFavorite(){
        for (Iterator<DictDTO> it = dictDTOs.iterator(); it.hasNext();) {
            if (it.next().getFavorite().equals("-1")) {
                it.remove();
            }
        }
        view.updateAllData();
    }

    @Override
    public void onEvent(Event event) {
        switch (event.eventType){
            case ADD_FAVORITE:
                addItem((DictDTO)event.content[0]);
                break;
            case UPDATE_FAVORITE:
                DictDTO dictDTO = (DictDTO)event.content[0];
                if(dictDTO.getFavorite().equals("-1")){
                    updateFavorite(dictDTO);
                }else if(!dictDTOs.contains(dictDTO)){
                    addItem((DictDTO)event.content[0]);
                }
                break;
            case DELETE_FAVORITE:
                removeAllNotFavorite();
                break;
            default:
                break;

        }
    }
}

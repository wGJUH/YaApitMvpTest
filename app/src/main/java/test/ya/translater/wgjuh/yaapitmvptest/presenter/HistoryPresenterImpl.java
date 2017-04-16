package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import java.util.ArrayList;
import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite.IHistoryFavoriteFragment;

/**
 * Created by wGJUH on 16.04.2017.
 */

public class HistoryPresenterImpl extends BasePresenter<IHistoryFavoriteFragment> implements IHistoryFavoritePresenter {

    private final IModel iModel;
    private final EventBus eventBus;
    private final List<DictDTO> dictDTOs = new ArrayList<>();


    public HistoryPresenterImpl(IModel iModel, EventBus eventBus) {
        this.iModel = iModel;
        this.eventBus = eventBus;
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
        eventBus.getEventBus().onNext(eventBus.createEvent(Event.EventType.UPDATE_FAVORITE,dictDTO));
    }

    @Override
    public void insertItemInTaleOfAdapterListAndNotify(DictDTO dictDTO) {
        dictDTOs.add(dictDTO);
        view.getViewAdapter().notifyItemInserted(dictDTOs.size()-1);
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
        view.getViewAdapter().notifyItemChanged(position);
    }

    @Override
    public void addFavorite(DictDTO dictDTO) {
        dictDTO.setFavorite(Long.toString(iModel.setFavorites(dictDTO)));
        dictDTOs.set(dictDTOs.indexOf(dictDTO),dictDTO);
        view.getViewAdapter().notifyItemChanged(dictDTOs.indexOf(dictDTO));
        eventBus.getEventBus().onNext(eventBus.createEvent(Event.EventType.UPDATE_FAVORITE,dictDTO));
    }

    @Override
    public void subscribeToBusEvents() {
        addSubscription(eventBus.getEventBus().subscribe(this::onEvent));
    }

    @Override
    public void replaceItemsInAdapterData(int oldPosition, DictDTO dictDTO) {
        dictDTOs.remove(oldPosition);
        dictDTOs.add(0, dictDTO);
        view.getViewAdapter().notifyItemMoved(oldPosition,0);
        // TODO: 16.04.2017  баг с необновлением индексов в списке
        view.getViewAdapter().notifyDataSetChanged();
    }

    @Override
    public void insertItemInNoseOfAdapterDataAndNotify(DictDTO dictDTO) {
        dictDTOs.add(0,dictDTO);
        view.getViewAdapter().notifyItemInserted(0);
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

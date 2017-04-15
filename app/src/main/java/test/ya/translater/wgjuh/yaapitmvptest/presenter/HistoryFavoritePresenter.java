package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IModel;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite.HistoryFavoritesFragment;

/**
 * Created by wGJUH on 13.04.2017.
 */

public class HistoryFavoritePresenter extends BasePresenter<HistoryFavoritesFragment> {
    private final IModel iModel;
    private final EventBus eventBus;
    private final boolean isHistory;
    private final List<DictDTO> dictDTOs = new ArrayList<>();

    public void getHistoryListTranslate() {
        addSubscription(iModel.getHistoryListTranslate().subscribe(dictDTO -> {
            updateAdapterData(dictDTO);
            Log.d(DATA.TAG, "getHistoryListTranslate: " + dictDTO.getCommonTranslate() + " is Favorite: " + dictDTO.getFavorite());
        }));
    }

    public void getFavoriteListTranslate() {
        addSubscription(iModel.getFavoriteListTranslate().subscribe(dictDTO -> {
            updateAdapterData(dictDTO);
            Log.d(DATA.TAG, "getFavoriteListTranslate: " + dictDTO.getCommonTranslate() + " is Favorite: " + dictDTO.getFavorite());
        }));
    }


    public HistoryFavoritePresenter(IModel iModel, EventBus eventBus, boolean isHistory) {
        this.iModel = iModel;
        this.eventBus = eventBus;
        this.isHistory = isHistory;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        if (isHistory) {
            getHistoryListTranslate();
        } else {
            getFavoriteListTranslate();
        }
        subscribeToBusEvents();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(DATA.TAG, "onStop: " + getClass().getName());
    }

    void subscribeToBusEvents() {
        if (isHistory) {
            addSubscription(EventBus.getInstance().getEventBus().subscribe(event -> {
                switch (event.eventType) {
                    case WORD_TRANSLATED:
                        updateFirstItemInAdapterData((DictDTO) event.content[0]);
                        break;
                    case WORD_UPDATED:
                        updateLastTranslatedInRow((DictDTO) event.content[0]);
                        break;
                    case ADD_FAVORITE:
                        updateFavoriteBox((DictDTO) event.content[0]);
                        break;
                    default:
                        break;
                }
            }));
        } else {
            addSubscription(EventBus.getInstance().getEventBus().subscribe(event -> {
                switch (event.eventType) {
                    case ADD_FAVORITE:
                        updateItemInFavoriteAdapterData((DictDTO) event.content[0]);
                        break;
                    case UPDATE_FAVORITE:
                        view.getViewAdapter().removeAllNotFavorite();
                        break;
                    default:
                        break;
                }
            }));
        }
    }

    public void updateFirstItemInAdapterData(DictDTO dictDTO) {
        if (dictDTO != null) {
            this.dictDTOs.add(0, dictDTO);
            view.getViewAdapter().notifyItemInserted(dictDTOs.size() - 1);
            view.getViewAdapter().notifyItemRangeChanged(0, dictDTOs.size());
            view.getRecyclerView().invalidate();
        }
    }

    public void updateAdapterData(DictDTO dictDTO) {
        this.dictDTOs.add(dictDTO);
        view.getViewAdapter().notifyItemInserted(dictDTOs.size() - 1);
    }

    public void updateItemInFavoriteAdapterData(DictDTO dictDTO) {
        int position = dictDTOs.indexOf(dictDTO);
        if (position != -1) {
            updateFavoriteBox(dictDTO);
            //dictDTOs.remove(position);
            //view.getViewAdapter().notifyItemRemoved(position);
            //view.getViewAdapter().notifyItemRangeChanged(0, dictDTOs.size()-1);
            //view.getRecyclerView().invalidate();

        } else {
            updateFirstItemInAdapterData(dictDTO);

        }
    }

    public void updateLastTranslatedInRow(DictDTO dictDTO) {
        int oldPosition = dictDTOs.indexOf(dictDTO);
        dictDTOs.remove(dictDTO);
        dictDTOs.add(0, dictDTO);
        view.getViewAdapter().notifyItemMoved(oldPosition, 0);
        view.getRecyclerView().scrollToPosition(0);
    }

    public void updateFavoriteBox(DictDTO dictDTO) {
        int position = dictDTOs.indexOf(dictDTO);
        dictDTOs.set(position, dictDTO);
        view.getViewAdapter().notifyItemChanged(position);
        view.getRecyclerView().scrollToPosition(position);
    }

    public List<DictDTO> getDictDTOs() {
        return dictDTOs;
    }

    public void updateFavorites(DictDTO dictDTO) {
        dictDTO.setFavorite(Long.toString(iModel.setFavorites(dictDTO)));
        eventBus.getEventBus().onNext(eventBus.createEvent(Event.EventType.ADD_FAVORITE, dictDTO));

    }
}

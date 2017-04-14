package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import android.util.Log;

import test.ya.translater.wgjuh.yaapitmvptest.DATA;
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

    public void getHistoryListTranslate(){
       addSubscription( iModel.getHistoryListTranslate().subscribe(dictDTO -> {
            view.updateAdapterData(dictDTO);
            Log.d(DATA.TAG, "getHistoryListTranslate: " + dictDTO.getCommonTranslate() + " is Favorite: " + dictDTO.getFavorite());
        }));
    }

    public void getFavoriteListTranslate(){
        addSubscription( iModel.getFavoriteListTranslate().subscribe(dictDTO -> {
            view.updateAdapterData(dictDTO);
            Log.d(DATA.TAG, "getFavoriteListTranslate: " + dictDTO.getCommonTranslate() + " is Favorite: " + dictDTO.getFavorite());
        }));
    }

    // TODO: 13.04.2017 дописать метод обновления recyclerView
    public void updateRecyclerView(){
        view.updateRecyclerView();
    }
    
    public HistoryFavoritePresenter(IModel iModel, EventBus eventBus, boolean isHistory){
        this.iModel = iModel;
        this.eventBus = eventBus;
        this.isHistory = isHistory;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        if(isHistory) {
            getHistoryListTranslate();
        }else {
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
                        view.updateFirstItemInAdapterData((DictDTO) event.content[0]);
                        break;
                    case WORD_UPDATED:
                        view.updateLastTranslatedInRow((DictDTO) event.content[0]);
                        break;
                    case ADD_FAVORITE:
                        view.updateFavoriteBox((DictDTO) event.content[0]);
                        break;
                    default:
                        break;
                }
            }));
        }
    }
}
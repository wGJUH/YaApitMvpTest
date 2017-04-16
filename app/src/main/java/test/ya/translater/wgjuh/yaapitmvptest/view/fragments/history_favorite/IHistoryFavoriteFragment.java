package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite;

import android.support.v7.widget.RecyclerView;

import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

/**
 * Created by wGJUH on 12.04.2017.
 */

public interface IHistoryFavoriteFragment extends View {
    RecyclerView.Adapter getViewAdapter();
    RecyclerView getRecyclerView();
}

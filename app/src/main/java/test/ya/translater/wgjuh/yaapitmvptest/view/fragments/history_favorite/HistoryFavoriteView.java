package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite;

import android.support.v7.widget.RecyclerView;

import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

/**
 * Created by wGJUH on 12.04.2017.
 */

public interface HistoryFavoriteView extends View {


    void updateAdapterTale(int size);

    void updateAdapterItemOnPosition(int position);

    void changeAdapterItemPosition(int oldPosition, int newPosition);

    void updateAdapterNose();

    void scrollToPosition(int position);

    void updateAllData();

    void removeItemOnPosition(int position);
}

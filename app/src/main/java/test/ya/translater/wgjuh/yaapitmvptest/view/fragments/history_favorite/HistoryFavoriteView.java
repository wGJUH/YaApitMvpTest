package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

/**
 * Created by wGJUH on 12.04.2017.
 */

public interface HistoryFavoriteView extends View {

    void updateAdapterItemOnPosition(int position);

    void changeAdapterItemPosition(int oldPosition, int newPosition);

    void updateAdapterNose(int i);

    void scrollToPosition(int position);

    void updateAllData();

    void removeItemOnPosition(int position);

    void removeItemRange(int start, int stop);

    void updateAdapterNose();

    void showDeleteDialog(DictDTO dictDTO);
}

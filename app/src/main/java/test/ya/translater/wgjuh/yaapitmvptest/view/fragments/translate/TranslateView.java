package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate;

import android.support.v7.widget.RecyclerView;

import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

/**
 * Created by wGJUH on 06.04.2017.
 */

public interface TranslateView extends View {

    void showTranslate(String s);

    void showProgressBar(Boolean show);

    void setBtnFavoriteSelected(Boolean selected);

    void updateAdapterTale(int size);

    void clearAdapter(int oldSize);
}

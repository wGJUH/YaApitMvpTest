package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate;

import rx.functions.Action1;
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

    void hideError();

    void setBtnFavoriteEnabled(Boolean enabled);
}

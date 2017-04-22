package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate;

import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

public interface TranslateView extends View {

    void showTranslate(String s);

    void showProgressBar(Boolean show);

    void setBtnFavoriteSelected(Boolean selected);

    void updateAdapterTale(int size);

    void clearAdapter(int oldSize);

    void hideError();

    void setBtnFavoriteEnabled(Boolean enabled);

    void showLicenseUnderCommonTranslate(Boolean show);
}

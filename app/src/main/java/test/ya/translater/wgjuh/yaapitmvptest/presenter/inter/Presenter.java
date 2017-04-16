package test.ya.translater.wgjuh.yaapitmvptest.presenter.inter;


import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

/**
 * Created by wGJUH on 04.04.2017.
 */

public interface Presenter<T extends  View> {
    void onStop();
    void onBindView(T view);
    void onUnbindView();
}

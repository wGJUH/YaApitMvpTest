package test.ya.translater.wgjuh.yaapitmvptest.presenter;


import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;


public interface Presenter<T extends  View> {
    void onStop();
    void onBindView(T view);
    void onUnbindView();
}

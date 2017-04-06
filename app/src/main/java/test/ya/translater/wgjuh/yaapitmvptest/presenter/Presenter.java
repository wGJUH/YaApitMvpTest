package test.ya.translater.wgjuh.yaapitmvptest.presenter;


import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

/**
 * Created by wGJUH on 04.04.2017.
 */

public interface Presenter {
    void onStop();
    void onBindView(View view, String tag);
    void onUnbindView(String tag);
}

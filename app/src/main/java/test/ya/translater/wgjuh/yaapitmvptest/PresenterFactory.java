package test.ya.translater.wgjuh.yaapitmvptest;

import android.support.annotation.NonNull;

/**
 * Created by wGJUH on 06.04.2017.
 */
public interface PresenterFactory<T> {

    @NonNull
    T createPresenter();
}

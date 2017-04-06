package test.ya.translater.wgjuh.yaapitmvptest.view.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;

import test.ya.translater.wgjuh.yaapitmvptest.presenter.BasePresenter;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 04.04.2017.
 */

public abstract class BaseFragment extends Fragment {

    protected abstract BasePresenter getPresenter();

    @Override
    public void onStop() {
        super.onStop();
        if (getPresenter() != null) {
            getPresenter().onStop();
        }
    }
}

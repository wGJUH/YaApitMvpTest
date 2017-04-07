package test.ya.translater.wgjuh.yaapitmvptest.view.fragments;

import android.support.v4.app.Fragment;

import test.ya.translater.wgjuh.yaapitmvptest.presenter.BasePresenter;

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

package test.ya.translater.wgjuh.yaapitmvptest.view.fragments;

import android.support.v4.app.Fragment;

import test.ya.translater.wgjuh.yaapitmvptest.presenter.inter.Presenter;

/**
 * Created by wGJUH on 04.04.2017.
 */

public abstract class BaseFragment extends Fragment {

    protected abstract Presenter getPresenter();
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getPresenter() != null) {
            getPresenter().onStop();
        }
    }
}

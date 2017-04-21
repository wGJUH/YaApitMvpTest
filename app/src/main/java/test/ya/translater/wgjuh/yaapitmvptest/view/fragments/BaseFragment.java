package test.ya.translater.wgjuh.yaapitmvptest.view.fragments;

import android.support.v4.app.Fragment;

import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;

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

package test.ya.translater.wgjuh.yaapitmvptest.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.*;
import android.view.View;

import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;

/**
 * Created by wGJUH on 19.04.2017.
 */

public class ErrorFragment extends BaseFragment {
    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_error,container,false);
    }
}

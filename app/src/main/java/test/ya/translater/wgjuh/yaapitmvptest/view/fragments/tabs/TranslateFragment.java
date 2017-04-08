package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.BasePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.TranslateFragmentContainerImpl;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateListFragment;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 04.04.2017.
 */

public class TranslateFragment extends BaseFragment implements TransalteView {

    @BindView(R.id.input_translateblock)
    FrameLayout inputFrame;
    @BindView(R.id.list_translateblock)
    FrameLayout translateFrame;

    private TranslateFragmentContainerImpl translatePresenter;
    private FragmentManager fragmentManager;

    public TranslateFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.translate_page_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        translatePresenter = new TranslateFragmentContainerImpl();
        translatePresenter.onBindView(this);
        if (savedInstanceState == null)
            translatePresenter.addFragments(new InputTranslateFragment(), new TranslateListFragment());
    }


    @Override
    protected BasePresenter getPresenter() {
        return translatePresenter;
    }


    @Override
    public void showError() {

    }

    @Override
    public FragmentManager getTranslateFragmentManager() {
        return fragmentManager;
    }

    @Override
    public View getInputFrame() {
        return inputFrame;
    }

    @Override
    public View getTranslateFrame() {
        return translateFrame;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: " + getClass().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: " + getClass().getName());
        translatePresenter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + getClass().getName());
        LeakCanaryApp.getRefWatcher(this.getContext()).watch(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + getClass().getName());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + getClass().getName());
    }
}

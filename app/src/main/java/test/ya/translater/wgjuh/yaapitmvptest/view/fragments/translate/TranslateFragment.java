package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate;

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
import test.ya.translater.wgjuh.yaapitmvptest.PresenterCache;
import test.ya.translater.wgjuh.yaapitmvptest.PresenterFactory;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.BasePresenterForCompositeView;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.TranslateFragmentContainerImpl;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;

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

    public TranslateFragment newInstance() {
        return new TranslateFragment();
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
        translatePresenter.onBindView(this, getClass().getName());
        if (savedInstanceState == null)
            translatePresenter.addFragments(new InputTranslateFragment(), new TranslateListFragment());
        else
            translatePresenter.bindViewsToPresenter(((test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View) getTranslateFragmentManager().findFragmentByTag(InputTranslateFragment.class.getName())), ((test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View) getTranslateFragmentManager().findFragmentByTag(TranslateListFragment.class.getName())));
    }


    @Override
    protected BasePresenterForCompositeView getPresenter() {
        return null;
    }


    @Override
    public void showError() {

    }

    @Override
    public void setPresenter(Presenter presenter) {
        translatePresenter = (TranslateFragmentContainerImpl) presenter;
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
        translatePresenter.onUnbindView(getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + getClass().getName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(PresenterCache.getInstance().getPresenter(TranslateFragmentContainerImpl.class.getName(), presenterFactory));
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + getClass().getName());
    }

    // TODO: 06.04.2017 дописать presenter factory срочно !!
    private final PresenterFactory<TranslateFragmentContainerImpl> presenterFactory =
            TranslateFragmentContainerImpl::new;
}

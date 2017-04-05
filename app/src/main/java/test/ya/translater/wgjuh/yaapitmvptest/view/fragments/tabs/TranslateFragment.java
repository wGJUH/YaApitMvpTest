package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.BasePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.TranslateFragmentContainerImpl;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateFragment;

/**
 * Created by wGJUH on 04.04.2017.
 */

public class TranslateFragment extends BaseFragment implements TransalteFragment{

    @BindView(R.id.input_translateblock)
    FrameLayout inputFrame;
//    @BindView(R.id.list_translateblock)
            FrameLayout translateFrame;

    private TranslateFragmentContainerImpl translatePresenter = new TranslateFragmentContainerImpl(this, new InputTranslateFragment());
    private FragmentManager fragmentManager;

    public TranslateFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
    }

    public TranslateFragment newInstance(){
        return new TranslateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.translate_page_fragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        translatePresenter.addFragments();
    }



    @Override
    protected BasePresenter getPresenter() {
        return null;
    }


    @Override
    public void showError() {

    }

    @Override
    public FragmentManager getTranslateFragmentManager() {
        return fragmentManager;
    }

    @Override
    public FrameLayout getInputFrame() {
        return inputFrame;
    }

    @Override
    public FrameLayout getTranslateFrame() {
        return translateFrame;
    }
}

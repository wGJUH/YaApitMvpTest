package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.activity_tabs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.TranslateFragmentContainerImpl;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.activity_tabs.inter.TransalteView;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.fragment.InputFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.fragment.TranslateFragment;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 04.04.2017.
 */

public class InputTranslateFragmentContainer extends BaseFragment implements TransalteView {

    @BindView(R.id.input_translateblock)
    FrameLayout inputFrame;
    @BindView(R.id.list_translateblock)
    FrameLayout translateFrame;
    @BindView(R.id.from_language)
    TextView fromLanguageTextView;
    @BindView(R.id.to_language)
    TextView toLanguageTextView;
    @BindView(R.id.btn_change_langs)
    ImageButton imageButton;
    private TranslateFragmentContainerImpl translatePresenter;
    private FragmentManager fragmentManager;

    public InputTranslateFragmentContainer() {

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentManager = getActivity().getSupportFragmentManager();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.translate_page_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setFromLanguageTextView(String s){
        fromLanguageTextView.setText(s);
        toLanguageTextView.invalidate();
    }

    public void setToLanguageTextView(String s){
        toLanguageTextView.setText(s);
        toLanguageTextView.invalidate();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       translatePresenter = new TranslateFragmentContainerImpl(ModelImpl.getInstance(), EventBusImpl.getInstance());
        translatePresenter.onBindView(this);
        new InputFragment();
       if (savedInstanceState == null)
            translatePresenter.addFragments(new InputFragment(), new TranslateFragment());
        translatePresenter.updateToolbarLanguages(false);
        fromLanguageTextView.setOnClickListener(text -> translatePresenter.onChooseLanguage(Event.EventType.FROM_LANGUAGE));
        toLanguageTextView.setOnClickListener(text -> translatePresenter.onChooseLanguage(Event.EventType.TARGET_LANGUAGE));
        imageButton.setOnClickListener(btn -> translatePresenter.onChangeLanguages());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    protected Presenter getPresenter() {
        return translatePresenter;
    }


    @Override
    public void showError(String error) {

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

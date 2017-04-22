package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.activity_tabs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.TabActivity;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.TranslateFragmentContainerImpl;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.activity_tabs.TranslateContainerView;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.fragment.InputFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.fragment.TranslateFragment;

public class InputTranslateFragmentContainer extends BaseFragment implements TranslateContainerView {

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
        View view = inflater.inflate(R.layout.fragemnt_translate, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setFromLanguageTextView(String s){
        fromLanguageTextView.setText(s);
        toLanguageTextView.invalidate();

    }

    public void notifyActivityHistoryShown(){
        ((TabActivity)getActivity()).onShowHistoryWord();
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
       if (savedInstanceState == null) {
           translatePresenter.addFragments(new InputFragment(), new TranslateFragment());
       }else{
            translatePresenter.updateDataArrays();
       }
        translatePresenter.updateToolbarLanguages(false);
        fromLanguageTextView.setOnClickListener(text -> translatePresenter.onChooseLanguage(Event.EventType.FROM_LANGUAGE));
        toLanguageTextView.setOnClickListener(text -> translatePresenter.onChooseLanguage(Event.EventType.TARGET_LANGUAGE));
        imageButton.setOnClickListener(btn -> translatePresenter.onChangeLanguages());
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
    public void onDestroyView() {
        super.onDestroyView();
        translatePresenter = null;
    }
}

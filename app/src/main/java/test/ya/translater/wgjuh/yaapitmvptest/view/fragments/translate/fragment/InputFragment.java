package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.InputPresenterImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.IInputPresenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 04.04.2017.
 */

public class InputFragment extends BaseFragment implements InputView {
    @BindView(R.id.edittext_translate_word)
    EditText editText;
    @BindView(R.id.btn_clear_input)
    ImageButton imageButton;

    private IInputPresenter inputPresenter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputPresenter = new InputPresenterImpl(ModelImpl.getInstance(), EventBusImpl.getInstance());
        inputPresenter.onBindView(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: " + getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + getClass().getName());
         LeakCanaryApp.getRefWatcher(this.getContext()).watch(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + getClass().getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edittext_translate_block, container, false);
        ButterKnife.bind(this, view);
        imageButton.setOnClickListener(btn -> inputPresenter.clearInput());
        editText.setOnEditorActionListener((input, action, event) -> inputPresenter.onButtonTranslateClick());
        return view;
    }

    @Override
    public void clearText() {
        editText.setText("");
    }

    @Override
    public String getTargetText() {
        return editText.getText().toString();
    }


    @Override
    public void showError(String e) {
        editText.setText(e);
        Log.e(TAG, "Error: " + e);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected Presenter getPresenter() {
        return inputPresenter;
    }


}

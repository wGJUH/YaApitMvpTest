package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.InputPresenterImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.InputPresenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;


public class InputFragment extends BaseFragment implements InputView {
    @BindView(R.id.edittext_translate_word)
    EditText editText;
    @BindView(R.id.btn_clear_input)
    ImageButton imageButton;
    private InputPresenter inputPresenter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputPresenter = new InputPresenterImpl(EventBusImpl.getInstance());
        inputPresenter.onBindView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_input_edittext, container, false);
        ButterKnife.bind(this, view);
        imageButton.setOnClickListener(btn -> inputPresenter.clearInput());
        editText.setOnEditorActionListener((input, action, event) -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getApplicationWindowToken(),0);
            return inputPresenter.onButtonTranslateClick();
        });
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
    public void setText(String text) {
        editText.setText(text);
    }


    @Override
    public void showError(String e) {
        Log.e(TAG, "Error: " + e);
    }

    @Override
    protected Presenter getPresenter() {
        return inputPresenter;
    }
}

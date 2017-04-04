package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.support.design.widget.Snackbar;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.ActivityCallback;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.BasePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.InputPresenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;

/**
 * Created by wGJUH on 04.04.2017.
 */

public class InputTranslateFragment extends BaseFragment implements InputTranslateView {
    @BindView(R.id.edittext_translate_word)
    EditText editText;
    @BindView(R.id.btn_clear_input)
    ImageButton imageButton;

    private ActivityCallback activityCallback;
    private InputPresenter inputPresenter = new InputPresenter(this);




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            activityCallback = (ActivityCallback)context;
        }catch (ClassCastException e){

            Log.e(DATA.TAG," "+ e.getMessage());
            throw new ClassCastException(context.toString()
                    + " must implement activityCallback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edittext_translate_block,null,false);
        ButterKnife.bind(this,view);
        imageButton.setOnClickListener(btn -> inputPresenter.onButtonTranslateClick());
        return view;
    }

    @Override
    public void clearText() {
        editText.setText("");
    }

    @Override
    public void translateText() {

    }

    @Override
    public String getTargetText() {
        return editText.getText().toString();
    }


    @Override
    public void showError() {
        Log.e(DATA.TAG," showError");
    }

    @Override
    protected BasePresenter getPresenter() {
        return inputPresenter;
    }
}

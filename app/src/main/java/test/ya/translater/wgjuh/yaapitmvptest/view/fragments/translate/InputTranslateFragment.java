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

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.BasePresenterForCompositeView;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.TranslateFragmentContainerImpl;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 04.04.2017.
 */

public class InputTranslateFragment extends BaseFragment implements InputTranslateView {
    @BindView(R.id.edittext_translate_word)
    EditText editText;
    @BindView(R.id.btn_clear_input)
    ImageButton imageButton;

    private TranslateFragmentContainerImpl translateFragmentContainer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
        }catch (ClassCastException e){

            Log.e(TAG," "+ e.getMessage());
            throw new ClassCastException(context.toString()
                    + " must implement activityCallback");
        }
    }
/*    public void setPresenter(Presenter presenter){
        translateFragmentContainer = PresenterCache.getInstance().getPresenter(TranslateFragmentContainerImpl.class.getName(),);
    }*/
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: " + getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + getClass().getName());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + getClass().getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edittext_translate_block,container,false);
        ButterKnife.bind(this,view);
        imageButton.setOnClickListener(btn -> translateFragmentContainer.onButtonClearClick());
        editText.setOnEditorActionListener((input,action,event) -> translateFragmentContainer.onButtonTranslateClick());
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
    public void showError() {
        Log.e(TAG," showError");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        translateFragmentContainer.onUnbindView(getClass().getName());
    }

    @Override
    public void setPresenter(Presenter presenter) {
        translateFragmentContainer = (TranslateFragmentContainerImpl) presenter;
    }

    @Override
    protected BasePresenterForCompositeView getPresenter() {
        return translateFragmentContainer;
    }


}

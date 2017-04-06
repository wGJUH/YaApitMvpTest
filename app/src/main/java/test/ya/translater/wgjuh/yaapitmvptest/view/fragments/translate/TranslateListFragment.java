package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.BasePresenterForCompositeView;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.TranslateFragmentContainerImpl;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 06.04.2017.
 */

public class TranslateListFragment extends BaseFragment implements TranslateListView {
    @BindView(R.id.textview_common_translate)
        TextView translate;

    private TranslateFragmentContainerImpl translateFragmentContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_translate_block,container,false);
        ButterKnife.bind(this,view);
        // TODO: 06.04.2017 Необходимо создать Serializable для сохранения состояния фрагмента перевода 
        if(savedInstanceState != null)
            translate.setText(savedInstanceState.getString("TEST"));
        return view;
    }

    @Override
    protected BasePresenterForCompositeView getPresenter() {
        return translateFragmentContainer;
    }

    @Override
    public void showTranslate(String translate) {
        this.translate.setText(translate);
    }

    @Override
    public void showError() {

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("TEST",translate.getText().toString());
    }

    public void setPresenter(Presenter pre){
        translateFragmentContainer = (TranslateFragmentContainerImpl) pre;
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
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + getClass().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        translateFragmentContainer.onUnbindView(getClass().getName());
    }
}

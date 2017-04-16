package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.TranslatePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 06.04.2017.
 */

public class TranslateListFragment extends BaseFragment implements TranslateListView {
    @BindView(R.id.textview_common_translate)
    TextView translate;
    @BindView(R.id.recycler_translate)
    RecyclerView recyclerView;
    @BindView(R.id.btn_add_favorite)
    CheckBox btnFavorite;


    private TranslatePresenter translatePresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        translatePresenter = new TranslatePresenter(ModelImpl.getInstance(), EventBus.getInstance());
        translatePresenter.onBindView(this);
        if(savedInstanceState != null) {
            DictDTO dictDTO  = (DictDTO)savedInstanceState.getSerializable(DATA.OUT_STATE);
            if(dictDTO != null) {
                translatePresenter.restoreState(dictDTO);
            }
        }
        btnFavorite.setOnClickListener(btn -> translatePresenter.addToFavorites());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_translate_block,container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    protected Presenter getPresenter() {
        return translatePresenter;
    }

    @Override
    public void showTranslate(String translate) {
        this.translate.setText(translate);
    }

    @Override
    public void setBtnFavoriteSelected(Boolean selected) {
        btnFavorite.setChecked(selected);
    }

    @Override
    public void showError(String error) {

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        translatePresenter.saveOutState(outState);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.IEventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.BasePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.TranslateFragmentContainerImpl;
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

    private TranslatePresenter translatePresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        translatePresenter = new TranslatePresenter(ModelImpl.getInstance(), EventBus.getInstance());
        translatePresenter.onBindView(this);
    }

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
    protected BasePresenter getPresenter() {
        return translatePresenter;
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

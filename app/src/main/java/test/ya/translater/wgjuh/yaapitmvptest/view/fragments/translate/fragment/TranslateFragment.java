package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.ITranslatePrsenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.TranslatePresenterImpl;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.DictionaryTranslateRecyclerViewAdapter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

/**
 * Created by wGJUH on 06.04.2017.
 */

public class TranslateFragment extends BaseFragment implements TranslateView {
    @BindView(R.id.textview_common_translate)
    TextView translate;
    @BindView(R.id.recycler_translate)
    RecyclerView recyclerView;
    @BindView(R.id.btn_add_favorite)
    CheckBox btnFavorite;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.error_frame)
    RelativeLayout error_frame;
    @BindView(R.id.btn_retry)
    ImageButton btn_retry;

    View view;
    private DictionaryTranslateRecyclerViewAdapter viewAdapter;

    private ITranslatePrsenter translatePresenterImpl;
    private Snackbar snackbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        translatePresenterImpl = new TranslatePresenterImpl(ModelImpl.getInstance(), EventBusImpl.getInstance());
        viewAdapter = new DictionaryTranslateRecyclerViewAdapter(translatePresenterImpl.getDictionarySate());
        translatePresenterImpl.onBindView(this);
        if(savedInstanceState != null) {
            DictDTO dictDTO  = savedInstanceState.getParcelable(DATA.OUT_STATE);
            if(dictDTO != null) {
                translatePresenterImpl.restoreState(dictDTO);
            }else {
                translatePresenterImpl.restoreState();
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(viewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        btnFavorite.setOnClickListener(btn -> translatePresenterImpl.addToFavorites());
        btn_retry.setOnClickListener(btn_retry -> translatePresenterImpl.startRetry());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.list_translate_block,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    protected Presenter getPresenter() {
        return translatePresenterImpl;
    }

    @Override
    public void showTranslate(String translate) {
        this.translate.setText(translate);
    }

    @Override
    public void showProgressBar(Boolean show) {
       if(show) {
           progressBar.setVisibility(View.VISIBLE);
       }else {
           progressBar.hide();
       }
    }

    @Override
    public void setBtnFavoriteSelected(Boolean selected) {
        btnFavorite.setChecked(selected);
    }

    @Override
    public void updateAdapterTale(int size) {
        viewAdapter.notifyItemInserted(size);
        viewAdapter.notifyDataSetChanged();
        recyclerView.invalidate();
    }

    @Override
    public void clearAdapter(int oldSize) {
        viewAdapter.notifyDataSetChanged();
        viewAdapter.removeAllViews();
    }

    @Override
    public void stopAnimateButton() {
        Log.d(TAG, "stopAnimateButton: ");
        btn_retry.clearAnimation();
    }

    @Override
    public void startAnimateButton() {
        Log.d(TAG, "startAnimateButton: ");
        if(btn_retry.getAnimation() == null) {

            RotateAnimation ranim = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
            btn_retry.setAnimation(ranim);
            btn_retry.startAnimation(ranim);
        }
    }

    @Override
    public void hideError() {
        Log.d(TAG, "hideError: ");
        stopAnimateButton();
        error_frame.setVisibility(View.GONE);
    }

    @Override
    public void showError(String error) {
        error_frame.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        translatePresenterImpl.saveOutState(outState);
        super.onSaveInstanceState(outState);
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

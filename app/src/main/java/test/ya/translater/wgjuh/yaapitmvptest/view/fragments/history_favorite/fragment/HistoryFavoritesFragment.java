package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.FavoritePresenterImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.HistoryPresenterImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.inter.IHistoryFavoritePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.inter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.HistoryFavoriteRecyclerViewAdapter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite.inter.IHistoryFavoriteFragment;


public class HistoryFavoritesFragment extends BaseFragment implements IHistoryFavoriteFragment {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    private static final String IS_HISTORY = "is_history";
    private boolean isHistory;
    private IHistoryFavoritePresenter historyFavoritePresenter;
    private HistoryFavoriteRecyclerViewAdapter viewAdapter;


    public HistoryFavoritesFragment() {
    }

    /**
     * Метод для определения роли создаваемого фрагмента (история/избранное)
     *
     * @param isHistory true когда фрагмент должен отображать историю
     * @return HistoryFavoritesFragment
     */
    public static HistoryFavoritesFragment newInstance(boolean isHistory) {
        HistoryFavoritesFragment fragment = new HistoryFavoritesFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_HISTORY, isHistory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isHistory = getArguments().getBoolean(IS_HISTORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historyfavoriteitem_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(isHistory) {
            historyFavoritePresenter = new HistoryPresenterImpl(ModelImpl.getInstance(), EventBusImpl.getInstance());
        }else{
            historyFavoritePresenter = new FavoritePresenterImpl(ModelImpl.getInstance(), EventBusImpl.getInstance());
        }
        viewAdapter = new HistoryFavoriteRecyclerViewAdapter(historyFavoritePresenter.getTranslateList(),isHistory, historyFavoritePresenter);
        historyFavoritePresenter.onBindView(this);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(viewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public HistoryFavoriteRecyclerViewAdapter getViewAdapter() {
        return viewAdapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected Presenter getPresenter() {
        return historyFavoritePresenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(DATA.TAG, "onDestroyView: " + getClass().getName());
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LeakCanaryApp.getRefWatcher(this.getContext()).watch(this);
    }



}
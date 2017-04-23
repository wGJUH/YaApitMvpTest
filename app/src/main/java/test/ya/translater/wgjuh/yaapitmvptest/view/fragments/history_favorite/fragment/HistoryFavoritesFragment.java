package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.FavoritePresenterImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.HistoryPresenterImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.HistoryFavoritePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.HistoryFavoriteRecyclerViewAdapter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite.HistoryFavoriteView;


public class HistoryFavoritesFragment extends BaseFragment implements HistoryFavoriteView {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    private static final String IS_HISTORY = "is_history";
    private boolean isHistory;
    private HistoryFavoritePresenter historyFavoritePresenter;
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
        View view = inflater.inflate(R.layout.fragment_historyfavorite_recycler_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isHistory) {
            historyFavoritePresenter = new HistoryPresenterImpl(ModelImpl.getInstance(), EventBusImpl.getInstance());
        } else {
            historyFavoritePresenter = new FavoritePresenterImpl(ModelImpl.getInstance(), EventBusImpl.getInstance());
        }
        viewAdapter = new HistoryFavoriteRecyclerViewAdapter(historyFavoritePresenter.getTranslateList(), historyFavoritePresenter);
        historyFavoritePresenter.onBindView(this);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(1);
    }

    @Override
    protected Presenter getPresenter() {
        return historyFavoritePresenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewAdapter = null;
    }

    @Override
    public void showError(int error) {

    }

    @Override
    public void scrollToPosition(int position) {
        recyclerView.scrollToPosition(position);
    }

    @Override
    public void updateAllData() {
        viewAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeItemOnPosition(int position) {
        viewAdapter.notifyItemRemoved(position);
    }

    @Override
    public void removeItemRange(int start, int stop) {
        viewAdapter.notifyItemRangeRemoved(start,stop);
    }

    @Override
    public void updateAdapterNose() {
        viewAdapter.notifyItemInserted(0);
    }

    @Override
    public void showDeleteDialog(DictDTO dictDTO) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Удалить",
                (dialogInterface, i) -> historyFavoritePresenter.deleteItem(dictDTO));
        alertDialog.show();
    }

    @Override
    public void updateAdapterItemOnPosition(int position) {
        viewAdapter.notifyItemChanged(position);
    }

    @Override
    public void changeAdapterItemPosition(int oldPosition, int newPosition) {
        viewAdapter.notifyItemMoved(oldPosition, 0);
    }

    @Override
    public void updateAdapterNose(int i) {
        viewAdapter.notifyItemInserted(i);
    }
}

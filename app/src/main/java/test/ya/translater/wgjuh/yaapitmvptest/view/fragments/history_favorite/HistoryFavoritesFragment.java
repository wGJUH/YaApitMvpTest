package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.BasePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.HistoryFavoritePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;


public class HistoryFavoritesFragment extends BaseFragment implements IHistoryFavoriteFragment {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    private static final String IS_HISTORY = "is_history";
    private boolean isHistory;
    private HistoryFavoritePresenter historyFavoritePresenter;
    private MyhistoryfavoriteitemRecyclerViewAdapter viewAdapter;
    private List<DictDTO> dictDTOs = new ArrayList<>();

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
        viewAdapter = new MyhistoryfavoriteitemRecyclerViewAdapter(dictDTOs);
        historyFavoritePresenter = new HistoryFavoritePresenter(ModelImpl.getInstance(), EventBus.getInstance());
        historyFavoritePresenter.onBindView(this);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(viewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
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
    protected BasePresenter getPresenter() {
        return historyFavoritePresenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(DATA.TAG, "onDestroyView: " + getClass().getName());
    }

    public void updateAdapterData(DictDTO dictDTO){
        this.dictDTOs.add(dictDTO);
        viewAdapter.notifyItemInserted(dictDTOs.size()-1);
    }
    public void updateFirstItemInAdapterData(DictDTO dictDTO){
        if(dictDTO != null) {
            this.dictDTOs.add(0, dictDTO);
            viewAdapter.notifyItemInserted(dictDTOs.size() - 1);
            viewAdapter.notifyItemRangeChanged(0, dictDTOs.size());
            recyclerView.invalidate();
        }
    }


    @Override
    public void updateRecyclerView() {

    }

    @Override
    public void showError(String error) {

    }

    public void updateLastTranslatedInRow(DictDTO dictDTO) {
        int oldPosition = dictDTOs.indexOf(dictDTO);
        dictDTOs.remove(dictDTO);
        dictDTOs.add(0,dictDTO);
        viewAdapter.notifyItemMoved(oldPosition,0);
        recyclerView.scrollToPosition(0);
    }

}

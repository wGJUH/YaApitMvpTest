package test.ya.translater.wgjuh.yaapitmvptest.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.SettingsPresenter;

public class SettingLangsFragment extends Fragment implements test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View {

    Event.EventType direction;
    RecyclerView recyclerView;
    Toolbar toolbar;

    public SettingLangsFragment() {
    }

    public static SettingLangsFragment newInstance(Event.EventType eventType) {
        SettingLangsFragment settingLangsFragment = new SettingLangsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA.DIRECTION, eventType);
        settingLangsFragment.setArguments(bundle);
        return settingLangsFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            direction = (Event.EventType)getArguments().getSerializable(DATA.DIRECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
            toolbar = (Toolbar) view.findViewById(R.id.fragment_toolbar);
        toolbar.setTitle(R.string.choose_language);
        recyclerView = (RecyclerView) view.findViewById(R.id.langs_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setRecyclerViewAdapter(MyItemRecyclerViewAdapter myItemRecyclerViewAdapter){
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SettingsPresenter settingsPresenter = new SettingsPresenter(ModelImpl.getInstance(), EventBus.getInstance(), direction);
        settingsPresenter.onBindView(this);
            }

    @Override
    public void showError() {

    }
}

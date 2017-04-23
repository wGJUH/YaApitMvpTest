package test.ya.translater.wgjuh.yaapitmvptest.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.SettingsPresenterImpl;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.LanguageSettingsRecyclerViewAdapter;

public class SettingLangsFragment extends BaseFragment implements test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View {

    private Event.EventType direction;
    @BindView(R.id.langs_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_toolbar)
    Toolbar toolbar;
    private SettingsPresenterImpl settingsPresenterImpl;
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

        View view = inflater.inflate(R.layout.fragment_languagesetting_recycler_list, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle(R.string.choose_language);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        setRecyclerDecorator(linearLayoutManager,dividerItemDecoration);

        return view;
    }

    private void setRecyclerDecorator(LinearLayoutManager linearLayoutManager, DividerItemDecoration dividerItemDecoration){
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setRecyclerViewAdapter(LanguageSettingsRecyclerViewAdapter languageSettingsRecyclerViewAdapter){
        recyclerView.setAdapter(languageSettingsRecyclerViewAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsPresenterImpl = new SettingsPresenterImpl(ModelImpl.getInstance(), EventBusImpl.getInstance(), direction);
        settingsPresenterImpl.onBindView(this);
            }

    @Override
    public void showError(int error) {

    }

    @Override
    protected Presenter getPresenter() {
        return settingsPresenterImpl;
    }
}

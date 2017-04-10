package test.ya.translater.wgjuh.yaapitmvptest.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;

public class SettingLangsFragment extends Fragment implements OnLanguageChossen{
    public SettingLangsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SettingLangsFragment newInstance(int columnCount) {
        SettingLangsFragment fragment = new SettingLangsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.fragment_toolbar);

        toolbar.setTitle(R.string.choose_language);

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
            recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
            ModelImpl.getInstance()
                    .getLangs()
                    .subscribe(langModel -> recyclerView.setAdapter(new MyItemRecyclerViewAdapter(langModel,this)));
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

    @Override
    public void onLanguageChoosen() {
        getFragmentManager().popBackStack();
    }
}

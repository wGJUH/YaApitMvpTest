package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.BasePresenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.FragmentAdapter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.SettingLangsFragment;

/**
 * Created by wGJUH on 11.04.2017.
 */

public class HistoryFavoriteFragmentContainer extends BaseFragment {
    @BindView(R.id.fragment_toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottom_tabLayout_app)
    TabLayout tabLayout;
    @BindView(R.id.history_favorites_viewpager)
    ViewPager viewPager;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_bookmark_container, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inits();
        toolbar.setTitle(getClass().getName());
    }

    private void inits() {
        FragmentAdapter fragmentPagerAdapter = new FragmentAdapter(getFragmentManager());
        fragmentPagerAdapter.addFragment(new SettingLangsFragment(), SettingLangsFragment.class.getName()+1);
        fragmentPagerAdapter.addFragment(new SettingLangsFragment(), SettingLangsFragment.class.getName()+2);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}

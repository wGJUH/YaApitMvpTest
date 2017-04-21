package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.activity_tabs.fragment;

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
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.FragmentAdapter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite.fragment.HistoryFavoritesFragment;

/**
 * Created by wGJUH on 11.04.2017.
 */

public class  HistoryFavoriteFragmentContainer extends BaseFragment {
    @BindView(R.id.fragment_toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottom_tabLayout_app)
    TabLayout tabLayout;
    @BindView(R.id.history_favorites_viewpager)
    ViewPager viewPager;

    @Override
    protected Presenter getPresenter() {
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
        fragmentPagerAdapter.addFragment(HistoryFavoritesFragment.newInstance(true), HistoryFavoritesFragment.class.getName()+1);
        fragmentPagerAdapter.addFragment(HistoryFavoritesFragment.newInstance(false), HistoryFavoritesFragment.class.getName()+2);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if(tabLayout.getTabCount() == 2) {
            tabLayout.getTabAt(0).setText(R.string.tab_history);
            tabLayout.getTabAt(1).setText(R.string.tab_favorite);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1) {
                    EventBusImpl.getInstance().post(EventBusImpl.getInstance().createEvent(Event.EventType.DELETE_FAVORITE));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}

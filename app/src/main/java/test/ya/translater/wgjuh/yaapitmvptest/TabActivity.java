package test.ya.translater.wgjuh.yaapitmvptest;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.FragmentAdapter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.activity_tabs.fragment.HistoryFavoriteFragmentContainer;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.activity_tabs.fragment.InputTranslateContainerFragmentContainer;

public class TabActivity extends AppCompatActivity implements ActivityCallback, TabLayout.OnTabSelectedListener {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.bottom_tabLayout_app)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        inits();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    private void inits() {
        FragmentAdapter fragmentPagerAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addFragment(new InputTranslateContainerFragmentContainer(), InputTranslateContainerFragmentContainer.class.getName());
        fragmentPagerAdapter.addFragment(new HistoryFavoriteFragmentContainer(), HistoryFavoriteFragmentContainer.class.getName());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.addOnTabSelectedListener(this);
        if(fragmentPagerAdapter.getCount() == 2) {
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_translate_ya);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_bookmark);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tabLayout.removeOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            tab.setIcon(R.drawable.ic_translate_ya);
        } else {
            tab.setIcon(R.drawable.ic_bookmark_ya);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            tab.setIcon(R.drawable.ic_translate);
        } else {
            tab.setIcon(R.drawable.ic_bookmark);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onShowHistoryWord() {
        viewPager.setCurrentItem(0);
    }
}

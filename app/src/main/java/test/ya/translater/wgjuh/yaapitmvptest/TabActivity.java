package test.ya.translater.wgjuh.yaapitmvptest;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.FragmentAdapter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.tabs.TranslateFragment;

public class TabActivity extends AppCompatActivity implements ActivityCallback {
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

    private void inits() {
        FragmentAdapter fragmentPagerAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addFragment(new TranslateFragment(),TranslateFragment.class.getName());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    public void onButtonTranslate() {

    }
}

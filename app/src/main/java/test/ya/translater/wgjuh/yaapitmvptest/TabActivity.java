package test.ya.translater.wgjuh.yaapitmvptest;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.FragmentAdapter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.tabs.TranslateFragment;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

public class TabActivity extends AppCompatActivity implements ActivityCallback {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.bottom_tabLayout_app)
    TabLayout tabLayout;
    RefWatcher refWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        inits();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + getClass().getName());
        if(getSupportFragmentManager().getFragments() != null)
            Log.d(TAG, "onStart: getsupportFM: " + getSupportFragmentManager().getFragments().size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + getClass().getName());
        if(getSupportFragmentManager().getFragments() != null)
        Log.d(TAG, "onResume: getsupportFM: " + getSupportFragmentManager().getFragments().size());

    }

    private void inits() {
        FragmentAdapter fragmentPagerAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addFragment(new TranslateFragment(),TranslateFragment.class.getName());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + getClass().getName());
        Log.d(TAG, "onDestroy: getsupportFM: " + getSupportFragmentManager().getFragments().size());
        refWatcher = LeakCanaryApp.getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + getClass().getName());
        Log.d(TAG, "onStop: getsupportFM: " + getSupportFragmentManager().getFragments().size());
    }
}

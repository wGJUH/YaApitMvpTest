package test.ya.translater.wgjuh.yaapitmvptest.presenter;


import android.util.Log;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;


/**
 * Created by wGJUH on 04.04.2017.
 */

public abstract  class BasePresenter<T extends View> implements Presenter {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    public T view;


    void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    @Override
    public void onUnbindView() {
        Log.d(TAG, "onUnbindView: " );
        view = null;
    }

    @Override
    public void onBindView(View view) {
        Log.d(TAG, "onBindView: "+ view.getClass().getName());
        this.view = (T)view;
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
        onUnbindView();
    }
}

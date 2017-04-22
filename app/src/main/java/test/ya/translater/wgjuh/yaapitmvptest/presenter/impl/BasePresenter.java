package test.ya.translater.wgjuh.yaapitmvptest.presenter.impl;


import android.util.Log;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

public abstract  class BasePresenter<T extends View> implements Presenter {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    T view;


    void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    @Override
    public void onUnbindView() {
        Log.d(TAG, "onUnbindView: " + view.getClass().getName());
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

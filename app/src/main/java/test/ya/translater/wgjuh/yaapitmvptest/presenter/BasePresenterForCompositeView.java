package test.ya.translater.wgjuh.yaapitmvptest.presenter;


import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import test.ya.translater.wgjuh.yaapitmvptest.model.Model;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;


/**
 * Created by wGJUH on 04.04.2017.
 */

public abstract class BasePresenterForCompositeView implements Presenter {

    private final SimpleArrayMap<String,View> views = new SimpleArrayMap<>();
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    final Model model = new ModelImpl();



    void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }
    @Override
    public void onUnbindView(String tag) {
        Log.d(TAG, "onUnbindView: " + tag);
        views.remove(tag);
        Log.d(TAG, "onUnBindView: " + views.toString() + " size = " + views.size());
    }

    @Override
    public  void onBindView(View view, String tag) {
        Log.d(TAG, "onBindView: "+ tag);
        views.put(tag,view);
        Log.d(TAG, "onBindView: " + views.toString() + " size = " + views.size());
    }

    View getViewByTag(String tag){
       return views.get(tag);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}

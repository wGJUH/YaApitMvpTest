package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import test.ya.translater.wgjuh.yaapitmvptest.model.Model;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;

import static android.support.v7.appcompat.R.styleable.View;

/**
 * Created by wGJUH on 04.04.2017.
 */

public abstract class BasePresenter implements Presenter {
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    protected Model model = new ModelImpl();



    protected void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}

package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import android.util.Log;

import retrofit2.Call;
import rx.Observer;
import rx.Subscription;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.model.data.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateView;

/**
 * Created by wGJUH on 04.04.2017.
 */

public class InputPresenter extends BasePresenter {
    InputTranslateView view;

    public InputPresenter(InputTranslateView view){
        this.view = view;
    }

    public void onButtonClearClick(){
        view.clearText();
    }

    public void onButtonTranslateClick(){

        Subscription subscription = model
                .getTranslateForLanguage(view.getTargetText(),"en-ru")
                .subscribe(new Observer<TranslatePojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TranslatePojo translatePojo) {
                        Log.d(DATA.TAG,translatePojo.getText().toString());
                    }

                });
        addSubscription(subscription);
    }

}

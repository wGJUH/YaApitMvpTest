package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import android.util.Log;

import rx.Observer;
import rx.Subscription;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.model.data.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.tabs.TransalteFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateFragment;

/**
 * Created by wGJUH on 04.04.2017.
 */

public class TranslateFragmentContainerImpl extends BasePresenter {
    TransalteFragment transalteFragment;
    InputTranslateFragment inputTranslateFragment;

    /**
     * Данный презентер будет являтся презентером для composite view состоящего из трех фрагментов
     * @param transalteFragment - Фрагмент на котором распологаются два замещаемых фрейма
     * @param inputTranslateFragment - фрагмент с полем ввода текста.
     */
    public TranslateFragmentContainerImpl(TransalteFragment transalteFragment, InputTranslateFragment inputTranslateFragment) {
        this.transalteFragment = transalteFragment;
        this.inputTranslateFragment = inputTranslateFragment;
    }

    public void addFragments() {
        transalteFragment.getTranslateFragmentManager().beginTransaction().add(transalteFragment.getInputFrame().getId(), inputTranslateFragment).commit();
        setPresenters();
    }

    public void setPresenters(){
        inputTranslateFragment.setPresenter(this);
    }

    /**
     * Метод вызывается из view фрагмента ввода текста и создает subcription на перевод текста для фрагмента вывода перевода
     */
    public void onButtonTranslateClick(){
        Subscription subscription = model
                .getTranslateForLanguage(inputTranslateFragment.getTargetText(),"en-ru")
                .subscribe(new Observer<TranslatePojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TranslatePojo translatePojo) {
                        // TODO: 05.04.2017 вызываем подписчика фрагмента вфвода на экран.
                        Log.d(DATA.TAG,translatePojo.getText().toString());
                    }

                });
        addSubscription(subscription);
    }

    @Override
    public void onStop() {

    }
}

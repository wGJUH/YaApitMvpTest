package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import android.util.Log;

import rx.Observer;
import rx.Subscription;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.model.data.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TransalteView;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateListFragment;

/**
 * Created by wGJUH on 04.04.2017.
 */
// TODO: 06.04.2017 добавить bind, unbind для вьюх срочно ! 
public class TranslateFragmentContainerImpl extends BasePresenter {
    TransalteView transalteView;
    InputTranslateFragment inputTranslateFragment;
    TranslateListFragment translateListFragment;

    /**
     * Данный презентер будет являтся презентером для composite view состоящего из трех фрагментов
     * @param transalteView - Фрагмент на котором распологаются два замещаемых фрейма
     * @param inputTranslateFragment - фрагмент с полем ввода текста.
     */
    public TranslateFragmentContainerImpl(TransalteView transalteView, InputTranslateFragment inputTranslateFragment, TranslateListFragment translateListFragment) {
        this.transalteView = transalteView;
        this.inputTranslateFragment = inputTranslateFragment;
        this.translateListFragment = translateListFragment;
    }

    public void addFragments() {
        Log.d(DATA.TAG, "addFragments: "+ transalteView.getTranslateFragmentManager().getFragments().size());
        transalteView.getTranslateFragmentManager().beginTransaction().add(transalteView.getInputFrame().getId(), inputTranslateFragment,inputTranslateFragment.getClass().getName()).commit();
        transalteView.getTranslateFragmentManager().beginTransaction().add(transalteView.getTranslateFrame().getId(), translateListFragment, translateListFragment.getClass().getName()).commit();
        setPresenters();
    }

    public void setPresenters(){
        inputTranslateFragment.setPresenter(this);
        translateListFragment.setPresenter(this);
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
                        updateTranslateView(translatePojo.getText().toString());
                    }
                });
        addSubscription(subscription);
    }

    public void updateTranslateView(String s){
        translateListFragment.showTranslate(s);
    }

    @Override
    public void onStop() {
        Log.d(DATA.TAG, "onStop: " + TranslateFragmentContainerImpl.this.getClass().getName());
    }
}

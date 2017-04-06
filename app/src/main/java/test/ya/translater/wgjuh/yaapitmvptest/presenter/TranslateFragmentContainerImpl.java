package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import android.util.Log;

import rx.Observer;
import rx.Subscription;
import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.model.data.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateView;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateListFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateListView;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;


/**
 * Created by wGJUH on 04.04.2017.
 */
// TODO: 06.04.2017 добавить bind, unbind для вьюх срочно !
public class TranslateFragmentContainerImpl extends BasePresenterForCompositeView {
    private TranslateFragment translateView;

    /**
     * Данный презентер будет являтся презентером для composite view состоящего из трех фрагментов
     */
    public TranslateFragmentContainerImpl() {
    }

    public void addFragments(InputTranslateFragment inputTranslateFragment, TranslateListFragment translateListFragment) {
        if (translateView == null)
            translateView = (TranslateFragment) getViewByTag(TranslateFragment.class.getName());
        translateView
                .getTranslateFragmentManager()
                .beginTransaction()
                .add(translateView.getInputFrame().getId(), inputTranslateFragment, inputTranslateFragment.getClass().getName())
                .add(translateView.getTranslateFrame().getId(), translateListFragment, translateListFragment.getClass().getName())
                .commit();
        bindViewsToPresenter(inputTranslateFragment, translateListFragment);
    }

    @Override
    public void onBindView(View view, String tag) {
        super.onBindView(view, tag);
        translateView = null;
    }

    /**
     * Метод позволяет забиндить фрагменты презентером, а также обнавляет данные о презенторе у самого фрагмента
     * @param views
     */
    public void bindViewsToPresenter(View... views) {
        for (View view : views) {
            onBindView( view, view.getClass().getName());
            view.setPresenter(this);
        }
    }

    /**
     * Метод вызывается из view фрагмента ввода текста и создает subscription на перевод текста для фрагмента вывода перевода
     */

    public boolean onButtonTranslateClick() {
        clearTranslate();
        // TODO: 06.04.2017 попробовать найти способ уйти от такого способа удержания подписанных fragments
        Subscription subscription = model
                .getTranslateForLanguage(((InputTranslateView) getViewByTag(InputTranslateFragment.class.getName())).getTargetText(), "en-ru")
                .subscribe(new Observer<TranslatePojo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TranslatePojo translatePojo) {
                        Log.d(DATA.TAG, translatePojo.getText().toString());
                        updateTranslateView(translatePojo.getText().toString());
                    }
                });
        addSubscription(subscription);
        return true;
    }

    /**
     * Метод вызывается при нажатии кнопки очистить в фрагменте ввода
     */
    public void onButtonClearClick(){
        clearTranslate();
        clearInput();

    }

    /**
     * Метод вызывает у View метод вывода переведенного текста на экран
     * @param s - строка которую необходимо вывести
     */
    private void updateTranslateView(String s) {
        ((TranslateListView) getViewByTag(TranslateListFragment.class.getName())).showTranslate(s);
    }

    /**
     * Метод для очистки поля ввода переводимого текста
     */
    private void clearInput(){
        ((InputTranslateFragment) getViewByTag(InputTranslateFragment.class.getName())).clearText();
    }

    /**
     * Метода для очистки поля переведенного текста
     */
    private  void clearTranslate(){
        ((TranslateListView) getViewByTag(TranslateListFragment.class.getName())).showTranslate("");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: " + TranslateFragmentContainerImpl.this.getClass().getName());
    }


}

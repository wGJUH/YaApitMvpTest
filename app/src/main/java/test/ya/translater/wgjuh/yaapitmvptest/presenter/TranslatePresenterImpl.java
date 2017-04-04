package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.tabs.TransalteFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.InputTranslateFragment;

/**
 * Created by wGJUH on 04.04.2017.
 */

public class TranslatePresenterImpl extends BasePresenter {
    TransalteFragment transalteFragment;
    InputTranslateFragment inputTranslateFragment;

    public TranslatePresenterImpl(TransalteFragment transalteFragment) {
        this.transalteFragment = transalteFragment;
    }

    public void addFragments() {
        inputTranslateFragment = new InputTranslateFragment();
        transalteFragment.getTranslateFragmentManager().beginTransaction().add(transalteFragment.getInputFrame().getId(), inputTranslateFragment).commit();
    }

    @Override
    public void onStop() {

    }
}

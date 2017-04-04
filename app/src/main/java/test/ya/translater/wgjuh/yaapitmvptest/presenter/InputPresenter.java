package test.ya.translater.wgjuh.yaapitmvptest.presenter;

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

    }

}

package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate;

import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

public interface InputView extends View {

    void clearText();

    String getTargetText();

    void setText(String text);
}

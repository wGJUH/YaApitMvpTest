package test.ya.translater.wgjuh.yaapitmvptest.presenter;

/**
 * Created by wGJUH on 16.04.2017.
 */

public interface InputPresenter extends Presenter {

    boolean onButtonTranslateClick();
    /**
     * Метод для запуска процесса перевода
     */
    void startTranslate();
    /**
     * Метод для запуска процесса очистки полей ввода переводимого текста
     * и перевода
     */
    void clearInput();


}

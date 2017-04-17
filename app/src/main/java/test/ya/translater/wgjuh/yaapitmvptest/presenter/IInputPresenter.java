package test.ya.translater.wgjuh.yaapitmvptest.presenter;

/**
 * Created by wGJUH on 16.04.2017.
 */

public interface IInputPresenter extends Presenter {

    boolean onButtonTranslateClick();
    /**
     * Метод для запуска процесса перевода
     */
    void startTranslate();
    /**
     * Метод для очистки поля ввода переводимого текста
     */
    void clearInput();


}

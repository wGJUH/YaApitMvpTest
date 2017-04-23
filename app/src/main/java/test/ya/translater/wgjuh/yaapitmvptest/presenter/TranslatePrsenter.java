package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefRecyclerItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

/**
 * Created by wGJUH on 16.04.2017.
 */

public interface TranslatePrsenter extends Presenter {

    void startTranslate();

    void setFavorite(boolean isFavorite);
    /**
     * Метод вызывает у View метод вывода переведенного текста на экран
     * @param translate - строка которую необходимо вывести
     */
    void updateTranslateView(String translate);

    /**
     * Метод для получения значений перевода
     * @param dictDTO объект из которого будут получены значения
     */
    void updateRecylcerView(DictDTO dictDTO);

    void updateChecboxFavorite(boolean isFavorite);
    /**
     * Метод для очистки поля переведенного текста
     */
    void clearTranslate();

    void addFavorite();

    void restoreState();

    void restoreState(DictDTO dictDTO);

    List<DefRecyclerItem> getDictionaryState();

    void startRetry();

    void deleteFavorite();
}

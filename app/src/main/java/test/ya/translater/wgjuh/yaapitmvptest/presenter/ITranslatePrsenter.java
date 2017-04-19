package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import android.os.Bundle;

import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefRecyclerItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

/**
 * Created by wGJUH on 16.04.2017.
 */

public interface ITranslatePrsenter extends Presenter {

    void setFavorite(boolean isFavorite);
    /**
     * Метод вызывает у View метод вывода переведенного текста на экран
     * @param translate - строка которую необходимо вывести
     */
    void updateTranslateView(String translate);

    void updateRecylcerView(DictDTO dictDTO);

    void updateChecboxFavorite(boolean isFavorite);
    /**
     * Метод для очистки поля переведенного текста
     */
    void clearTranslate();

    void addToFavorites();

    void saveOutState(Bundle outState);

    void setLastTranslate(DictDTO lastTranslate);

    void restoreState(DictDTO dictDTO);

    void restoreState();

    void startTranslate(String targetText);

    List<DefRecyclerItem> getDictionarySate();
}

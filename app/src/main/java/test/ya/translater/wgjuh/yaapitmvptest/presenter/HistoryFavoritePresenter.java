package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

/**
 * Created by wGJUH on 16.04.2017.
 */

public interface HistoryFavoritePresenter extends Presenter {


    /**
     * Метод возвращает набор переводов
     * @return List список который используется в адаптерах
     * @see DictDTO
     */
    List<DictDTO> getTranslateList();
    /**
     * Данный метод вызывается из адаптера и визуально изменяет объект в списке, а также отправляет в шину событие удаления
     * @param dictDTO объект который необходимо изменить и удалить
     */
    void deleteFavorite(DictDTO dictDTO);

    /**
     * Метода помещает объект в начало списка и уведомляет адаптер о том что список изменился
     * @param dictDTO объект помещаемый в начало списка
     */
    void insertItemInNoseOfAdapterDataAndNotify(DictDTO dictDTO);

    /**
     * Добавление объекта перевода в адаптер
     * @param dictDTO
     */
    void addItem(DictDTO dictDTO);

    /**
     * Обновление флага избранного
     * @param dictDTO какой объект необходимо обновить
     */
    void updateFavorite(DictDTO dictDTO);

    /**
     * Создание события обновления избранного
     * @param dictDTO объект который необходимо обновить
     */
    void addFavorite(DictDTO dictDTO);

    void subscribeToBusEvents();

    /**
     * Удалить объект из адаптера
     * @param dictDTO объект который необходимо удалить
     */
    void deleteItem(DictDTO dictDTO);

    void showTranslate(DictDTO dictDTO);

    void onLongItemClick(DictDTO dictDTO);

}

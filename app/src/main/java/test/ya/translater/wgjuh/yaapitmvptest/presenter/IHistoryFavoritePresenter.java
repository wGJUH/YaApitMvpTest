package test.ya.translater.wgjuh.yaapitmvptest.presenter;

import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

/**
 * Created by wGJUH on 16.04.2017.
 */

public interface IHistoryFavoritePresenter extends Presenter {


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
     * Метод помещает объект в конец списка и уведомляет адаптер о том что список изменился
     * @param dictDTO объект помещаемый в конец списка
     */
    void insertItemInTaleOfAdapterListAndNotify(DictDTO dictDTO);

    /**
     * Метода помещает объект в начало списка и уведомляет адаптер о том что список изменился
     * @param dictDTO объект помещаемый в начало списка
     */
    void insertItemInNoseOfAdapterDataAndNotify(DictDTO dictDTO);

    /**
     * Подписываемся на получение списка из бд
     */
    void initTranslateList();

    void addItem(DictDTO dictDTO);

    void updateFavorite(DictDTO dictDTO);

    void addFavorite(DictDTO dictDTO);

    void subscribeToBusEvents();

    void replaceItemsInAdapterData(int oldPosition, DictDTO dictDTO);

    void onEvent(Event event);
}

package test.ya.translater.wgjuh.yaapitmvptest.model.db;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;

/**
 * Created by wGJUH on 16.04.2017.
 */

public interface IDbBackEnd {
    /**
     * Метод добавления объекта в историю
     * @param dictDTO объект добавляемый в историю
     * @return положение объекта в таблице DB_TABLE_HISTORY
     * @see test.ya.translater.wgjuh.yaapitmvptest.model.db.Contractor.Translate
     */
    long insertHistoryTranslate(DictDTO dictDTO);

    /**
     * Метод копирования объекта из истории в избранное
     * @param id объекта переноса в таблице истории
     * @return id в таблице, в случае добавления нового
     */
    long insertFavoriteFromHistory(String id);

    /**
     * Метод устанавливает значение из таблицы избранное в поле "избранное"
     * строки в таблице история
     * @param historyId id объекта в таблице история
     * @param favoriteId id в таблице избранное
     */
    void setHistoryItemFavorite(String historyId, long favoriteId);

    /**
     * Метод удаляет из таблице избранное объект по его id, и
     * обнавляет поле избранное в таблице история, у которого
     * значение было равно favorit_id
     * @param favoriteId id избранного в таблице история
     */
    void removeHistoryItemFavorite(String favoriteId);
    /**
     * Метод позволяет получать сохраненный перевод из бд.
     * При получении в объекта типа DictDTO ему присваевается id записи в бд.
     * @see DictDTO
     * @param target текст который переводили
     * @param langs группа языков с/на который переводили
     * @return DictDTO объект перевода
     */
    DictDTO getHistoryTranslate(String target, String langs);

    /**
     * Метод позволяет получать сохраненный перевод из бд.
     * При получении в объекта типа DictDTO ему присваевается id записи в бд.
     * @see DictDTO
     * @param id идентификатор в таблице история
     * @return DictDTO объект перевода
     */
    DictDTO getHistoryTranslate(long id);

    /**
     * Метод обнавляет список доступных языков в таблице Языки
     * @see test.ya.translater.wgjuh.yaapitmvptest.model.db.Contractor.Langs
     * @param langsDirsModelDTO объект хранящий языки
     */
    void upateLangs(LangsDirsModelDTO langsDirsModelDTO);

    /**
     * Метод возвращает хранимые в базе языки
     * @return LangModel хранимые в базе языки
     */
    LangModel getStoredLangs();
}

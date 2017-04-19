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

    void setHistoryItemFavorite(DictDTO dictDTO, long favoriteId);


    void removeHistoryItemFavorite(DictDTO dictDTO);

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
    LangsDirsModelDTO getStoredLangs();

    int removeHistoryItem(DictDTO dictDTO);

    long insertFavorite(DictDTO dictDTO);

    DictDTO getFavoriteTranslate(String targetText, String translateDirection);
}

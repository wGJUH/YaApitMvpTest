package test.ya.translater.wgjuh.yaapitmvptest.model.db;

import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;

/**
 * Created by wGJUH on 16.04.2017.
 */

interface DbBackEnd {
    /**
     * Метод добавления объекта в историю
     *
     * @param dictDTO объект добавляемый в историю
     * @return положение объекта в таблице DB_TABLE_HISTORY
     * @see test.ya.translater.wgjuh.yaapitmvptest.model.db.Contractor.Translate
     */
    long insertHistoryTranslate(DictDTO dictDTO);

    void removeFavoriteItemAndUpdateHistory(DictDTO dictDTO);

    String getHistoryTranslate(String target, String langs);

    /**
     * Метод позволяет получать сохраненный перевод из бд.
     * При получении в объекта типа DictDTO ему присваевается id записи в бд.
     *
     * @param id идентификатор в таблице история
     * @return DictDTO объект перевода
     * @see DictDTO
     */

    String getHistoryTranslate(long id);

    /**
     * Метод обнавляет список доступных языков в таблице Языки
     *
     * @param langsDirsModelDTO объект хранящий языки
     * @see test.ya.translater.wgjuh.yaapitmvptest.model.db.Contractor.Langs
     */
    void upateLangs(LangsDirsModelDTO langsDirsModelDTO);

    /**
     * Метод возвращает хранимые в базе языки
     *
     * @return LangModel хранимые в базе языки
     */
    LangsDirsModelDTO getStoredLangs();

    int removeHistoryItem(DictDTO dictDTO);

    long insertFavoriteItem(DictDTO dictDTO);

    List<String> getHistoryListTranslate();

    String getFavoriteTranslate(String targetText, String translateDirection);

    List<String> getFavoriteListTranslate();

    String getLangByCode(String code);
}

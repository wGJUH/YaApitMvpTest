package test.ya.translater.wgjuh.yaapitmvptest.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wGJUH on 07.04.2017.
 */

public class Event<T> implements Serializable {
    public enum EventType {
        BTN_CLEAR_CLICKED,
        WORD_TRANSLATED,
        WORD_UPDATED,
        TARGET_LANGUAGE,
        FROM_LANGUAGE,
        ADD_FAVORITE, CHANGE_LANGUAGES
    }
    public final T[] content;
    public final EventType eventType;

    public Event(EventType eventType, T...content){
        this.eventType = eventType;
        this.content = content;
    }

    // TODO: 09.04.2017  выпилить методы, дергать поля на прямую
}
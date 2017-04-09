package test.ya.translater.wgjuh.yaapitmvptest.model;

import java.util.List;

/**
 * Created by wGJUH on 07.04.2017.
 */

public class Event<T> {
    public enum EventType {
        BTN_CLEAR_CLICKED,
        WORD_TRANSLATED,
        ON_LANGUAGE_CHNAGED
    }
    T[] content;
    EventType eventType;
    public Event(EventType eventType, T...content){
        this.eventType = eventType;
        this.content = content;
    }
    public T[] getEventObject(){
        return content;
    }
    public EventType getEventType(){
        return eventType;
    }
}
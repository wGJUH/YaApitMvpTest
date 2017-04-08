package test.ya.translater.wgjuh.yaapitmvptest;

/**
 * Created by wGJUH on 07.04.2017.
 */

public class Event<T> {
    public enum EventType {
        BTN_CLEAR_CLICKED,
        WORD_TRANSLATED
    }
    T content;
    EventType eventType;
    public Event(EventType eventType, T content){
        this.eventType = eventType;
        this.content = content;
    }
    public T getEventObject(){
        return content;
    }
    public EventType getEventType(){
        return eventType;
    }
}
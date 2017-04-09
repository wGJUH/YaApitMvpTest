package test.ya.translater.wgjuh.yaapitmvptest.model;

import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by wGJUH on 09.04.2017.
 */

public class EventBus implements IEventBus {

    private static EventBus instance;
    private static final Subject<Event, Event> eventBus = PublishSubject.create();

    private EventBus() {
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }


    @Override
    public Subject<Event, Event> getEventBus() {
        return eventBus;
    }

    @SafeVarargs
    @Override
    public final <T> Event createEvent(Event.EventType eventType, T... container) {
        return new Event(eventType,container);
    }
}

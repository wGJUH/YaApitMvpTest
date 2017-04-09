package test.ya.translater.wgjuh.yaapitmvptest.model;

import rx.subjects.Subject;

/**
 * Created by wGJUH on 09.04.2017.
 */

public interface IEventBus {

    Subject<Event, Event> getEventBus();

    <T>Event createEvent(Event.EventType eventType, T... container);

}

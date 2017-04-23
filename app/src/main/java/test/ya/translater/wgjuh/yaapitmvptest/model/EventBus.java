package test.ya.translater.wgjuh.yaapitmvptest.model;

import rx.Subscription;
import rx.functions.Action1;

public interface EventBus {

    void post(Event event);

    Subscription subscribe(Action1<Event> eventAction, Event.EventType... filter);

    Event createEvent(Event.EventType eventType, Object... container);

}

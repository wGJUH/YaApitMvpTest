package test.ya.translater.wgjuh.yaapitmvptest.model;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public interface IEventBus {

    void post(Event event);

    Subscription subscribe(Action1<Event> eventAction);

    Subscription subscribe(Subscriber<Event>  subscriber);

    Event createEvent(Event.EventType eventType, Object... container);

}

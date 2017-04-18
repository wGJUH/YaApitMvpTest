package test.ya.translater.wgjuh.yaapitmvptest.model;

import android.app.usage.UsageEvents;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action;
import rx.functions.Action1;
import rx.subjects.Subject;

/**
 * Created by wGJUH on 09.04.2017.
 */

public interface IEventBus {

    void post(Event event);

    Subscription subscribe(Action1<Event> eventAction);

    Subscription subscribe(Subscriber subscriber);

    Event createEvent(Event.EventType eventType, Object... container);

}

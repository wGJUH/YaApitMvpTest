package test.ya.translater.wgjuh.yaapitmvptest.model;

import android.support.annotation.VisibleForTesting;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;


public class EventBusImpl implements IEventBus {

    private static EventBusImpl instance;
    private static final Subject<Event, Event> eventBus = PublishSubject.create();

    public static EventBusImpl getInstance() {
        if (instance == null) {
            instance = new EventBusImpl();
        }
        return instance;
    }


    @Override
    public void post(Event event) {
        getEventBusForPost().onNext(event);
    }

    @Override
    public Subscription subscribe(Action1<Event> eventAction) {
        return getEventBus().subscribe(eventAction);
    }

    @Override
    public Subscription subscribe(Subscriber<Event> subscriber) {
        return getEventBus().subscribe(subscriber);
    }

    private Subject<Event, Event> getEventBusForPost() {
        return eventBus;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private Observable<Event> getEventBus() {
        return eventBus.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public final Event createEvent(Event.EventType eventType, Object... container) {
        return new Event(eventType,container);
    }
}

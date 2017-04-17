package test.ya.translater.wgjuh.yaapitmvptest.model;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;


/**
 * Created by wGJUH on 09.04.2017.
 */

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

    private Subject<Event, Event> getEventBusForPost() {
        return eventBus;
    }

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

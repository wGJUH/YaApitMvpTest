package test.ya.translater.wgjuh.yaapitmvptest.model;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;


/**
 * Created by wGJUH on 09.04.2017.
 */

public class EventBusImpl implements IEventBus {

    private static EventBusImpl instance;
    private static final Subject<Event, Event> eventBus = PublishSubject.create();


    private EventBusImpl() {
    }

    public static EventBusImpl getInstance() {
        if (instance == null) {
            instance = new EventBusImpl();
        }
        return instance;
    }


    @Override
    public Subject<Event, Event> getEventBusForPost() {
        return eventBus;
    }

    @Override
    public Observable<Event> getEventBus() {
        return eventBus.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @SafeVarargs
    @Override
    public final <T> Event createEvent(Event.EventType eventType, T... container) {
        return new Event(eventType,container);
    }
}

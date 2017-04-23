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


public class EventBusImpl implements EventBus {

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

    /**
     * Метод подписывающий на шину событий
     * @param eventAction метод обрабатывающий каждое новое событие
     * @param filter эвенты которые необходимо получить
     * @return подписка
     */
    @Override
    public Subscription subscribe(Action1<Event> eventAction, Event.EventType...filter) {
        return getEventBus().
                filter(event -> {
                    for (Event.EventType eventType:filter){
                        if (event.eventType.equals(eventType)){
                            return true;
                        }
                    }
                    return false;
                }).
                subscribe(eventAction);
    }

    /**
     * Метод подписывающий на шину событий
     * @param subscriber определенный подписчик
     * @return подписка
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Subscription subscribe(Subscriber<Event> subscriber) {
        return getEventBus().subscribe(subscriber);
    }

    private Subject<Event, Event> getEventBusForPost() {
        return eventBus;
    }

    private Observable<Event> getEventBus() {
        return eventBus.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     *  Создает событие с определенным типом и данными
     * @param eventType Тип события
     * @param container контейнер данных
     * @return  Event
     */
    @Override
    public final Event createEvent(Event.EventType eventType, Object... container) {
        return new Event(eventType,container);
    }
}

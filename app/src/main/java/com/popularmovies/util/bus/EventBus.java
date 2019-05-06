package com.popularmovies.util.bus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Event bus for movie events. This class is used as a channel of communication between non-related
 * parts of the application.
 * Publishers can use the @publish method to broadcast events. Observers can subscribe to this events
 * using the @getObservable method.
 * This process is implemented using the RxJava library.
 * @see <a href="https://github.com/ReactiveX/RxJava">https://github.com/ReactiveX/RxJava</a>
 * */
public class EventBus {

    private static EventBus instance;

    private static final PublishSubject<MovieEvent> eventBus = PublishSubject.create();

    private EventBus() {
        //Empty
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public void publish(MovieEvent event) {
        eventBus.onNext(event);
    }

    public Observable<MovieEvent> getObservable() {
        return eventBus;
    }
}

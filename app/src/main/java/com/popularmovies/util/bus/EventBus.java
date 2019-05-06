package com.popularmovies.util.bus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class EventBus {

    private static EventBus instance;

    private static final PublishSubject<MovieEvent> eventBus = PublishSubject.create();

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

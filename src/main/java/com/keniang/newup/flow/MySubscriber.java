package com.keniang.newup.flow;

import java.util.concurrent.Flow;

public class MySubscriber<T> implements Flow.Subscriber<T> {

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1); //a value of  Long.MAX_VALUE may be considered as effectively
    }

    @Override
    public void onNext(T item) {
        System.out.println("Got : " + item);
        subscription.request(1); //a value of  Long.MAX_VALUE may be considered as effectively unbounded
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Done");
    }
}

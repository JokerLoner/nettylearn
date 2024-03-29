package com.keniang.newup.flow;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

public class MyTransformProcessor<T,R> extends SubmissionPublisher<R> implements Flow.Processor<T,R>{

    private Function function;
    private Flow.Subscription subscription;

    public MyTransformProcessor(Function<? extends T, ? extends R> function) {
        super();
        this.function = function;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        submit((R)function.apply(item));
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        close();
    }
}

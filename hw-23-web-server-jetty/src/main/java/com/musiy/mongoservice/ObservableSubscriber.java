package com.musiy.mongoservice;

import com.mongodb.MongoTimeoutException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.MINUTES;

// Взято "как есть" с otus
public class ObservableSubscriber<T> implements Subscriber<T> {

    private final CountDownLatch latch = new CountDownLatch(1);
    private final List<T> results = new ArrayList<>();

    private volatile Throwable error;

    public ObservableSubscriber() {
    }

    @Override
    public void onSubscribe(final Subscription subscription) {
        subscription.request(Integer.MAX_VALUE);
    }

    @Override
    public void onNext(final T t) {
        results.add(t);
        boolean printResults = true;
    }

    @Override
    public void onError(final Throwable throwable) {
        error = throwable;
        onComplete();
    }

    @Override
    public void onComplete() {
        latch.countDown();
    }

    public List<T> getResults() {
        return results;
    }

    public void await() throws Throwable {
        if (!latch.await(10, MINUTES)) {
            throw new MongoTimeoutException("Publisher timed out");
        }
        if (error != null) {
            throw error;
        }
    }
}

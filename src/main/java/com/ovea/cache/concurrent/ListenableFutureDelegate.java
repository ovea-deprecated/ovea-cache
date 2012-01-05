package com.ovea.cache.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class ListenableFutureDelegate<V> implements ListenableFuture<V> {

    private final ListenableFuture<V> delegate;

    public ListenableFutureDelegate(ListenableFuture<V> delegate) {
        this.delegate = delegate;
    }

    public void addListener(FutureListener<V> vFutureListener) {
        delegate.addListener(vFutureListener);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return delegate.cancel(mayInterruptIfRunning);
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return delegate.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.get(timeout, unit);
    }

    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public boolean isDone() {
        return delegate.isDone();
    }
}

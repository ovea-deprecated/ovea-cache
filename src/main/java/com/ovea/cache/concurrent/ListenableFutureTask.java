package com.ovea.cache.concurrent;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.FutureTask;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class ListenableFutureTask<V> extends FutureTask<V> implements ListenableFuture<V> {

    private final List<FutureListener<V>> listeners = new CopyOnWriteArrayList<FutureListener<V>>();

    public ListenableFutureTask(Callable<V> vCallable) {
        super(vCallable);
    }

    public ListenableFutureTask(Runnable runnable, V result) {
        super(runnable, result);
    }

    @Override
    public final void addListener(FutureListener<V> listener) {
        listeners.add(listener);
    }

    @Override
    protected final void done() {
        onDone();
        if (isCancelled()) {
            for (FutureListener<V> listener : listeners) {
                listener.onComplete(this);
            }
        } else {
            for (FutureListener<V> listener : listeners) {
                listener.onCancelled(this);
            }
        }
    }

    protected void onDone() {
    }
}

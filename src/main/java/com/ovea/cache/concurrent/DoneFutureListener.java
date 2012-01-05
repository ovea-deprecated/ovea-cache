package com.ovea.cache.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class DoneFutureListener<V> implements FutureListener<V> {

    private final AtomicBoolean called = new AtomicBoolean(false);

    @Override
    public final void onComplete(ListenableFutureTask<V> task) {
        if (called.compareAndSet(false, true)) {
            onDone(task);
        }
    }

    @Override
    public final void onCancelled(ListenableFutureTask<V> task) {
        if (called.compareAndSet(false, true)) {
            onDone(task);
        }
    }

    protected abstract void onDone(ListenableFutureTask<V> task);
}

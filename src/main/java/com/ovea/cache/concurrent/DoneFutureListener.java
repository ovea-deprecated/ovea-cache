package com.ovea.cache.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class DoneFutureListener<V> implements FutureListener<V> {

    private final AtomicBoolean called = new AtomicBoolean(false);

    @Override
    public final void onComplete(Future<V> future) {
        if (called.compareAndSet(false, true)) {
            onDone(future);
        }
    }

    @Override
    public final void onCancelled(Future<V> future) {
        if (called.compareAndSet(false, true)) {
            onDone(future);
        }
    }

    protected abstract void onDone(Future<V> future);
}

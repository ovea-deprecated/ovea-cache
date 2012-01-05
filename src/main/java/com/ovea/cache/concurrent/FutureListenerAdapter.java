package com.ovea.cache.concurrent;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class FutureListenerAdapter<V> implements FutureListener<V> {
    @Override
    public void onComplete(ListenableFutureTask task) {
    }

    @Override
    public void onCancelled(ListenableFutureTask task) {
    }
}

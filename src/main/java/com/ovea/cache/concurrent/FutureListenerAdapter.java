package com.ovea.cache.concurrent;

import java.util.concurrent.Future;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class FutureListenerAdapter<V> implements FutureListener<V> {
    @Override
    public void onComplete(Future<V> future) {
    }

    @Override
    public void onCancelled(Future<V> future) {
    }
}

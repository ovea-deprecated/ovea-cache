package com.ovea.cache.concurrent;

import java.util.concurrent.Future;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface FutureListener<V> {
    void onComplete(Future<V> future);

    void onCancelled(Future<V> future);
}

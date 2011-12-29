package com.ovea.cache.concurrent;

import java.util.concurrent.Future;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ListenableFuture<V> extends Future<V> {
    void addListener(FutureListener<V> listener);
}

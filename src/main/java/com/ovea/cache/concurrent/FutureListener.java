package com.ovea.cache.concurrent;

import java.util.concurrent.Future;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface FutureListener<V> {
    void onComplete(ListenableFutureTask<V> task);

    void onCancelled(ListenableFutureTask<V> task);
}

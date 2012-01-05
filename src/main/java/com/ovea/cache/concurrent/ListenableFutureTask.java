/**
 * Copyright (C) 2011 Ovea <dev@ovea.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

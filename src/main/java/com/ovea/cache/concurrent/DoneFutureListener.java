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

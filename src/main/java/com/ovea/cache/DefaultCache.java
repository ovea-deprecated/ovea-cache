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
package com.ovea.cache;

import com.ovea.cache.concurrent.DoneFutureListener;
import com.ovea.cache.concurrent.ImmediateExecutor;
import com.ovea.cache.concurrent.ListenableFuture;
import com.ovea.cache.concurrent.ListenableFutureTask;
import com.ovea.cache.store.NoStore;

import java.util.concurrent.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultCache<T> implements Cache<T> {

    private final ConcurrentMap<String, ListenableFutureTask<T>> tasks = new ConcurrentHashMap<String, ListenableFutureTask<T>>();
    private final CacheStatsCounter counter = new CacheStatsCounter();
    private final Executor loadExecutor = new ImmediateExecutor();

    private CacheStore<T> store = new NoStore<T>();
    private CacheLoader<T> loader = new CacheLoader<T>() {
        @Override
        public T load(String key) throws Exception {
            return null;
        }
    };

    public DefaultCache<T> setStore(CacheStore<T> store) {
        this.store = store;
        return this;
    }

    public void setLoader(CacheLoader<T> loader) {
        this.loader = loader;
    }

    @Override
    public final CacheStats getStats() {
        return counter.snapshot();
    }

    @Override
    public T get(final String key) throws CacheException {
        Future<T> val = synchronizedGetOrLoad(key, new Callable<T>() {
            @Override
            public T call() throws Exception {
                return loader.load(key);
            }
        });
        try {
            return val.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CacheException(e);
        } catch (ExecutionException e) {
            counter.recordMiss();
            throw new CacheException(e.getCause());
        } catch (CancellationException e) {
            throw new CacheException(e);
        }
    }

    private ListenableFuture<T> synchronizedGetOrLoad(final String key, final Callable<T> loader) {
        ListenableFutureTask<T> task = new ListenableFutureTask<T>(new Callable<T>() {
            @Override
            public T call() throws Exception {
                T val = store.get(key);
                if (val == null) {
                    val = withStats(loader).call();
                    if (val != null) {
                        val = store.set(key, val);
                    }
                } else {
                    counter.recordHit();
                }
                return val;
            }
        });
        ListenableFutureTask<T> current = this.tasks.putIfAbsent(key, task);
        if (current != null) {
            return current;
        }
        task.addListener(new DoneFutureListener<T>() {
            @Override
            protected void onDone(ListenableFutureTask<T> task) {
                tasks.remove(key, task);
            }
        });
        loadExecutor.execute(task);
        return task;
    }

    private <T> Callable<T> withStats(final Callable<T> loader) {
        return new Callable<T>() {
            @Override
            public T call() throws Exception {
                long time = System.nanoTime();
                try {
                    T val = loader.call();
                    counter.recordLoadSuccess(System.nanoTime() - time);
                    return val;
                } catch (Exception e) {
                    counter.recordLoadException(System.nanoTime() - time);
                    throw e;
                }
            }
        };
    }

}

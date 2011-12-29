package com.ovea.cache;

import com.ovea.cache.concurrent.DoneFutureListener;
import com.ovea.cache.concurrent.ImmediateExecutor;
import com.ovea.cache.concurrent.ListenableFuture;
import com.ovea.cache.concurrent.ListenableFutureTask;

import javax.inject.Inject;
import java.util.concurrent.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class CacheRepositorySkeleton<T> implements CacheRepository<T> {

    private final ConcurrentMap<String, ListenableFutureTask<CacheEntry<T>>> tasks = new ConcurrentHashMap<String, ListenableFutureTask<CacheEntry<T>>>();
    private final CacheStatsCounter counter = new CacheStatsCounter();
    private Executor executor = new ImmediateExecutor();

    @Inject
    protected final void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public final void recordEviction() {
        counter.recordEviction();
    }

    @Override
    public final CacheStats getStats() {
        return counter.snapshot();
    }

    @Override
    public final ListenableFuture<CacheEntry<T>> getOrLoad(final String key, final Callable<CacheEntry<T>> loader) {
        final ListenableFutureTask<CacheEntry<T>> task = new ListenableFutureTask<CacheEntry<T>>(new Callable<CacheEntry<T>>() {
            @Override
            public CacheEntry<T> call() throws Exception {
                CacheEntry<T> entry = innerGet(key);
                if (entry == null) {
                    entry = innerSet(withStats(loader).call());
                } else {
                    counter.recordHit();
                }
                return entry;
            }
        });
        ListenableFutureTask<CacheEntry<T>> current = this.tasks.putIfAbsent(key, task);
        if (current != null) {
            return current;
        }
        task.addListener(new DoneFutureListener<CacheEntry<T>>() {
            @Override
            protected void onDone(Future<CacheEntry<T>> future) {
                tasks.remove(key, task);
            }
        });
        executor.execute(task);
        return task;
    }

    protected CacheEntry<T> innerGet(String key) throws Exception {
        return null;
    }

    protected CacheEntry<T> innerSet(CacheEntry<T> entry) throws Exception {
        return entry;
    }

    private <T> Callable<CacheEntry<T>> withStats(final Callable<CacheEntry<T>> loader) {
        return new Callable<CacheEntry<T>>() {
            @Override
            public CacheEntry<T> call() throws Exception {
                long time = System.nanoTime();
                try {
                    CacheEntry<T> entry = loader.call();
                    counter.recordLoadSuccess(System.nanoTime() - time);
                    return entry;
                } catch (Exception e) {
                    counter.recordLoadException(System.nanoTime() - time);
                    throw e;
                }
            }
        };
    }

}

package com.ovea.cache;

import java.util.concurrent.atomic.AtomicLong;

public class CacheStatsCounter {

    private final AtomicLong hitCount = new AtomicLong();
    private final AtomicLong missCount = new AtomicLong();
    private final AtomicLong loadSuccessCount = new AtomicLong();
    private final AtomicLong loadExceptionCount = new AtomicLong();
    private final AtomicLong totalLoadTime = new AtomicLong();
    private final AtomicLong evictionCount = new AtomicLong();

    public void recordHit() {
        hitCount.incrementAndGet();
    }

    public void recordLoadSuccess(long loadTime) {
        missCount.incrementAndGet();
        loadSuccessCount.incrementAndGet();
        totalLoadTime.addAndGet(loadTime);
    }

    public void recordLoadException(long loadTime) {
        missCount.incrementAndGet();
        loadExceptionCount.incrementAndGet();
        totalLoadTime.addAndGet(loadTime);
    }

    public void recordEviction() {
        evictionCount.incrementAndGet();
    }

    public CacheStats snapshot() {
        return new CacheStats(
            hitCount.get(),
            missCount.get(),
            loadSuccessCount.get(),
            loadExceptionCount.get(),
            totalLoadTime.get(),
            evictionCount.get());
    }

}

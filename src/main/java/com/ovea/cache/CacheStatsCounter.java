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

    public void recordMiss() {
        missCount.incrementAndGet();
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

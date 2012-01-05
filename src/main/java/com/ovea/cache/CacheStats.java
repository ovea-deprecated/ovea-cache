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

public final class CacheStats {

    private final long hitCount;
    private final long missCount;
    private final long loadSuccessCount;
    private final long loadExceptionCount;
    private final long totalLoadTime;
    private final long evictionCount;

    CacheStats(long hitCount, long missCount, long loadSuccessCount, long loadExceptionCount, long totalLoadTime, long evictionCount) {
        this.hitCount = hitCount;
        this.missCount = missCount;
        this.loadSuccessCount = loadSuccessCount;
        this.loadExceptionCount = loadExceptionCount;
        this.totalLoadTime = totalLoadTime;
        this.evictionCount = evictionCount;
    }

    public long requests() {
        return hitCount + missCount;
    }

    public long hits() {
        return hitCount;
    }

    public double hitRate() {
        long requestCount = requests();
        return (requestCount == 0) ? 1.0 : (double) hitCount / requestCount;
    }

    public long miss() {
        return missCount;
    }

    public double missRate() {
        long requestCount = requests();
        return (requestCount == 0) ? 0.0 : (double) missCount / requestCount;
    }

    public long loads() {
        return loadSuccessCount + loadExceptionCount;
    }

    public long loadSuccess() {
        return loadSuccessCount;
    }

    public long loadException() {
        return loadExceptionCount;
    }

    public double loadExceptionRate() {
        long totalLoadCount = loadSuccessCount + loadExceptionCount;
        return (totalLoadCount == 0)
            ? 0.0
            : (double) loadExceptionCount / totalLoadCount;
    }

    public long totalLoadTime() {
        return totalLoadTime;
    }

    public double averageLoadPenalty() {
        long totalLoadCount = loadSuccessCount + loadExceptionCount;
        return (totalLoadCount == 0)
            ? 0.0
            : (double) totalLoadTime / totalLoadCount;
    }

    public long evictionCount() {
        return evictionCount;
    }

    public CacheStats add(CacheStats other) {
        return new CacheStats(
            hitCount + other.hitCount,
            missCount + other.missCount,
            loadSuccessCount + other.loadSuccessCount,
            loadExceptionCount + other.loadExceptionCount,
            totalLoadTime + other.totalLoadTime,
            evictionCount + other.evictionCount);
    }

    public CacheStats minus(CacheStats other) {
        return new CacheStats(
            Math.max(0, hitCount - other.hitCount),
            Math.max(0, missCount - other.missCount),
            Math.max(0, loadSuccessCount - other.loadSuccessCount),
            Math.max(0, loadExceptionCount - other.loadExceptionCount),
            Math.max(0, totalLoadTime - other.totalLoadTime),
            Math.max(0, evictionCount - other.evictionCount));
    }

    @Override
    public String toString() {
        return "CacheStats{" +
            "hitCount=" + hitCount +
            ", missCount=" + missCount +
            ", loadSuccessCount=" + loadSuccessCount +
            ", loadExceptionCount=" + loadExceptionCount +
            ", totalLoadTime=" + totalLoadTime +
            ", evictionCount=" + evictionCount +
            '}';
    }
}

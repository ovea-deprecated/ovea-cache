package com.ovea.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class CacheForwardingCacheEntryBuilder<T> implements CacheEntryBuilder<T> {
    private final Cache<T> cache;

    public CacheForwardingCacheEntryBuilder(Cache<T> cache) {
        this.cache = cache;
    }

    @Override
    public CacheEntry<T> build(String key) throws Exception {
        return cache.get(key);
    }
}

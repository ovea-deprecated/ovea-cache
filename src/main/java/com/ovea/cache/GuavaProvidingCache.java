package com.ovea.cache;

import com.google.common.cache.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class GuavaProvidingCache<T> implements ProvidingCache<T> {

    private final com.google.common.cache.Cache<String, CacheEntry<T>> cache;

    public GuavaProvidingCache() {
        new CacheBuilder<>().build()
    }

    public GuavaProvidingCache(com.google.common.cache.Cache<String, CacheEntry<T>> cache) {
        this.cache = cache;
    }

    @Override
    public void add(CacheEntry<T> entry) {
        cache.
    }

    @Override
    public T get(String key) throws CacheException {
        CacheEntry<T> entry = cache.get(key);
        return entry == null ? null : entry.value();
    }
}

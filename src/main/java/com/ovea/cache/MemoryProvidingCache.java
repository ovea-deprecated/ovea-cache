package com.ovea.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MemoryProvidingCache<T> implements ProvidingCache<T> {

    private final Map<String, CacheEntry<T>> cache;

    public MemoryProvidingCache() {
        this(new HashMap<String, CacheEntry<T>>());
    }

    public MemoryProvidingCache(Map<String, CacheEntry<T>> cache) {
        this.cache = cache;
    }

    @Override
    public void add(CacheEntry<T> entry) {
        cache.put(entry.key(), entry);
    }

    @Override
    public T get(String key) throws CacheException {
        CacheEntry<T> entry = cache.get(key);
        return entry == null ? null : entry.value();
    }
}

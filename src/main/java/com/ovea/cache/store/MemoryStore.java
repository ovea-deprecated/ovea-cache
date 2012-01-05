package com.ovea.cache.store;

import com.ovea.cache.CacheStore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MemoryStore<T> implements CacheStore<T> {

    private final Map<String, T> cache;

    public MemoryStore() {
        this(new HashMap<String, T>());
    }

    public MemoryStore(Map<String, T> cache) {
        this.cache = cache;
    }

    @Override
    public T get(String key) {
        return cache.get(key);
    }

    @Override
    public T set(String key, T val) {
        cache.put(key, val);
        return val;
    }

}

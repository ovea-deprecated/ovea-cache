package com.ovea.cache.store;

import com.ovea.cache.CacheStore;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class NoStore<T> implements CacheStore<T> {
    @Override
    public T get(String key) {
        return null;
    }

    @Override
    public T set(String key, T val) {
        return val;
    }
}

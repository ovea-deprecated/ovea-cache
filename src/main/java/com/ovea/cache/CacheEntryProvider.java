package com.ovea.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface CacheEntryProvider<T> {
    CacheEntry<T> get(String key) throws CacheException;
}

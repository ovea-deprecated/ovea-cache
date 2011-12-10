package com.ovea.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface MutableCache<T> extends Cache<T> {
    /**
     * @return The value associated for the key of null of none
     */
    void add(CacheEntry<T> entry);
}
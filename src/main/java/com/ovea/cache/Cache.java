package com.ovea.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Cache<T> {
    /**
     * @return The value associated for the key or null of none
     */
    T get(String key) throws CacheException;
}
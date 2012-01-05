package com.ovea.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface CacheStore<T> {
    T get(String key) throws Exception;

    T set(String key, T val) throws Exception;
}

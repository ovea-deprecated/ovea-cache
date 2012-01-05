package com.ovea.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface CacheLoader<T> {
    T load(String key) throws Exception;
}

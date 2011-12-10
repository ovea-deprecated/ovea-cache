package com.ovea.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class ExpiringCacheEntry<T> extends CacheEntry<T> {
    public ExpiringCacheEntry(String key, T value, long expiration, TimeUnit unit) {
        super(key, value);
    }
}

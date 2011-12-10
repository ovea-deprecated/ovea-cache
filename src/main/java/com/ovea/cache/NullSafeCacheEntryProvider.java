package com.ovea.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class NullSafeCacheEntryProvider<T> implements CacheEntryProvider<T> {

    private final CacheEntryProvider<T> delegate;

    public NullSafeCacheEntryProvider(CacheEntryProvider<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public CacheEntry<T> get(String key) throws CacheException {
        CacheEntry<T> entry = delegate.get(key);
        return entry == null ? new CacheEntry<T>(key) : entry;
    }
}

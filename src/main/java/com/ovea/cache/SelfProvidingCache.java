package com.ovea.cache;

/**
 * Cache which automatically feeds the delegate cache if it does not contains the key (returns null), by asking
 * the value to a provider
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SelfProvidingCache<T> implements Cache<T> {
    private final MutableCache<T> delegate;
    private final CacheEntryProvider<T> provider;

    public SelfProvidingCache(MutableCache<T> delegate, CacheEntryProvider<T> provider) {
        this.delegate = delegate;
        this.provider = new NullSafeCacheEntryProvider<T>(provider);
    }

    @Override
    public T get(String key) throws CacheException {
        T val = delegate.get(key);
        if (val == null) {
            CacheEntry<T> entry = provider.get(key);
            val = entry.value();
            delegate.add(entry);
        }
        return val;
    }
}

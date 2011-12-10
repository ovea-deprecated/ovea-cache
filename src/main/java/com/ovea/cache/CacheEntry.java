package com.ovea.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class CacheEntry<T> {

    private static final Object NULL = new Object();

    private final String key;
    private final T value;

    public CacheEntry(String key) {
        this(key, null);
    }

    public CacheEntry(String key, T value) {
        this.key = key;
        //noinspection unchecked
        this.value = value == null ? (T) NULL : value;
    }

    public final String key() {
        return key;
    }

    public final T value() {
        return value == NULL ? null : value;
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheEntry that = (CacheEntry) o;
        return key.equals(that.key);
    }

    @Override
    public final int hashCode() {
        return key.hashCode();
    }

    public boolean isEmpty() {
        return value == NULL;
    }

}

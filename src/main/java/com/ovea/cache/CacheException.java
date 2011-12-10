package com.ovea.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class CacheException extends RuntimeException {

    private Throwable throwable;

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
        if (throwable instanceof CacheException) {
            this.throwable = ((CacheException) cause).throwable;
        } else {
            this.throwable = cause;
        }
    }

    public CacheException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public <T extends Throwable> boolean contains(Class<T> type) {
        return type.isInstance(throwable);
    }

    public <T extends Throwable> void rethrowIf(Class<T> type) throws T {
        if (contains(type))
            throw type.cast(throwable);
    }
}

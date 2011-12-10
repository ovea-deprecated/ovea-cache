/**
 * Copyright (C) 2011 Ovea <dev@ovea.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

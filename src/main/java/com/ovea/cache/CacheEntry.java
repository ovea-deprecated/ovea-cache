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

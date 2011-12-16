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
 * Cache which automatically feeds the delegate cache if it does not contains the key (returns null), by asking
 * the value to a provider
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentProvidingCache<T> implements Cache<T> {
    private final ProvidingCache<T> delegate;
    private final CacheEntryProvider<T> provider;

    public ConcurrentProvidingCache(ProvidingCache<T> delegate, CacheEntryProvider<T> provider) {
        this.delegate = delegate;
        this.provider = new NullSafeCacheEntryProvider<T>(provider);
    }

    @Override
    public T get(String key) throws CacheException {

        T val = delegate.get(key);
        if (val == null) {
            lock(key);
            CacheEntry<T> entry;
            try {
                entry = provider.get(key);
            } catch (Throwable throwable) {
                throw new CacheException(throwable);
            }
            val = entry.value();
            delegate.add(entry);
        }
        return val;
    }

}

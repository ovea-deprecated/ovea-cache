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

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Cache which automatically feeds the delegate cache if it does not contains the key (returns null), by asking
 * the value to a provider
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ProvidingCache<T> implements Cache<T> {

    private final CacheRepository<T> repository;
    private final CacheEntryBuilder<T> loader;

    public ProvidingCache(CacheRepository<T> repository, CacheEntryBuilder<T> loader) {
        this.repository = repository;
        this.loader = loader;
    }

    @Override
    public CacheEntry<T> get(final String key) throws CacheException {
        Future<CacheEntry<T>> entry = repository.getOrLoad(key, new Callable<CacheEntry<T>>() {
            @Override
            public CacheEntry<T> call() throws Exception {
                return loader.build(key);
            }
        });
        try {
            return entry.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CacheException(e);
        } catch (ExecutionException e) {
            throw new CacheException(e.getCause());
        } catch (CancellationException e) {
            throw new CacheException(e);
        }
    }

}

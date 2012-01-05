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
import com.ovea.cache.Cache
import com.ovea.cache.CacheLoader
import com.ovea.cache.CacheStore
import com.ovea.cache.DefaultCache
import com.ovea.cache.store.NoStore
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

ConcurrentMap<String, AtomicInteger> checkCannotLoadConcurrently = new ConcurrentHashMap<String, AtomicInteger>()
def incrementAndGet = {String key ->
    AtomicInteger ai = new AtomicInteger(0)
    AtomicInteger curr = checkCannotLoadConcurrently.putIfAbsent(key, ai)
    if (curr == null) curr = ai
    return curr.incrementAndGet()
}

CacheStore<String> store = new NoStore<String>()
CacheLoader<String> loader = {String key ->
    try {
        int n = incrementAndGet(key)
        assert n == 1
        def a = 'T' + Thread.currentThread().name + ' - CacheLoader[' + key + '] - #' + n
        println a
        if(key == 'error') throw new Exception(a)
        return key == 'null' ? null : key + " : value"
    } finally {
        checkCannotLoadConcurrently.get(key).decrementAndGet()
    }
} as CacheLoader<String>
Cache<String> cache = new DefaultCache<String>(store: store, loader: loader)

int nThreads = 15
CountDownLatch ready = new CountDownLatch(1)
CountDownLatch finished = new CountDownLatch(nThreads)

nThreads.times {
    Thread.start "${it}".padLeft(2, '0'), {
        try {
            ready.await()
            println "T${Thread.currentThread().name} - " + cache.get("null")
            println "T${Thread.currentThread().name} - " + cache.get("null")
            println "T${Thread.currentThread().name} - " + cache.get("hello")
            println "T${Thread.currentThread().name} - " + cache.get("hello")
            try {
                println 'T' + Thread.currentThread().name + ' - ' + cache.get("error")
                assert false
            } catch (e) {
                println 'T' + Thread.currentThread().name + ' - ERROR: ' + e.message
                assert true
            }
            sleep(1500)
            println "T${Thread.currentThread().name} - " + cache.get("hello")
            println "T${Thread.currentThread().name} - " + cache.get("hello")
        } finally {
            finished.countDown()
        }
    }
}

ready.countDown()
finished.await()
println cache.stats

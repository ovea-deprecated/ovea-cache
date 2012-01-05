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

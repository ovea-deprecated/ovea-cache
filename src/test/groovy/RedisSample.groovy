import com.mycila.inject.redis.RedisMethodInterceptor
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import redis.clients.jedis.JedisPool
import com.google.inject.*
import com.ovea.cache.*
import com.ovea.cache.concurrent.ImmediateExecutor

Injector injector = Guice.createInjector(new AbstractModule() {
    @Override
    protected void configure() {
        RedisMethodInterceptor.bind(binder())
        //bind(new TypeLiteral<CacheRepository<String>>() {}).to(RedisCacheRepository)
        bind(new TypeLiteral<CacheRepository<String>>() {}).to(NoCacheRepository)
        bind(JedisPool).toInstance(new JedisPool("localhost", 6379))
        bind(Executor).to(ImmediateExecutor)
    }
})

CacheRepository<String> repository = injector.getInstance(Key.get(new TypeLiteral<CacheRepository<String>>() {}))

AtomicInteger checkCannotLoadConcurrently = new AtomicInteger(0)

Cache<String> cache = new ProvidingCache<String>(repository, new CacheEntryBuilder<String>() {
    CacheEntry<String> build(String key) {
        try {
            int n = checkCannotLoadConcurrently.incrementAndGet()
            assert n == 1
            println "T${Thread.currentThread().name} - CacheEntryBuilder[${key}] - #${n}"
            return key == 'null' ? null : new ExpiringCacheEntry<String>(key, key + "-built", 1, TimeUnit.SECONDS)
        } finally {
            checkCannotLoadConcurrently.decrementAndGet()
        }
    }
})

CountDownLatch ready = new CountDownLatch(1)
CountDownLatch finished = new CountDownLatch(10)

10.times {
    Thread.start "${it}".padLeft(2, '0'), {
        ready.await()
        println "T${Thread.currentThread().name} - " + cache.get("null")
        println "T${Thread.currentThread().name} - " + cache.get("null")
        println "T${Thread.currentThread().name} - " + cache.get("hello")
        println "T${Thread.currentThread().name} - " + cache.get("hello")
        sleep(1500)
        println "T${Thread.currentThread().name} - " + cache.get("hello")
        println "T${Thread.currentThread().name} - " + cache.get("hello")
        finished.countDown()
    }
}

ready.countDown()
finished.await()

println repository.stats

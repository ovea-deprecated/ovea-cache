package com.ovea.cache;

import javax.inject.Inject;
import java.util.concurrent.Executor;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class RedisCacheRepository extends CacheRepositorySkeleton<String> {

    private final Executor executor;

    @Inject
    public RedisCacheRepository(Executor executor) {
        this.executor = executor;
    }


}

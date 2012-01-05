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
package com.ovea.cache.store;

import com.ovea.cache.CacheStore;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class RedisStore implements CacheStore<String> {

    private final JedisPool pool;

    public RedisStore(JedisPool pool) {
        this.pool = pool;
    }

    @Override
    public String get(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.get(key);
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public String set(String key, String val) {
        Jedis jedis = pool.getResource();
        try {
            jedis.set(key, val);
        } finally {
            pool.returnResource(jedis);
        }
        return val;
    }


}

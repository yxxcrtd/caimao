/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.gate.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("redisUtils")
public class RedisUtils {

    @Resource
    private RedisTemplate<String, ?> commonRedisTemplate;

    public void set(int index, final String key, final String value, final Long liveTime) {
        commonRedisTemplate.execute(new RedisCallback<Object>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key.getBytes(), value.getBytes());
                if (null != liveTime && liveTime > 0) {
                    connection.expire(key.getBytes(), liveTime);
                }
                return 1L;
            }
        });
    }

    public void hSet(int index, final String key, final String field, final String value, final Long liveTime) {
        commonRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection conn) throws DataAccessException {
                conn.hSet(key.getBytes(), field.getBytes(), value.getBytes());
                if (null != liveTime && liveTime > 0) {
                    conn.expire(key.getBytes(), liveTime);
                }
                return 1L;
            }
        });
    }

    public Object get(int index, final String key) {
        final RedisTemplate<String, ?> redisTemplate = commonRedisTemplate;
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection conn) {
                byte[] bvalue = conn.get(key.getBytes());
                if (null == bvalue) {
                    return null;
                } else {
                    return redisTemplate.getStringSerializer().deserialize(bvalue);
                }
            }
        });
    }

    public Object hGet(int index, final String key, final String field) {
        final RedisTemplate<String, ?> redisTemplate = commonRedisTemplate;
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection conn) {
                byte[] bvalue = conn.hGet(key.getBytes(), field.getBytes());
                if (null == bvalue) {
                    return null;
                } else {
                    return redisTemplate.getStringSerializer().deserialize(bvalue);
                }
            }
        });
    }

    public long del(int index, final String... keys) {
        return (long) commonRedisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    result = result + connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }

    public void hDel(int index, final String key, final String field) {
        commonRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection conn) throws DataAccessException {
                return conn.hDel(key.getBytes(), field.getBytes());
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private byte[][] serializeMulti(RedisTemplate redisTemplate, String... keys) {
        byte[][] ret = new byte[keys.length][];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = redisTemplate.getStringSerializer().serialize(keys[i]);
        }
        return ret;
    }
}

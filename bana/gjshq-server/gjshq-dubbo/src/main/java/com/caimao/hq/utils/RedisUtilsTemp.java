/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.hq.utils;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("RedisUtilstemp")
public class RedisUtilsTemp {
	private static Logger logger = LoggerFactory.getLogger(RedisUtilsTemp.class);
	@Autowired
	ExceptionUtils exceptionUtils;
	public void set(Integer index, final byte[] key, final byte[] value,
			final long liveTime) {
		getRedisTemplate(index).execute(new RedisCallback<Object>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(key, value);
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}


//	public  void set(int index,String redisKey,Object object) {
//
//		final RedisTemplate redisTemplate = getRedisTemplate(index);
//		ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
//		valueops.set(redisKey, object);
//	}

//	public  Object get(int index,String redisKey) {
//
//		final RedisTemplate redisTemplate = getRedisTemplate(index);
//		ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
//		return valueops.get(redisKey);
//	}

	public void set(Integer index, String key, String value, long liveTime) {
		this.set(index, key.getBytes(), value.getBytes(), liveTime);
	}

	public Object get(Integer index, final String key) {
		final RedisTemplate<String, ?> redisTemplate = getRedisTemplate(index);
		return redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection conn) {
				byte[] bvalue = conn.get(key.getBytes());
				if (null == bvalue) {
					return null;
				} else {
					return redisTemplate.getStringSerializer().deserialize(
							bvalue);
				}
			}
		});
	}

	public Object hGet(Integer index, final String key, final String field) {
		final RedisTemplate<String, ?> redisTemplate = getRedisTemplate(index);
		return redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection conn) {
				byte[] bvalue = conn.hGet(key.getBytes(), field.getBytes());
				if (null == bvalue) {
					return null;
				} else {
					return redisTemplate.getStringSerializer().deserialize(
							bvalue);
				}
			}
		});
	}

	public void hSet(Integer index, final String key, final String field,
			final String value) {
		getRedisTemplate(index).execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection conn)
					throws DataAccessException {
				conn.hSet(key.getBytes(), field.getBytes(), value.getBytes());
				return null;
			}
		});
	}

	public void rPush(Integer index, final String key, final String value) {
		getRedisTemplate(index).execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection conn)
					throws DataAccessException {
				conn.rPush(key.getBytes(), value.getBytes());
				return null;
			}
		});
	}
	//设置某一key的超时时间,单位秒
	public void expired(Integer index, String key, long timeout) {
		getRedisTemplate(index).expire(key, timeout, TimeUnit.SECONDS);
	}

	public RedisTemplate<String, ?> getRedisTemplate(Integer index) {
		return ApplicationContextUtils.getBean("redisTemplate" + index);
	}

	public void del(Integer index, final String key) {
		final RedisTemplate<String, ?> redisTemplate = getRedisTemplate(index);
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection conn) {
				return conn.del(key.getBytes());
			}
		});
	}
	/**
     * key以秒为单位,返回给定 key 的剩余生存时间
     * @param key
     */
    public Long ttl(Integer index,final String key) {
        final RedisTemplate<String, ?> redisTemplate = getRedisTemplate(index);
        return redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
             return connection.ttl(key.getBytes());
            }
        });
    } 
    /**
     * 对一个key的value加一
     * @param key
     */
    public Long incr(Integer index,final String key) {
        final RedisTemplate<String, ?> redisTemplate = getRedisTemplate(index);
        return redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
             return connection.incr(key.getBytes());
            }
        });
    }
    public Long getTtl(Integer index,final String key) {
        final RedisTemplate<String, ?> redisTemplate = getRedisTemplate(index);
        return  (Long) redisTemplate.execute(new RedisCallback<Object>() {
             public Long doInRedis(RedisConnection conn){
                 Long value = conn.ttl(key.getBytes());
                 return value;
             }
         });
     }


}

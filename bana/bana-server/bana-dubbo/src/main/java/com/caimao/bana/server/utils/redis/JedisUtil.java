package com.caimao.bana.server.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * Jedis util
 * Created by Administrator on 2015/11/30.
 */
@Component
public class JedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);


    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取一个 jedis 对象，进行使用
     * @return
     */
    public Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
        } catch (Exception e) {
            logger.error("get jedis resource error {}", e);
            throw e;
        }
        return jedis;
    }

    /**
     * 使用完回收jedis对象
     * @param jedis
     */
    public void returnJedis(Jedis jedis) {
        if (jedis != null) {
            this.jedisPool.returnResource(jedis);
        }
    }

}

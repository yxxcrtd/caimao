package com.caimao.bana.server.service.getui;

import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.utils.ApplicationContextUtils;
import com.caimao.bana.server.utils.redis.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 个推消息发送的队列
 * Created by Administrator on 2015/11/30.
 */
@Component
public class GetuiSendQueue implements BlockingQueue<Runnable> {

    private static final Logger logger = LoggerFactory.getLogger(GetuiSendQueue.class);

    private String redisKey;
    private Integer maxLength;

    @Autowired
    private JedisUtil jedisUtil;

    /** Main lock guarding all access */
    ReentrantLock lock;

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * 构造初始方法
     * @param maxLength 队列最大值
     * @param redisKey  redisKey
     */
    public GetuiSendQueue(Integer maxLength, String redisKey) {
        this.redisKey = redisKey;
        this.maxLength = maxLength;
        this.lock = new ReentrantLock(true);
    }

    public Jedis getJedis() throws CustomerException {
        Jedis jedis = this.jedisUtil.getJedis();
        if (jedis == null) {
            throw new CustomerException("get jedis obj error", 88881);
        }
        return jedis;
    }

    @Override
    public boolean add(Runnable Runnable) {
        return this.offer(Runnable);
    }

    /**
     * 在队列中添加任务
     * @param Runnable
     * @return
     */
    @Override
    public boolean offer(Runnable Runnable) {
        if (Runnable == null) {
            throw new NullPointerException();
        }
        GetuiSendRunnable r = (GetuiSendRunnable)Runnable;
        Long id = r.getMsgId();
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.lpush(this.redisKey, id.toString());
        } catch (Exception e) {
            logger.error(" getuiSendQueue offer error {}", e);
            return false;
        } finally {
            this.jedisUtil.returnJedis(jedis);
        }
        return true;
    }

    @Override
    public Runnable remove() {
        return null;
    }

    @Override
    public Runnable poll() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            GetuiSendRunnable r = new GetuiSendRunnable();
            String idStr = jedis.rpop(this.redisKey);
            if (idStr == null) return null;
            Long id = Long.valueOf(idStr);
            if (id != 0) {
                r.setMsgId(id);
                r.setRedisKey(this.redisKey);
                return r;
            }
        } catch (Exception e) {
            logger.error("getuiSendQueue poll error {}", e);
            return null;
        } finally {
            lock.unlock();
            this.jedisUtil.returnJedis(jedis);
        }
        return null;
    }

    @Override
    public Runnable element() {
        return null;
    }

    @Override
    public Runnable peek() {
        return null;
    }

    @Override
    public void put(Runnable Runnable) throws InterruptedException {
        this.offer(Runnable);
    }

    @Override
    public boolean offer(Runnable Runnable, long timeout, TimeUnit unit) throws InterruptedException {
        return this.offer(Runnable);
    }

    @Override
    public Runnable take() throws InterruptedException {
        return this.poll();
    }

    @Override
    public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
        return this.poll();
    }

    @Override
    public int remainingCapacity() {
        return this.maxLength = this.size();
    }

    @Override
    public boolean remove(Object o) {
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Runnable> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            Long len = jedis.llen(this.redisKey);
            return len == null ? 0 : len.intValue();
        } catch (Exception e) {
            logger.error("getuiSendQueue size error {}", e);
            return 0;
        } finally {
            this.jedisUtil.returnJedis(jedis);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Runnable> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public int drainTo(Collection<? super Runnable> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super Runnable> c, int maxElements) {
        return 0;
    }
}

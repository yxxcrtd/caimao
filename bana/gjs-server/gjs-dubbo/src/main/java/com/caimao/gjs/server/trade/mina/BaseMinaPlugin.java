package com.caimao.gjs.server.trade.mina;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public abstract class BaseMinaPlugin implements MinaPlugin{
    private static final Logger logger = LoggerFactory.getLogger(BaseMinaPlugin.class);

    private PoolConnectFactory poolConnectFactory;

    private GenericObjectPool<IoSession> pool;

    protected abstract ProtocolCodecFilter getProtocolCodecFilter();

    protected abstract IoHandler getHandler();

    @Override
    public void init(Element el) {
        try{
            poolConnectFactory = new PoolConnectFactory();
            poolConnectFactory.setPoolName(el.getAttribute("name"));
            poolConnectFactory.setUserName(el.getAttribute("userName"));
            poolConnectFactory.setPassword(el.getAttribute("password"));
            poolConnectFactory.setHostName(el.getAttribute("ip"));
            poolConnectFactory.setPort(Integer.parseInt(el.getAttribute("port")));
            poolConnectFactory.setProtocolCodecFilter(getProtocolCodecFilter());
            poolConnectFactory.setIdleTime(Integer.parseInt(el.getAttribute("idle")));
            poolConnectFactory.setIoHandler(getHandler());
            poolConnectFactory.setConnectCount(Integer.parseInt(el.getAttribute("connect")));
            poolConnectFactory.setNeedReconnect(Boolean.parseBoolean(el.getAttribute("needReconnect")));
            pool = new GenericObjectPool<>(poolConnectFactory);
            pool.setMaxTotal(150);
            pool.setMaxWaitMillis(60000);
            pool.setBlockWhenExhausted(true);
            pool.setLifo(false);
            if(!Boolean.parseBoolean(el.getAttribute("needReconnect"))){
                pool.setTestOnBorrow(true);
                pool.setMinIdle(poolConnectFactory.getConnectCount());
                pool.setTestWhileIdle(true);
                pool.setTimeBetweenEvictionRunsMillis(poolConnectFactory.getIdleTime() * 1000);
            }
        }catch(Exception e){
            logger.error("初始化连接配置失败", e);
            throw e;
        }
    }

    @Override
    public String getName(){
        return poolConnectFactory.getPoolName();
    }

    @Override
    public void start() {
        try {
            int l = poolConnectFactory.getConnectCount();
            for (int i = 0; i < l; i++) {
                pool.addObject();
                logger.info(String.format("连接池 %s %s 连接成功", poolConnectFactory.getPoolName(), i));
            }
        } catch (Exception e) {
            logger.info(String.format("连接池 %s 连接失败", poolConnectFactory.getPoolName()));
        }
    }

    @Override
    public void stop() {
        try {
            int l = poolConnectFactory.getConnectCount();
            for (int i = 0; i < l; i++) {
                poolConnectFactory.destroyObject(poolConnectFactory.wrap(pool.borrowObject()));
            }
            pool.close();
        } catch (Exception e) {
            logger.error("连接池关闭失败");
        }
    }

    @Override
    public void restart(){
        try{
            int l = poolConnectFactory.getConnectCount();
            for (int i = 0; i < l; i++) {
                poolConnectFactory.destroyObject(poolConnectFactory.wrap(pool.borrowObject()));
                pool.addObject();
            }
        }catch(Exception e){
            logger.error("连接池重启失败");
        }
    }

    public void sendBase(Object baseMsg){
        IoSession ioSession = this.getSessionConnect();
        if(ioSession != null){
            try{
                ioSession.write(baseMsg);
            }catch(Exception e){
                logger.error("发送失败, 失败原因{}", e);
            }finally {
                pool.returnObject(ioSession);
            }
        }
    }

    private IoSession getSessionConnect() {
        try{
            return pool.borrowObject();
        }catch(Exception e){
            logger.error("获取session失败");
            return null;
        }
    }
}
package com.caimao.jserver.mina;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class PoolConnectFactory extends BasePooledObjectFactory<IoSession>{
    private static final Logger logger = LoggerFactory.getLogger(PoolConnectFactory.class);

    private String poolName;
    private String hostName;
    private int port;
    private int connectCount;
    private long connectTimeoutMillis = 30000;
    private long writeTimeoutMillis = 30000;
    private int idleTime = 20;
    private ProtocolCodecFilter protocolCodecFilter;
    private IoHandler ioHandler;
    private NioSocketConnector connector;
    private String userName;
    private String password;
    private Boolean isDestroy = false;
    private Boolean needReconnect = false;

    @Override
    public IoSession create() throws Exception {
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(connectTimeoutMillis);
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, idleTime);
        connector.getSessionConfig().setTcpNoDelay(true);
        connector.getSessionConfig().setReceiveBufferSize(1024 * 10000);
        connector.getSessionConfig().setMaxReadBufferSize(1024 * 10000);
        connector.getSessionConfig().setReadBufferSize(1024 * 10000);
        if(needReconnect){
            connector.getFilterChain().addFirst("reconnect", new IoFilterAdapter(){
                public void sessionClosed(NextFilter nextFilter, IoSession ioSession) throws Exception{
                    for(;;){
                        try{
                            if(isDestroy) break;
                            Thread.sleep(10000);
                            ConnectFuture future = connector.connect(new InetSocketAddress(hostName, port));
                            future.awaitUninterruptibly(connectTimeoutMillis);
                            ioSession = future.getSession();
                            ioSession.setAttribute("connectName", poolName);
                            ioSession.setAttribute("userName", userName);
                            ioSession.setAttribute("password", password);
                            if(ioSession.isConnected()){
                                logger.info("断线重连["+ hostName +":"+ port+"]成功");
                                break;
                            }
                        }catch(Exception ex){
                            logger.info(hostName + ":" + port + "重连服务器登录失败,10秒再连接一次:" + ex.getMessage());
                        }
                    }
                }
            });
        }

        connector.getFilterChain().addLast("codec", protocolCodecFilter);
        connector.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        connector.setHandler(ioHandler);
        ConnectFuture future = connector.connect(new InetSocketAddress(hostName, port));
        boolean completed = future.awaitUninterruptibly(connectTimeoutMillis);
        if(!completed){
            logger.error("超时");
        }
        IoSession ioSession = future.getSession();
        ioSession.setAttribute("connectName", poolName);
        ioSession.setAttribute("userName", userName);
        ioSession.setAttribute("password", password);
        return ioSession;
    }

    @Override
    public PooledObject<IoSession> wrap(IoSession ioSession) {
        return new DefaultPooledObject<IoSession>(ioSession);
    }

    @Override
    public void destroyObject(PooledObject p) throws Exception{
        isDestroy = true;
        IoSession ioSession = (IoSession)p.getObject();
        ioSession.close(true);
        connector.dispose();
    }

    @Override
    public boolean validateObject(PooledObject p) {
        if (p.getObject() instanceof IoSession){
            IoSession ioSession = (IoSession)p.getObject();
            return (ioSession.isConnected() && !ioSession.isClosing());
        }else{
            return false;
        }
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectCount() {
        return connectCount;
    }

    public void setConnectCount(int connectCount) {
        this.connectCount = connectCount;
    }

    public NioSocketConnector getConnector() {
        return connector;
    }

    public void setConnector(NioSocketConnector connector) {
        this.connector = connector;
    }

    public long getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(long connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public long getWriteTimeoutMillis() {
        return writeTimeoutMillis;
    }

    public void setWriteTimeoutMillis(long writeTimeoutMillis) {
        this.writeTimeoutMillis = writeTimeoutMillis;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }

    public ProtocolCodecFilter getProtocolCodecFilter() {
        return protocolCodecFilter;
    }

    public void setProtocolCodecFilter(ProtocolCodecFilter protocolCodecFilter) {
        this.protocolCodecFilter = protocolCodecFilter;
    }

    public IoHandler getIoHandler() {
        return ioHandler;
    }

    public void setIoHandler(IoHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getNeedReconnect() {
        return needReconnect;
    }

    public void setNeedReconnect(Boolean needReconnect) {
        this.needReconnect = needReconnect;
    }
}

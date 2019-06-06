package com.caimao.gjs.server.trade.mina.sjsOpen;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SJSMinaOpenHandler extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SJSMinaOpenHandler.class);
    private Map<String, SJSOpenMsg> receiveMap = new ConcurrentHashMap<>();

    @Override
    public void sessionCreated(IoSession session) throws Exception {

    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if(message instanceof SJSOpenMsg){
            SJSOpenMsg sjsOpenMsg = (SJSOpenMsg)message;
            receiveMap.put(sjsOpenMsg.getSequence(), sjsOpenMsg);
        }else{
            System.out.println("返回其他信息：" + message.toString());
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

    }

    public SJSOpenMsg getReceive(String seq) {
        return receiveMap.remove(seq);
    }
}

package com.caimao.gjs.server.trade.mina.njsHQ;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NJSHQMinaHandler extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NJSHQMinaHandler.class);

    @Override
    public void sessionCreated(IoSession session) throws Exception {

    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        String userName = session.getAttribute("userName").toString();
        String password = session.getAttribute("password").toString();

        NJSHQMsg njshqMsg = new NJSHQMsg();
        njshqMsg.setDataLen(20);
        njshqMsg.setMainType(96);
        njshqMsg.setSubType(1);
        njshqMsg.setState("00000");
        njshqMsg.setNext("N");
        njshqMsg.setSign("0");
        njshqMsg.setCompress("0");
        njshqMsg.setKeep("0000");
        njshqMsg.setTraderId("0000000000000000");

        String loginData = String.format("%-4s", "") + (userName.length()>8?userName.substring(0, 8):String.format("%-8s", userName)) + (password.length()>8?password.substring(0, 8):String.format("%-8s", password));
        njshqMsg.setData(loginData);

        session.write(njshqMsg);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        if (status == IdleStatus.BOTH_IDLE) {
            NJSHQMsg njshqMsg = new NJSHQMsg();
            njshqMsg.setDataLen(0);
            njshqMsg.setMainType(0);
            njshqMsg.setSubType(0);
            njshqMsg.setState("00000");
            njshqMsg.setNext("N");
            njshqMsg.setSign("0");
            njshqMsg.setCompress("0");
            njshqMsg.setKeep("0000");
            njshqMsg.setTraderId("0000000000000000");
            session.write(njshqMsg);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if(message instanceof NJSHQMsg){
            NJSHQMsg njshqMsg = (NJSHQMsg) message;
            System.out.println(njshqMsg.getData());
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

    }
}

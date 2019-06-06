package com.caimao.gjs.server.trade.mina.njs;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NJSMinaHandler extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NJSMinaHandler.class);
    private Map<String, NJSTradeMsg> receiveMap = new ConcurrentHashMap<>();

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        session.write("015000000000N12345678911");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        if (status == IdleStatus.BOTH_IDLE) {
            session.write("015001000000N12345678911");
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if(message instanceof NJSTradeMsg){
            NJSTradeMsg njsTradeMsg = (NJSTradeMsg)message;

            switch (njsTradeMsg.getAdapter()) {
                case "015000":
                    logger.info("注册中间件成功");
                    break;
                case "015001":
                    //logger.info("心跳成功");
                    break;
                default:
                    receiveMap.put(njsTradeMsg.getSequence(), njsTradeMsg);
                    break;
            }
        }else{
            System.out.println("返回其他信息：" + message.toString());
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

    }

    public NJSTradeMsg getReceive(String seq) {
        return receiveMap.remove(seq);
    }
}

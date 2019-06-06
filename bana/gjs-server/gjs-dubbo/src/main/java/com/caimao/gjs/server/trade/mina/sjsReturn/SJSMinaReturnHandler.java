package com.caimao.gjs.server.trade.mina.sjsReturn;

import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.gjs.server.service.TradeServiceImpl;
import com.caimao.gjs.server.utils.SpringUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SJSMinaReturnHandler extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SJSMinaReturnHandler.class);

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        session.write("00000107term_type=05#user_key=97641239412631#user_type=3#user_id=1080012271#branch_id=B0077001#lan_ip=10.14.14.133#");
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
            session.write("00000011ConnectTest");
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if(message instanceof String){
            TradeServiceImpl tradeService = (TradeServiceImpl) SpringUtil.getBean("tradeService");
            tradeService.pushMatchMsg(EGJSExchange.SJS.getCode(), message.toString());
        }else{
            System.out.println("返回其他信息：" + message.toString());
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

    }
}

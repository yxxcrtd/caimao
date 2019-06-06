package com.caimao.gjs.server.trade.mina.njsReturn;

import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.gjs.server.service.AccountServiceImpl;
import com.caimao.gjs.server.service.TradeServiceImpl;
import com.caimao.gjs.server.utils.RedisUtils;
import com.caimao.gjs.server.utils.SpringUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NJSReturnMinaHandler extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NJSReturnMinaHandler.class);

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
        if (status == IdleStatus.BOTH_IDLE) {
            session.write("1234567891234567890");
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String receiveData = message.toString();
        if(receiveData.length() < 15) return;
        switch (receiveData) {
            case "010001000000N12345678910":
                logger.info("回报连接成功");
                break;
            case "1234567891234567890":
                try{
                    RedisUtils redisUtils = (RedisUtils) SpringUtil.getBean("RedisUtils");
                    redisUtils.set(0, "NJS_return_heart", "1", 60L);
                }catch(Exception e){
                    logger.error("redis 心跳写入失败");
                }
                //logger.info("回报心跳成功");
                break;
            default:
                logger.info("回报接收的信息：" + receiveData);
                this.parseReceiveData(receiveData);
                this.sendReceive(session, receiveData.substring(6, 19));
                break;
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

    }

    private void sendReceive(IoSession session, String message) throws Exception{
        session.write(message);
        logger.info("发送回报接收信息");
    }

    private void parseReceiveData(String receiveData) throws Exception{
        String feedBackType = receiveData.substring(5, 6);
        switch (feedBackType) {
            case "A": //委托反馈
                break;
            case "B": //成交反馈
                TradeServiceImpl tradeService = (TradeServiceImpl) SpringUtil.getBean("tradeService");
                tradeService.pushMatchMsg(EGJSExchange.NJS.getCode(), receiveData);
                break;
            case "C": //签约反馈
                AccountServiceImpl accountService = (AccountServiceImpl) SpringUtil.getBean("accountService");
                accountService.pushMsgIsSign(EGJSExchange.NJS.getCode(), receiveData);
                break;
        }
    }
}

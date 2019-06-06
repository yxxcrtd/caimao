package com.caimao.gjs.server.trade.mina.sjs;

import com.caimao.gjs.server.trade.mina.BaseMinaPlugin;
import com.caimao.gjs.server.utils.Sequence;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SJSMinaPluginTrade extends BaseMinaPlugin{
    private static final Logger logger = LoggerFactory.getLogger(SJSMinaPluginTrade.class);

    private ProtocolCodecFilter protocolCodecFilter = new ProtocolCodecFilter(new SJSProtocolTrade());
    private SJSMinaHandler ioHandler = new SJSMinaHandler();
    private Sequence sequence = new Sequence(10);

    @Override
    protected ProtocolCodecFilter getProtocolCodecFilter() {
        return protocolCodecFilter;
    }

    @Override
    protected IoHandler getHandler() {
        return ioHandler;
    }

    @Override
    public void send(String adapter, String message) {
        try{
            SJSTradeMsg sjsTradeMsgSend = new SJSTradeMsg();
            sjsTradeMsgSend.setAdapter(adapter);
            sjsTradeMsgSend.setSequence(sequence.nextStr());
            sjsTradeMsgSend.setData(message);
            sendBase(sjsTradeMsgSend);
        }catch(Exception e){
            logger.error("发送失败, 失败原因{}", e);
        }
    }

    @Override
    public Object sendAndRev(String adapter, Object message) {
        try{
            Object[] messageO = (Object[]) message;
            long currentTime = System.currentTimeMillis();

            String sequenceStr = sequence.nextStr();

            SJSTradeMsg sjsTradeMsgSend = new SJSTradeMsg();
            sjsTradeMsgSend.setAdapter(adapter);
            sjsTradeMsgSend.setSequence(sequenceStr);
            sjsTradeMsgSend.setUserId((String)messageO[0]);
            sjsTradeMsgSend.setData(messageO[1]);
            if(!adapter.equals("309901")){
                sjsTradeMsgSend.setEncrypt(true);
            }
            sendBase(sjsTradeMsgSend);

            SJSTradeMsg sjsTradeMsg = ioHandler.getReceive(sequenceStr);
            while (sjsTradeMsg == null) {
                if (System.currentTimeMillis() - currentTime > 30000) {
                    logger.error("socket receive time out");
                    return null;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception ignored) {}
                sjsTradeMsg = ioHandler.getReceive(sequenceStr);
            }
            return sjsTradeMsg;
        }catch(Exception e){
            logger.error("发送失败, 失败原因{}", e);
            return null;
        }
    }
}

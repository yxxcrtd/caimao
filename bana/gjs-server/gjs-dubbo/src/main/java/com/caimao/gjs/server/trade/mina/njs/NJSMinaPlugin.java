package com.caimao.gjs.server.trade.mina.njs;

import com.caimao.gjs.server.trade.mina.BaseMinaPlugin;
import com.caimao.gjs.server.utils.Sequence;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NJSMinaPlugin extends BaseMinaPlugin{
    private static final Logger logger = LoggerFactory.getLogger(NJSMinaPlugin.class);

    private ProtocolCodecFilter protocolCodecFilter = new ProtocolCodecFilter(new NJSProtocol());
    private NJSMinaHandler ioHandler = new NJSMinaHandler();
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
            NJSTradeMsg njsTradeMsg = new NJSTradeMsg();
            njsTradeMsg.setAdapter(adapter);
            njsTradeMsg.setSequence(sequence.nextStr());
            njsTradeMsg.setData(message);
            sendBase(njsTradeMsg);
        }catch(Exception e){
            logger.error("发送失败, 失败原因{}", e);
        }
    }

    @Override
    public Object sendAndRev(String adapter, Object message) {
        try{
            long currentTime = System.currentTimeMillis();

            String sequenceStr = sequence.nextStr();

            NJSTradeMsg njsTradeMsgSend = new NJSTradeMsg();
            njsTradeMsgSend.setAdapter(adapter);
            njsTradeMsgSend.setSequence(sequenceStr);
            njsTradeMsgSend.setData(message.toString());
            sendBase(njsTradeMsgSend);

            NJSTradeMsg njsTradeMsg = ioHandler.getReceive(sequenceStr);
            while (njsTradeMsg == null) {
                if (System.currentTimeMillis() - currentTime > 30000) {
                    logger.error("socket receive time out");
                    return null;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception ignored) {}
                njsTradeMsg = ioHandler.getReceive(sequenceStr);
            }
            return njsTradeMsg;
        }catch(Exception e){
            logger.error("发送失败, 失败原因{}", e);
            return null;
        }
    }
}

package com.caimao.gjs.server.trade.mina.sjsOpen;

import com.caimao.gjs.server.trade.mina.BaseMinaPlugin;
import com.caimao.gjs.server.utils.Sequence;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SJSMinaPluginOpen extends BaseMinaPlugin{
    private static final Logger logger = LoggerFactory.getLogger(SJSMinaPluginOpen.class);

    private ProtocolCodecFilter protocolCodecFilter = new ProtocolCodecFilter(new SJSProtocolOpen());
    private SJSMinaOpenHandler ioHandler = new SJSMinaOpenHandler();
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
            SJSOpenMsg sjsOpenMsgSend = new SJSOpenMsg();
            sjsOpenMsgSend.setAdapter(adapter);
            sjsOpenMsgSend.setSequence(sequence.nextStr());
            sjsOpenMsgSend.setData(message);
            sendBase(sjsOpenMsgSend);
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

            SJSOpenMsg sjsOpenMsgSend = new SJSOpenMsg();
            sjsOpenMsgSend.setAdapter(adapter);
            sjsOpenMsgSend.setSequence(sequenceStr);
            sjsOpenMsgSend.setData(messageO[1]);
            sendBase(sjsOpenMsgSend);

            SJSOpenMsg sjsOpenMsg = ioHandler.getReceive(sequenceStr);
            while (sjsOpenMsg == null) {
                if (System.currentTimeMillis() - currentTime > 30000) {
                    logger.error("socket receive time out");
                    return null;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception ignored) {}
                sjsOpenMsg = ioHandler.getReceive(sequenceStr);
            }
            return sjsOpenMsg;
        }catch(Exception e){
            logger.error("发送失败, 失败原因{}", e);
            return null;
        }
    }
}

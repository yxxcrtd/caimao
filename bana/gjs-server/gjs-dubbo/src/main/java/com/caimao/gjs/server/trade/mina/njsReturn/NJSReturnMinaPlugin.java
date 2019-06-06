package com.caimao.gjs.server.trade.mina.njsReturn;

import com.caimao.gjs.server.trade.mina.BaseMinaPlugin;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class NJSReturnMinaPlugin extends BaseMinaPlugin{
    private static final Logger logger = LoggerFactory.getLogger(NJSReturnMinaPlugin.class);

    private ProtocolCodecFilter protocolCodecFilter = new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("GBK"), LineDelimiter.CRLF.getValue(), LineDelimiter.CRLF.getValue()));
    private NJSReturnMinaHandler ioHandler = new NJSReturnMinaHandler();

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
            sendBase(message);
        }catch(Exception e){
            logger.error("发送失败, 失败原因{}", e);
        }
    }

    @Override
    public Object sendAndRev(String adapter, Object message) {
        try{
            return null;
        }catch(Exception e){
            logger.error("发送失败, 失败原因{}", e);
            return null;
        }
    }
}

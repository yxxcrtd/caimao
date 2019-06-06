package com.caimao.gjs.server.trade.mina.sjsReturn;

import com.caimao.gjs.server.trade.mina.BaseMinaPlugin;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SJSMinaPluginReturn extends BaseMinaPlugin{
    private static final Logger logger = LoggerFactory.getLogger(SJSMinaPluginReturn.class);

    private ProtocolCodecFilter protocolCodecFilter = new ProtocolCodecFilter(new SJSProtocolReturn());
    private SJSMinaReturnHandler ioHandler = new SJSMinaReturnHandler();

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

    }

    @Override
    public Object sendAndRev(String adapter, Object message) {
        return null;
    }
}

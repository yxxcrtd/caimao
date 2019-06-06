package com.caimao.jserver.mina.njsHQ;

import com.caimao.jserver.mina.BaseMinaPlugin;
import com.caimao.jserver.mina.PoolConnectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class NJSHQMinaPlugin extends BaseMinaPlugin {
    private static final Logger logger = LoggerFactory.getLogger(NJSHQMinaPlugin.class);

    private ProtocolCodecFilter protocolCodecFilter = new ProtocolCodecFilter(new NJSHQProtocol());
    private NJSHQMinaHandler ioHandler = new NJSHQMinaHandler();

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

package com.caimao.gjs.server.trade.mina.sjs;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

public class SJSProtocolTrade implements ProtocolCodecFactory {
    private static final Charset charset = Charset.forName("GBK");

    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return new SJSEncodeTrade();
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return new SJSDecodeTrade(charset);
    }
}

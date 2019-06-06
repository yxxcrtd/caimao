package com.caimao.gjs.server.trade.mina.njs;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

public class NJSProtocol implements ProtocolCodecFactory {
    private static final Charset charset = Charset.forName("GBK");

    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return new NJSEncode();
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return new NJSDecode(charset);
    }
}

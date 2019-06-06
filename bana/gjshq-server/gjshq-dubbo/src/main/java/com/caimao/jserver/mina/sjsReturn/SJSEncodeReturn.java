package com.caimao.jserver.mina.sjsReturn;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class SJSEncodeReturn extends ProtocolEncoderAdapter{
    private static final Logger logger = LoggerFactory.getLogger(SJSEncodeReturn.class);
    private Charset charset =  Charset.forName("GBK");

    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        IoBuffer buf = IoBuffer.allocate(message.toString().getBytes().length);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.setAutoExpand(true);
        byte[] putData;
        putData = message.toString().getBytes(charset);
        logger.info("sjs回报请求数据：" + message.toString());
        buf.put(putData);
        buf.flip();
        protocolEncoderOutput.write(buf);
        protocolEncoderOutput.flush();
        buf.free();
    }
}

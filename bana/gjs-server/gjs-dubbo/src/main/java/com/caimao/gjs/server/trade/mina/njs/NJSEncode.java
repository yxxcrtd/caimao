package com.caimao.gjs.server.trade.mina.njs;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;


public class NJSEncode extends ProtocolEncoderAdapter{
    private static final Logger logger = LoggerFactory.getLogger(NJSEncode.class);
    private Charset charset =  Charset.forName("GBK");

    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        IoBuffer buf = IoBuffer.allocate(message.toString().getBytes().length);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.setAutoExpand(true);
        byte[] putData;
        if(message instanceof NJSTradeMsg){
            NJSTradeMsg njsTradeMsg = (NJSTradeMsg) message;
            int dataLen = njsTradeMsg.getData().getBytes("GBK").length;
            String sendMsg = "016005" + String.format("%06d", dataLen + 24) + "NN" + njsTradeMsg.getSequence();
            sendMsg += njsTradeMsg.getAdapter() + String.format("%06d", dataLen) + "NN" + njsTradeMsg.getSequence();
            sendMsg += njsTradeMsg.getData();
            putData = sendMsg.getBytes("GBK");
            logger.info("请求数据：" + new String(putData));
        }else{
            putData = message.toString().getBytes(charset);
        }
        buf.put(putData);
        buf.flip();
        protocolEncoderOutput.write(buf);
        protocolEncoderOutput.flush();
        buf.free();
    }
}

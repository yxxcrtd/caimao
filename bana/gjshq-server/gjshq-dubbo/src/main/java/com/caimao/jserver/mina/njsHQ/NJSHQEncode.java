package com.caimao.jserver.mina.njsHQ;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;


public class NJSHQEncode extends ProtocolEncoderAdapter{
    private static final Logger logger = LoggerFactory.getLogger(NJSHQEncode.class);
    private Charset charset =  Charset.forName("GBK");

    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        IoBuffer buf = IoBuffer.allocate(message.toString().getBytes().length);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.setAutoExpand(true);
        byte[] putData;
        if(message instanceof NJSHQMsg){
            NJSHQMsg njshqMsg = (NJSHQMsg)message;

            String messageRight = njshqMsg.getState() + njshqMsg.getNext() + njshqMsg.getSign() + njshqMsg.getCompress() + njshqMsg.getKeep() + njshqMsg.getTraderId() + (njshqMsg.getData() == null?"":njshqMsg.getData());

            byte[] messageLeft = new byte[4];
            messageLeft[0] = (new Integer(0)).byteValue();
            messageLeft[1] = (new Integer(njshqMsg.getDataLen())).byteValue();
            messageLeft[2] = (new Integer(njshqMsg.getMainType())).byteValue();
            messageLeft[3] = (new Integer(njshqMsg.getSubType())).byteValue();

            putData = byteMerger(messageLeft, messageRight.getBytes());


        }else{
            putData = message.toString().getBytes(charset);
        }
        logger.info("请求数据：" + new String(putData));
        buf.put(putData);
        buf.flip();
        protocolEncoderOutput.write(buf);
        protocolEncoderOutput.flush();
        buf.free();
    }

    private static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}


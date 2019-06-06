package com.caimao.gjs.server.trade.mina.sjsReturn;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class SJSDecodeReturn extends CumulativeProtocolDecoder {
    private static final Logger logger = LoggerFactory.getLogger(SJSDecodeReturn.class);
    private Charset charset =  Charset.forName("GBK");

    public SJSDecodeReturn(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        try{
            ioBuffer.order(ByteOrder.LITTLE_ENDIAN);
            //判断是否有历史数据
            Integer newMsg = this.getNewMsg(ioSession);
            if(newMsg == null){
                //取报文头
                if(ioBuffer.remaining() >= 8){
                    byte[] headByte = new byte[8];
                    ioBuffer.get(headByte);
                    //System.out.println("头信息：" + new String(headByte));
                    int dataLen = Integer.parseInt(new String(headByte));
                    //判断长度是否为0
                    if(dataLen > 0){
                        //保存新数据
                        this.setNewMsg(ioSession, dataLen);
                    }else{
                        this.clearNewMsg(ioSession);
                        protocolDecoderOutput.write(dataLen);
                    }
                    return true;
                }
                return false;
            }

            //取报文体
            if(ioBuffer.remaining() >= newMsg){
                byte [] bodyByte = new byte[newMsg];
                ioBuffer.get(bodyByte);
                String data = new String(bodyByte, this.charset);
                //清除数据包
                this.clearNewMsg(ioSession);
                //处理数据
                //logger.info("接收数据：" + data);
                protocolDecoderOutput.write(data);
                return true;
            }
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    private Integer getNewMsg(IoSession ioSession) throws Exception{
        return (Integer)ioSession.getAttribute("sjs_open_new_msg");
    }

    private void setNewMsg(IoSession ioSession, Integer message) throws Exception{
        ioSession.setAttribute("sjs_open_new_msg", message);
    }

    private void clearNewMsg(IoSession ioSession) throws Exception{
        ioSession.removeAttribute("sjs_open_new_msg");
    }
}

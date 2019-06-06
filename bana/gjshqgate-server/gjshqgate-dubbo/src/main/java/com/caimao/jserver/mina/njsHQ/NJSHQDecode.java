package com.caimao.jserver.mina.njsHQ;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class NJSHQDecode extends CumulativeProtocolDecoder {
    private static final Logger logger = LoggerFactory.getLogger(NJSHQDecode.class);
    private Charset charset =  Charset.forName("GBK");

    public NJSHQDecode(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        try{
            ioBuffer.order(ByteOrder.LITTLE_ENDIAN);
            //判断是否有历史数据
            NJSHQMsg newMsg = this.getNewMsg(ioSession);
            if(newMsg == null){
                //取报文头
                if(ioBuffer.remaining() >= 32){
                    byte[] headByte = new byte[32];
                    ioBuffer.get(headByte);
                    //System.out.println("头信息：" + new String(headByte));
                    newMsg = new NJSHQMsg();
                    //设置基本信息
                    newMsg = this.readHead(newMsg, headByte);
                    //判断长度是否为0
                    if(newMsg.getDataLen() > 0){
                        //保存新数据
                        this.setNewMsg(ioSession, newMsg);
                        //System.out.println("解析新头：" + ToStringBuilder.reflectionToString(newMsg));
                    }else{
                        this.clearNewMsg(ioSession);
                        protocolDecoderOutput.write(newMsg);
                    }
                    return true;
                }
                return false;
            }

            //取报文体
            if(ioBuffer.remaining() >= newMsg.getDataLen()){
                byte [] bodyByte = new byte[newMsg.getDataLen()];
                ioBuffer.get(bodyByte);
                //保存体数据
                this.saveData(ioSession, new String(bodyByte, this.charset));
                //判断是否有下一包
                if(newMsg.getNext().equals("N")){
                    NJSHQMsg resultData = this.fixData(ioSession);
                    protocolDecoderOutput.write(resultData);
                }
                //清除数据包
                this.clearNewMsg(ioSession);
                return true;
            }
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    private NJSHQMsg getNewMsg(IoSession ioSession) throws Exception{
        return (NJSHQMsg)ioSession.getAttribute("njshq_new_msg");
    }

    private void setNewMsg(IoSession ioSession, NJSHQMsg message) throws Exception{
        ioSession.setAttribute("njshq_new_msg", message);
    }

    private void clearNewMsg(IoSession ioSession) throws Exception{
        ioSession.removeAttribute("njshq_new_msg");
    }


    private NJSHQMsg readHead(NJSHQMsg NJSHQMsg, byte[] headByte) throws Exception{
        NJSHQMsg.setDataLen(((headByte[0] & 0xff) << 8) | (headByte[1] & 0xff));
        NJSHQMsg.setMainType(headByte[2] & 0xff);
        NJSHQMsg.setSubType(headByte[3] & 0xff);
        NJSHQMsg.setNext(new String(headByte, 9, 1, this.charset));
        return NJSHQMsg;
    }

    private void saveData(IoSession ioSession, String dataStr) throws Exception{
        Object oldData = ioSession.getAttribute("njs_hq_msg");
        String oldDataStr = oldData == null?"":oldData.toString();
        ioSession.setAttribute("njs_hq_msg", oldDataStr + dataStr);
    }


    private NJSHQMsg fixData(IoSession ioSession) throws Exception{
        Object oldData = ioSession.getAttribute("njs_hq_msg");

        String oldDataStr = oldData == null?"":oldData.toString();
        if(oldDataStr.equals("")){
            return null;
        }else{
            NJSHQMsg njshqMsgOld = (NJSHQMsg)ioSession.getAttribute("njshq_new_msg");
            NJSHQMsg njshqMsg = new NJSHQMsg();
            njshqMsg.setMainType(njshqMsgOld.getMainType());
            njshqMsg.setSubType(njshqMsg.getSubType());
            byte[] oldDataStrByte = oldDataStr.getBytes(this.charset);
            njshqMsg.setData(new String(oldDataStrByte, this.charset));

            ioSession.removeAttribute("njs_hq_msg");
            return njshqMsg;
        }
    }
}

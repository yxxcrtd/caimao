package com.caimao.gjs.server.trade.mina.sjsOpen;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class SJSDecodeOpen extends CumulativeProtocolDecoder {
    private static final Logger logger = LoggerFactory.getLogger(SJSDecodeOpen.class);
    private Charset charset =  Charset.forName("GBK");

    public SJSDecodeOpen(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        try{
            ioBuffer.order(ByteOrder.LITTLE_ENDIAN);
            //判断是否有历史数据
            SJSOpenMsg newMsg = this.getNewMsg(ioSession);
            if(newMsg == null){
                //取报文头
                if(ioBuffer.remaining() >= 8){
                    byte[] headByte = new byte[8];
                    ioBuffer.get(headByte);
                    System.out.println("头信息：" + new String(headByte));
                    newMsg = new SJSOpenMsg();
                    newMsg.setDataLen(Integer.parseInt(new String(headByte)));
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
                newMsg.setData(new String(bodyByte, this.charset));
                //清除数据包
                this.clearNewMsg(ioSession);
                //处理数据
                SJSOpenMsg resultData = this.fixData(newMsg);
                logger.info("接收数据：" + (resultData == null?"":resultData.getData()));
                protocolDecoderOutput.write(resultData);
                return true;
            }
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    private SJSOpenMsg getNewMsg(IoSession ioSession) throws Exception{
        return (SJSOpenMsg)ioSession.getAttribute("sjs_open_new_msg");
    }

    private void setNewMsg(IoSession ioSession, SJSOpenMsg message) throws Exception{
        ioSession.setAttribute("sjs_open_new_msg", message);
    }

    private void clearNewMsg(IoSession ioSession) throws Exception{
        ioSession.removeAttribute("sjs_open_new_msg");
    }

    private String getSeq(String xml, String signStr) {
        int start = xml.indexOf(signStr);
        if (start != 0) {
            return xml.substring(start + signStr.length(), start + signStr.length() + 10);
        }
        return null;
    }

    private SJSOpenMsg fixData(SJSOpenMsg data) throws Exception{
        data.setAdapter(getSeq(data.getData().toString(), "<h_exch_code>"));
        data.setSequence(getSeq(data.getData().toString(), "<h_bk_seq_no>"));
        return data;
    }
}

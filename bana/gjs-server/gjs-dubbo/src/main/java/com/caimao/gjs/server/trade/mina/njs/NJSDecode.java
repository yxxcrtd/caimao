package com.caimao.gjs.server.trade.mina.njs;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class NJSDecode extends CumulativeProtocolDecoder {
    private static final Logger logger = LoggerFactory.getLogger(NJSDecode.class);
    private Charset charset =  Charset.forName("GBK");

    public NJSDecode(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        try{
            ioBuffer.order(ByteOrder.LITTLE_ENDIAN);
            //判断是否有历史数据
            NJSTradeMsg newMsg = this.getNewMsg(ioSession);
            if(newMsg == null){
                //取报文头
                if(ioBuffer.remaining() >= 24){
                    byte[] headByte = new byte[24];
                    ioBuffer.get(headByte);
                    System.out.println("头信息：" + new String(headByte));
                    newMsg = new NJSTradeMsg();
                    //设置基本信息
                    newMsg = this.readHead(newMsg, headByte);
                    //判断长度是否为0
                    if(newMsg.getDataLen() > 0){
                        //保存新数据
                        this.setNewMsg(ioSession, newMsg);
                        System.out.println("解析新头：" + ToStringBuilder.reflectionToString(newMsg));
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
                this.saveData(ioSession, newMsg.getSequence(), new String(bodyByte, this.charset));
                //清除数据包
                this.clearNewMsg(ioSession);
                //判断是否有下一包
                if(newMsg.getNext().equals("N")){
                    NJSTradeMsg resultData = this.fixData(ioSession, newMsg.getSequence());
                    logger.info("接收数据：" + (resultData == null?"":resultData.getData()));
                    protocolDecoderOutput.write(resultData);
                }
                return true;
            }
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    private NJSTradeMsg getNewMsg(IoSession ioSession) throws Exception{
        return (NJSTradeMsg)ioSession.getAttribute("njs_new_msg");
    }

    private void setNewMsg(IoSession ioSession, NJSTradeMsg message) throws Exception{
        ioSession.setAttribute("njs_new_msg", message);
    }

    private void clearNewMsg(IoSession ioSession) throws Exception{
        ioSession.removeAttribute("njs_new_msg");
    }


    private NJSTradeMsg readHead(NJSTradeMsg njsTradeMsg, byte[] headByte) throws Exception{
        njsTradeMsg.setAdapter(new String(headByte, 0, 6, this.charset));
        njsTradeMsg.setDataLen(Integer.parseInt(new String(headByte, 6, 6, this.charset)));
        njsTradeMsg.setNext(new String(headByte, 12, 1, this.charset));
        njsTradeMsg.setCompress(new String(headByte, 13, 1, this.charset));
        njsTradeMsg.setSequence(new String(headByte, 14, 10, this.charset));
        return njsTradeMsg;
    }

    private void saveData(IoSession ioSession, String sequence, String dataStr) throws Exception{
        Object oldData = ioSession.getAttribute("njs_trade_msg" + sequence);
        String oldDataStr = oldData == null?"":oldData.toString();
        ioSession.setAttribute("njs_trade_msg" + sequence, oldDataStr + dataStr);
    }


    private NJSTradeMsg fixData(IoSession ioSession, String sequence) throws Exception{
        Object oldData = ioSession.getAttribute("njs_trade_msg" + sequence);

        String oldDataStr = oldData == null?"":oldData.toString();
        if(oldDataStr.equals("")){
            return null;
        }else{
            NJSTradeMsg njsTradeMsg = new NJSTradeMsg();
            byte[] oldDataStrByte = oldDataStr.getBytes(this.charset);
            njsTradeMsg.setAdapter(new String(oldDataStrByte, 0, 6, this.charset));
            njsTradeMsg.setDataLen(Integer.parseInt(new String(oldDataStrByte, 6, 6, this.charset)));
            njsTradeMsg.setNext(new String(oldDataStrByte, 12, 1, this.charset));
            njsTradeMsg.setCompress(new String(oldDataStrByte, 13, 1, this.charset));
            njsTradeMsg.setSequence(new String(oldDataStrByte, 14, 10, this.charset));
            njsTradeMsg.setData(new String(oldDataStrByte, 24, oldDataStrByte.length - 24, this.charset));
            ioSession.removeAttribute("njs_trade_msg" + njsTradeMsg.getSequence());
            return njsTradeMsg;
        }
    }
}

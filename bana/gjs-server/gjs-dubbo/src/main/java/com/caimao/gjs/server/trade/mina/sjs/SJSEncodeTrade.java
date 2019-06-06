package com.caimao.gjs.server.trade.mina.sjs;

import com.caimao.gjs.server.utils.Constants;
import com.caimao.gjs.server.utils.XMLForBean;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;


public class SJSEncodeTrade extends ProtocolEncoderAdapter{
    private static final Logger logger = LoggerFactory.getLogger(SJSEncodeTrade.class);
    private Charset charset =  Charset.forName("GBK");
    private static byte[] DES_KEY = "ABCDEF0123456789abcdef11".getBytes();
    private static String BASE_HEAD = "<h_bank_no>" + Constants.SJS_BANK_NO + "</h_bank_no><h_branch_id>" + Constants.SJS_BRANCH_ID + "</h_branch_id><h_fact_date></h_fact_date><h_fact_time></h_fact_time><h_exch_date></h_exch_date><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg>";


    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        IoBuffer buf = IoBuffer.allocate(message.toString().getBytes().length);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.setAutoExpand(true);
        byte[] putData;
        if(message instanceof SJSTradeMsg){
            SJSTradeMsg sjsTradeMsg = (SJSTradeMsg) message;
            StringBuilder xml = new StringBuilder(BASE_HEAD);
            xml.append("<h_exch_code>").append(sjsTradeMsg.getAdapter()).append("</h_exch_code>");
            xml.append("<h_user_id>").append(sjsTradeMsg.getUserId()).append("</h_user_id>");
            xml.append("<h_serial_no>").append(sjsTradeMsg.getSequence()).append("</h_serial_no>");

            String xmlSend = XMLForBean.toXML(sjsTradeMsg.getData(), xml.toString());
            System.out.println("请求数据:" + xmlSend);

            byte[] A = StringUtil.StringToBytes(xmlSend);

            if(sjsTradeMsg.isEncrypt()){
                A = DESedeUtil.encrypt(StringUtil.StringToBytes(xmlSend), DES_KEY);
            }
            byte[] B = String.format("%0" + 8 + "d", A.length).getBytes(charset);

            putData = byteMerger(B, A);
        }else{
            putData = message.toString().getBytes(charset);
        }
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

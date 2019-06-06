package com.caimao.gjs.server.trade.mina.sjsOpen;

import com.caimao.gjs.server.utils.Constants;
import com.caimao.gjs.server.utils.XMLForBean;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class SJSEncodeOpen extends ProtocolEncoderAdapter{
    private static final Logger logger = LoggerFactory.getLogger(SJSEncodeOpen.class);
    private Charset charset =  Charset.forName("GBK");
    private static String BASE_HEAD = "<h_bank_no>"+ Constants.SJS_BANK_NO +"</h_bank_no>" +
            "<h_term_type>" + Constants.SJS_TERM_TYPE + "</h_term_type>" +
            "<h_teller_id>" + Constants.SJS_OP_USER + "</h_teller_id>" +
            "<h_teller_id_1></h_teller_id_1>" +
            "<h_teller_id_2></h_teller_id_2>" +
            "<h_branch_id>" + Constants.SJS_BRANCH_ID + "</h_branch_id>" +
            "<h_work_date></h_work_date><h_exch_date></h_exch_date>";

    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        IoBuffer buf = IoBuffer.allocate(message.toString().getBytes().length);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.setAutoExpand(true);
        byte[] putData;
        if(message instanceof SJSOpenMsg){
            SJSOpenMsg sjsOpenMsg = (SJSOpenMsg) message;
            StringBuilder xml = new StringBuilder(BASE_HEAD);
            xml.append("<h_exch_code>").append(sjsOpenMsg.getAdapter()).append("</h_exch_code>");
            xml.append("<h_bk_seq_no>").append(sjsOpenMsg.getSequence()).append("</h_bk_seq_no>");

            String xmlSend = XMLForBean.toXML(sjsOpenMsg.getData(), xml.toString());

            byte[] A = StringUtil.StringToBytes(xmlSend);
            byte[] B = String.format("%0" + 8 + "d", A.length).getBytes(charset);

            putData = byteMerger(B, A);

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

    private static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}

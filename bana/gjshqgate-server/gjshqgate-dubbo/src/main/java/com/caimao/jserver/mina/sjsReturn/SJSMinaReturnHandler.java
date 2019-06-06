package com.caimao.jserver.mina.sjsReturn;

import com.caimao.hq.api.entity.SJSSnapshot;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.core.NJSDataHandleThread;
import com.caimao.hq.core.ProcessorManager;
import com.caimao.hq.core.SJSDataHandleThread;
import com.caimao.hq.core.SJSQuoteParseUtils;
import com.caimao.hq.service.HQServiceImpl;
import com.caimao.hq.utils.DateUtils;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.SpringUtil;
import com.caimao.jserver.mina.MinaServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SJSMinaReturnHandler extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SJSMinaReturnHandler.class);
    private JRedisUtil jredisUtil=null;
    public HQServiceImpl hqService=null;
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        session.write("00000107term_type=05#user_key=97641239412631#user_type=3#user_id=1080012271#branch_id=B0077001#lan_ip=10.14.14.133#");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        jredisUtil =(JRedisUtil) SpringUtil.getBean("jredisUtil");
        hqService=(HQServiceImpl)SpringUtil.getBean("hqService");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        if (status == IdleStatus.BOTH_IDLE) {
            session.write("00000011ConnectTest");
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if(message instanceof String){
            String strMessage=(String)message;
            List<Snapshot> list= SJSQuoteParseUtils.convert(strMessage);
            if(null!=list){
                logger.info("上金所："+list.size());
            }
            List<Snapshot> removeRepeatList=removeRepeat(list);
            if(null!=removeRepeatList&&removeRepeatList.size()>0){
               callCreateCandle(removeRepeatList);
            }
            //insertRedisCacheData(list);
        }else{
            logger.error("上金所解析数据格式异常,非string编码： +message.toString()");
        }
    }

    //把获取的数据放入到redis缓存
    private  void insertRedisCacheData(  List<Snapshot> removeRepeatList){
        if(null!=removeRepeatList&&removeRepeatList.size()>0){
            if(null!=hqService){
                //logger.debug("insert SJS:"+removeRepeatList);
                hqService.insertRedisSnapshotAll(removeRepeatList);
            }else{
                logger.error("插入redis 行情错误：SpringUtil.getBean(\"hqService\")  is null");
            }
        }
    }

    //直接调用生成K线
    private  void callCreateCandle(  List<Snapshot> removeRepeatList){
        SJSDataHandleThread sjsDataHandleThread=(SJSDataHandleThread)SpringUtil.getBean("sjsDataHandleThread");
        sjsDataHandleThread.setMessage(null);
        sjsDataHandleThread.setMessage(removeRepeatList);
        ProcessorManager.push(sjsDataHandleThread);

    }
    private Boolean  isRepeat(Snapshot snapshot){

        Boolean isRepeat=false;
        String redisIsRepeatKey=getIsRepeatKey(snapshot);
        if(!StringUtils.isBlank(redisIsRepeatKey)) {

            if (jredisUtil.exists(redisIsRepeatKey)){
                isRepeat=true;
            }
        }
        return isRepeat;
    }
    private DecimalFormat decimalFormat = new DecimalFormat("###0.00");//格式化设置
    private String getIsRepeatKey(Snapshot snapshot){
        StringBuffer  sb=new StringBuffer();
        if(null!=snapshot) {
            SJSSnapshot temp = (SJSSnapshot) snapshot;
            if (null != snapshot) {

                if(StringUtils.isBlank(temp.getApdRecvTime())){
                    sb.append(temp.getTradeDate());
                }else{
                    sb.append(temp.getApdRecvTime());
                }
                sb.append(temp.getExchange());
                sb.append(temp.getProdCode());
                sb.append(decimalFormat.format(temp.getBusinessAmount()));

            }

        }
        return sb.toString();
    }
    private synchronized List<Snapshot> removeRepeat(List<Snapshot> list){

        List<Snapshot> listResult= Collections.synchronizedList(new ArrayList());
        if(null!=list&&list.size()>0){

            for(Snapshot snapshot:list){

                if(snapshot.getBusinessAmount()==0||snapshot.getOpenPx()==0||snapshot.getHighPx()==0||snapshot.getLowPx()==0){
                    continue;
                }
                if(!isRepeat(snapshot)){

                    listResult.add(snapshot);
                    jredisUtil.setex(getIsRepeatKey(snapshot), "true", 36000000);//如果不重复，就添加
                }
            }

        }
        return listResult;
    }
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

    }
}

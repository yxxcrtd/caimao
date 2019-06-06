package com.caimao.jserver.mina.njsHQ;

import com.caimao.hq.api.entity.NJSSnapshot;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.core.NJSDataHandleThread;
import com.caimao.hq.core.NJSQuoteParseUtils;
import com.caimao.hq.core.ProcessorManager;
import com.caimao.hq.service.HQServiceImpl;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.*;

public class NJSHQMinaHandler extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NJSHQMinaHandler.class);
    private JRedisUtil jredisUtil = null;
    public HQServiceImpl hqService = null;

    @Override
    public void sessionCreated(IoSession session) throws Exception {

    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        String userName = session.getAttribute("userName").toString();
        String password = session.getAttribute("password").toString();
        jredisUtil = (JRedisUtil) SpringUtil.getBean("jredisUtil");
        hqService = (HQServiceImpl) SpringUtil.getBean("hqService");
        NJSHQMsg njshqMsg = new NJSHQMsg();
        njshqMsg.setDataLen(20);
        njshqMsg.setMainType(96);
        njshqMsg.setSubType(1);
        njshqMsg.setState("00000");
        njshqMsg.setNext("N");
        njshqMsg.setSign("0");
        njshqMsg.setCompress("0");
        njshqMsg.setKeep("0000");
        njshqMsg.setTraderId("0000000000000000");
        String loginData = String.format("%-4s", "") + (userName.length() > 8 ? userName.substring(0, 8) : String.format("%-8s", userName)) + (password.length() > 8 ? password.substring(0, 8) : String.format("%-8s", password));
        njshqMsg.setData(loginData);
        session.write(njshqMsg);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        if (status == IdleStatus.BOTH_IDLE) {
            NJSHQMsg njshqMsg = new NJSHQMsg();
            njshqMsg.setDataLen(0);
            njshqMsg.setMainType(0);
            njshqMsg.setSubType(0);
            njshqMsg.setState("00000");
            njshqMsg.setNext("N");
            njshqMsg.setSign("0");
            njshqMsg.setCompress("0");
            njshqMsg.setKeep("0000");
            njshqMsg.setTraderId("0000000000000000");
            session.write(njshqMsg);
            logger.error("NJS发送心跳包：" + njshqMsg);
        }
        this.timeCloseSession(session);
    }

    private void timeCloseSession(IoSession ioSession) throws Exception{
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
            Integer minute = calendar.get(Calendar.MINUTE);
            if(hour == 8 && minute >= 40){
                jredisUtil = (JRedisUtil) SpringUtil.getBean("jredisUtil");
                Long isSuccess = jredisUtil.setnx("njs_session_close", "1");
                if(isSuccess == 1L){
                    ioSession.close(true);
                    jredisUtil.expire("njs_session_close", 3700);
                }
            }
        }catch(Exception e){
            logger.error("定时关闭session失败了。。", e);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

    }

    private DecimalFormat decimalFormat = new DecimalFormat("###0.00");//格式化设置

    private String getIsRepeatKey(Snapshot snapshot) {
        StringBuffer sb = new StringBuffer();
        if (null != snapshot) {
            NJSSnapshot temp = (NJSSnapshot) snapshot;
            if (null != snapshot) {
                if (StringUtils.isBlank(temp.getApdRecvTime())) {
                    sb.append(temp.getTradeDate());
                } else {
                    sb.append(temp.getApdRecvTime());
                }
                sb.append(temp.getExchange());
                sb.append(temp.getProdCode());
                sb.append(decimalFormat.format(temp.getBusinessAmount()));
            }
        }
        return sb.toString();
    }

    public Boolean isRepeat(Snapshot snapshot) {

        Boolean isRepeat = false;
        String redisIsRepeatKey = getIsRepeatKey(snapshot);
        if (!StringUtils.isBlank(redisIsRepeatKey)) {

            if (jredisUtil.exists(redisIsRepeatKey)) {
                isRepeat = true;
            } else {
                isRepeat = false;
            }
        } else {
            isRepeat = true;
        }
        return isRepeat;
    }

    private synchronized List<Snapshot> removeRepeat(List<Snapshot> list) {
        List<Snapshot> listResult = Collections.synchronizedList(new ArrayList());
        if (null != list) {

            for (Snapshot snapshot : list) {
                if (snapshot.getBusinessAmount() == 0 || snapshot.getOpenPx() == 0 || snapshot.getHighPx() == 0 || snapshot.getLowPx() == 0) {
                    continue;
                }
                if (!isRepeat(snapshot)) {

                    jredisUtil.setex(getIsRepeatKey(snapshot), "true", 36000000);//如果不重复，就添加
                    listResult.add(snapshot);
                }
            }

        }
        return listResult;
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        try{
            if (null != session && session.isConnected()) {
                NJSHQMsg temp = null;
                if (message instanceof NJSHQMsg) {
                    temp = (NJSHQMsg) message;
                    if (null != temp) {
                        List<Snapshot> list = NJSQuoteParseUtils.convert(temp.getData());
                        List<Snapshot> removeRepeatList = removeRepeat(list);
                        if (null != list) {
                            logger.info("南交所：" + list.size());
                        }

                        if (null != removeRepeatList && removeRepeatList.size() > 0) {

                            //callCreateCandle(removeRepeatList);
                        }
                        // insertRedisCacheData(list);
                    }
                }
            } else {
                logger.info("南交所 messageReceived 触发session close");

            }
        }catch(Exception e){
            logger.error("接收处理错误", e);
        }
    }

    //把获取的数据放入到redis缓存
    private void insertRedisCacheData(List<Snapshot> removeRepeatList) {
        if (null != removeRepeatList && removeRepeatList.size() > 0) {

            if (null != hqService) {
                hqService.insertRedisSnapshotAll(removeRepeatList);
            } else {
                logger.error("插入redis 行情错误：SpringUtil.getBean(\"hqService\")  is null");
            }
        }
    }

    //直接调用生成K线
    private void callCreateCandle(List<Snapshot> removeRepeatList) {
        NJSDataHandleThread njsDataHandleThread = (NJSDataHandleThread) SpringUtil.getBean("njsDataHandleThread");
        njsDataHandleThread.setMessage(null);
        njsDataHandleThread.setMessage(removeRepeatList);
        ProcessorManager.push(njsDataHandleThread);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

    }
}

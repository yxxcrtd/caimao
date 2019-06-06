package com.caimao.hq.core;

import com.caimao.bana.api.entity.req.getui.FGetuiPushMessageReq;
import com.caimao.bana.api.enums.getui.EGetuiActionType;
import com.caimao.bana.api.service.getui.IGetuiService;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理行情波动提醒线程
 */
public class GjsHQAlertRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GjsHQAlertRunnable.class);

    private List<Candle> candleList;
    private Snapshot snapshot;
    private String redisKey = "ampPush";

    public IHQService hqServer;
    public IGetuiService getuiService;
    public JRedisUtil jRedisUtil;

    public void setCandleList(List<Candle> candleList) {
        this.candleList = candleList;
    }

    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    GjsHQAlertRunnable(){
        //Spring 注入对象
        getuiService = (IGetuiService) SpringUtil.getBean("getuiService");
        hqServer = (IHQService) SpringUtil.getBean("hqService");
        jRedisUtil = (JRedisUtil) SpringUtil.getBean("jredisUtil");
    }

    @Override
    public void run() {
        try {
            Map<Integer, Map<String, Object>> allAmp = this.getAllAmp();
            Map<String, Object> ampA = allAmp.get(0);
            Boolean sendA = ampA != null?this.pushMsgA(Double.parseDouble(ampA.get("amp").toString()), Integer.parseInt(ampA.get("upDown").toString())):false;
            Map<String, Object> ampB = allAmp.get(30);
            Boolean sendB = ampB != null?this.pushMsgBCD(Double.parseDouble(ampB.get("amp").toString()), Integer.parseInt(ampB.get("upDown").toString()), 30):false;
            Map<String, Object> ampC = allAmp.get(60);
            Boolean sendC = ampC != null?this.pushMsgBCD(Double.parseDouble(ampC.get("amp").toString()), Integer.parseInt(ampC.get("upDown").toString()), 60):false;
            Map<String, Object> ampD = allAmp.get(120);
            Boolean sendD = ampD != null?this.pushMsgBCD(Double.parseDouble(ampD.get("amp").toString()), Integer.parseInt(ampD.get("upDown").toString()), 120):false;

            String msgPre = "【%s提醒】%s%s%s价格%s达%s%%，报价%s，%s单盈利空间%s%%（%s单注意风险），请及时关注。";
            String msg = "";
            if(sendA){
                String s1 = ampA.get("upDown").toString().equals("1")?"下跌":"上涨";
                String s2 = "今日";
                String s3 = this.snapshot.getExchange().equals("NJS")?"南":"上";
                String s4 = this.snapshot.getProdName();
                String s5 = new BigDecimal(ampA.get("amp").toString()).setScale(2, BigDecimal.ROUND_DOWN).toString();
                String s6 = String.valueOf(this.snapshot.getLastPx());
                String s7 = ampA.get("upDown").toString().equals("1")?"空":"多";
                String s8 = new BigDecimal(ampA.get("amp").toString()).multiply(new BigDecimal("10")).setScale(2, BigDecimal.ROUND_DOWN).toString();
                String s9 = ampA.get("upDown").toString().equals("1")?"多":"空";

                msg = String.format(msgPre, s1, s2, s3, s4, s1, s5, s6, s7, s8, s9);
            }else{
                if(sendD){
                    String s1 = ampB.get("upDown").toString().equals("1")?"下跌":"上涨";
                    String s2 = "30分钟内";
                    String s3 = this.snapshot.getExchange().equals("NJS")?"南":"上";
                    String s4 = this.snapshot.getProdName();
                    String s5 = ampB.get("amp").toString();
                    String s6 = String.valueOf(this.snapshot.getLastPx());
                    String s7 = ampB.get("upDown").toString().equals("1")?"空":"多";
                    String s8 = new BigDecimal(ampB.get("amp").toString()).multiply(new BigDecimal("10")).setScale(2, BigDecimal.ROUND_DOWN).toString();
                    String s9 = ampB.get("upDown").toString().equals("1")?"多":"空";

                    msg = String.format(msgPre, s1, s2, s3, s4, s1, s5, s6, s7, s8, s9);
                }else{
                    if(sendC){
                        String s1 = ampC.get("upDown").toString().equals("1")?"下跌":"上涨";
                        String s2 = "1小时内";
                        String s3 = this.snapshot.getExchange().equals("NJS")?"南":"上";
                        String s4 = this.snapshot.getProdName();
                        String s5 = ampC.get("amp").toString();
                        String s6 = String.valueOf(this.snapshot.getLastPx());
                        String s7 = ampC.get("upDown").toString().equals("1")?"空":"多";
                        String s8 = new BigDecimal(ampC.get("amp").toString()).multiply(new BigDecimal("10")).setScale(2, BigDecimal.ROUND_DOWN).toString();
                        String s9 = ampC.get("upDown").toString().equals("1")?"多":"空";

                        msg = String.format(msgPre, s1, s2, s3, s4, s1, s5, s6, s7, s8, s9);
                    }else{
                        if(sendB){
                            String s1 = ampD.get("upDown").toString().equals("1")?"下跌":"上涨";
                            String s2 = "1小时内";
                            String s3 = this.snapshot.getExchange().equals("NJS")?"南":"上";
                            String s4 = this.snapshot.getProdName();
                            String s5 = ampD.get("amp").toString();
                            String s6 = String.valueOf(this.snapshot.getLastPx());
                            String s7 = ampD.get("upDown").toString().equals("1")?"空":"多";
                            String s8 = new BigDecimal(ampD.get("amp").toString()).multiply(new BigDecimal("10")).setScale(2, BigDecimal.ROUND_DOWN).toString();
                            String s9 = ampD.get("upDown").toString().equals("1")?"多":"空";

                            msg = String.format(msgPre, s1, s2, s3, s4, s1, s5, s6, s7, s8, s9);
                        }
                    }
                }
            }
            if(!msg.equals("")){
                logger.info("行情波动提醒:" + msg);
                try {
                    // 发送个推消息
                    FGetuiPushMessageReq getuiPushMessageReq = new FGetuiPushMessageReq();
                    getuiPushMessageReq.setActionType(EGetuiActionType.TYPE_OPENAPP.getValue());
                    getuiPushMessageReq.setSource("gjs_price_alert");
                    getuiPushMessageReq.setTitle("行情波动提醒");
                    getuiPushMessageReq.setContent(msg);
                    this.getuiService.pushMessageToApp(getuiPushMessageReq);
                } catch (Exception e) {
                    logger.error("发送个推APP消息失败 {}", e);
                }
            }
        } catch (Exception e) {
            logger.error(" 处理指定商品的行情波动提醒失败 {}", e);
        }
    }

    //获取周期push最后发送时间
    private String getPushCycleTime(Integer cycle) throws Exception{
        return jRedisUtil.hget(this.redisKey, "amp_time_" + cycle);
    }

    //获取振幅等级
    private double getAmpLevel(double amp, Integer cycle) throws Exception{
        Map<Integer, String> cycleMap = getCycleMap();
        String[] ampArr = cycleMap.get(cycle).split(",");
        double ampLevel = 0;
        for (String ampL:ampArr){
            double ampP = Double.parseDouble(ampL);
            if(amp >= ampP) ampLevel = ampP;
        }
        return ampLevel;
    }

    //A push
    private Boolean pushMsgA(double amp, Integer upDown) throws Exception{
        try{
            double ampLevel = this.getAmpLevel(amp, 0);
            if(ampLevel > 0){
                //判断是否发送过
                String key = "amp_push_0_" + this.snapshot.getMinTime().substring(0, 8) + ampLevel + upDown;
                Long success = jRedisUtil.hsetnx(this.redisKey, key, String.valueOf(ampLevel));
                if(success == 1){
                    jRedisUtil.hset(this.redisKey, "amp_time_0", System.currentTimeMillis() + "," + ampLevel + "," + upDown);
                    return true;
                }
            }
        }catch(Exception e){
            logger.error(" 推送A类消息失败 {}", e);
        }
        return false;
    }

    //BCD push
    private Boolean pushMsgBCD(double amp, Integer upDown, Integer cycle) throws Exception{
        try{
            double ampLevel = this.getAmpLevel(amp, cycle);
            //获取A类发送情况
            String pushATime = this.getPushCycleTime(0);
            if(pushATime == null || pushATime.equals("")) throw new CustomerException("A类未发送，所以其他周期不发送", 1111);
            String[] AArr = pushATime.split(",");
            boolean isSend = true;
            //因A类过滤
            if(System.currentTimeMillis() - Long.parseLong(AArr[0]) < cycle * 60 * 1000L
                    && upDown == Integer.parseInt(AArr[2])
                    && ampLevel == Double.parseDouble(AArr[1])){
                isSend = false;
            }
            //因自身类过滤
            String pushBTime = this.getPushCycleTime(cycle);
            if(pushBTime != null && !pushBTime.equals("")){
                String[] BArr = pushBTime.split(",");
                if(System.currentTimeMillis() - Long.parseLong(BArr[0]) < cycle * 60 * 1000L
                        && upDown == Integer.parseInt(BArr[2])
                        && ampLevel == Double.parseDouble(BArr[1])){
                    isSend = false;
                }
            }
            if(isSend) jRedisUtil.hset(this.redisKey, "amp_time_" + cycle, System.currentTimeMillis() + "," + ampLevel + "," + upDown);
            return isSend;
        }catch(Exception e){
            logger.error(" 推送B、C、D类消息失败 {}", e);
            return false;
        }
    }

    //周期提醒振幅
    private static Map<Integer, String> getCycleMap() throws Exception{
        Map<Integer, String> cycleMap = new HashMap<>();
        cycleMap.put(0, "0.5,0.7,0.9,1.1,1.3,1.5,2,3,4,5,6,7");
        cycleMap.put(30, "0.5,0.7,0.9,1.1,1.3,1.5,2,3,4,5,6,7");
        cycleMap.put(60, "0.7,0.9,1.1,1.3,1.5,2,3,4,5,6,7");
        cycleMap.put(120, "0.9,1.1,1.3,1.5,2,3,4,5,6,7");
        return cycleMap;
    }

    //获取所有周期振幅
    private Map<Integer, Map<String, Object>> getAllAmp() throws Exception{
        try{
            Map<Integer, Map<String, Object>> allAmp = new HashMap<>();
            Map<Integer, String> cycleMap = getCycleMap();
            for(Map.Entry<Integer, String> entry:cycleMap.entrySet()){
                String[] ampStr = entry.getValue().split(",");
                allAmp.put(entry.getKey(), this.ampCal(entry.getKey(), Double.parseDouble(ampStr[0])));
            }
            return allAmp;
        }catch(Exception e){
            logger.error("获取所有周期振幅失败{}", e);
            throw e;
        }
    }

    //当日振幅判断
    private Map<String, Object> ampCal(int cycle, double amp) throws Exception{
        try{
            //计算振幅
            double highPx = 0;
            double lowPx = 0;
            double preClose = this.snapshot.getPreclosePx();
            double newPrice = 0;
            if(cycle == 0){
                highPx = this.snapshot.getHighPx();
                lowPx = this.snapshot.getLowPx();
                newPrice = this.snapshot.getLastPx();
            }else{
                for (int i = 0; i < cycle; i++){
                    if(this.candleList.size() < i) break;
                    Candle candle = this.candleList.get(i);
                    if(i == 0){
                        highPx = candle.getHighPx();
                        lowPx = candle.getLowPx();
                        newPrice = candle.getLastPx();
                    }else{
                        if(candle.getHighPx() > highPx) highPx = candle.getHighPx();
                        if(candle.getLowPx() < lowPx) lowPx = candle.getLowPx();
                    }
                }
            }
            if(highPx == 0 || lowPx == 0 || preClose == 0 || newPrice == 0) throw new CustomerException("计算振幅数据有0值", 1234);
            double calAmp = (highPx - lowPx) / preClose * 100;
            //判断振幅
            if(calAmp >= amp){
                //判断上涨下跌
                Integer upDown = Math.abs(highPx - newPrice) >= Math.abs(lowPx - newPrice)?1:2;
                Map<String, Object> ampMap = new HashMap<>();
                ampMap.put("amp", calAmp);
                ampMap.put("upDown", upDown);
                return ampMap;
            }
        }catch (CustomerException e){
            logger.error("计算周期数据失败:" + e.getMessage());
        }catch(Exception e){
            logger.error("计算周期数据失败{}", e);
        }
        return null;
    }
}
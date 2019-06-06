package com.caimao.bana.jobs.tasks.gjs;

import com.caimao.bana.api.service.IOperationService;
import com.caimao.bana.jobs.utils.HttpHelper;
import com.caimao.bana.jobs.utils.RedisUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * GJS定时任务
 */
@Component
public class GJSMonitor {
    private static final Logger logger = LoggerFactory.getLogger(GJSMonitor.class);

    @Resource
    private IOperationService operationService;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 发送短信
     * @param content 内容
     * @throws Exception
     */
    private void sendSms(String redisKey, String subject, String content) throws Exception{
        try{
            if(this.checkSend(redisKey)){
                operationService.addAlarmTask(redisKey, subject, content);
            }
        }catch(Exception e){
            logger.error("报警短息你发送失败", e);
        }
    }

    private Boolean checkSend(String redisKey) throws Exception{
        try{
            Object redisObj = redisUtils.get(0, "GJSMonitor_" + redisKey);
            if(redisObj != null){
                Integer num = Integer.parseInt(redisObj.toString());
                if(num > 3){
                    redisUtils.expired(0, redisKey, 7200L);
                    return false;
                }else{
                    redisUtils.set(0, redisKey, String.valueOf(num + 1));
                }
            }
        }catch(Exception ignored){}
        return true;
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void checkNJSTraderIdNum() throws Exception {
        Boolean isWarning = false;
        try{
            String html = HttpHelper.doGet("https://jin.caimao.com/api/gjs_account/queryCanUseNum");
            if(html.equals("")) throw new Exception("返回结果为空");
            Gson gson = new Gson();
            Map<String, Object> result = gson.fromJson(html, Map.class);
            Boolean success = (Boolean) result.get("success");
            Integer num = new BigDecimal(result.get("data").toString()).intValue();
            if(!success || num < 50) isWarning = true;
        }catch(Exception e){
            isWarning = true;
        }
        try{
            if(isWarning){
                this.sendSms("check_trader_id_num", "贵金属南交所账号数量不足或网站无法访问", "贵金属南交所账号数量不足或网站无法访问");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkNJSTrade() throws Exception {
        Boolean isWarning = false;
        try{
            String html = HttpHelper.doGet("http://183.62.138.186:21632/MIS-Adapter/openJqAdapter.action?ADAPTER=011043");
            if(html.equals("")) throw new Exception("返回结果为空");
            Gson gson = new Gson();
            Map<String, Object> result = gson.fromJson(html, Map.class);
            String state = result.get("STATE").toString();
            if(state == null || state.equals("")) isWarning = true;
        }catch(Exception e){
            isWarning = true;
        }
        try{
            if(isWarning){
                this.sendSms("check_njs_trade", "贵金属南交所交易无法连接", "贵金属南交所交易无法连接");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void checkNJSReturn() throws Exception {
        Boolean isWarning = false;
        try{
            Object redisObj = redisUtils.get(0, "NJS_return_heart");
            if(redisObj == null){
                isWarning = true;
            }
        }catch(Exception e){
            isWarning = true;
        }
        try{
            if(isWarning){
                this.sendSms("check_njs_return", "南交所主动反馈挂掉了", "南交所主动反馈挂掉了");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkNJSHQ() throws Exception {
        Boolean isWarning = false;
        try{
            String html = HttpHelper.doGet("https://jin.caimao.com/api/gjshq/snapshot/query?prodCode=AG.NJS");
            if(html.equals("")) throw new Exception("返回结果为空");
            Gson gson = new Gson();
            Map<String, Object> result = gson.fromJson(html, Map.class);
            Boolean success = (Boolean) result.get("success");
            if(!success) isWarning = true;
        }catch(Exception e){
            isWarning = true;
        }
        try{
            if(isWarning){
                this.sendSms("check_njs_hq", "贵金属行情无法连接", "贵金属行情无法连接");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
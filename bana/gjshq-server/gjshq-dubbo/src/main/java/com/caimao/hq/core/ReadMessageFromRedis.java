package com.caimao.hq.core;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.utils.ConfigUtils;
import com.caimao.hq.utils.GJSProductUtils;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by yzc on 2015/12/11.
 */
@Service("readMessageFromRedis")
public class ReadMessageFromRedis {
    private Logger logger = LoggerFactory.getLogger(ReadMessageFromRedis.class);
    @Autowired
    private JRedisUtil jredisUtil;


    @Autowired
    private IHQService hqService;
    @Autowired
    private GJSProductUtils gjsProductUtils;

    public void  start(){
        String is_open_WriteCandle= ConfigUtils.getApplicationProperties().getProperty("is_open_WriteCandle");
        if(StringUtils.isBlank(is_open_WriteCandle)){
            is_open_WriteCandle="true";
        }
        if("true".equalsIgnoreCase(is_open_WriteCandle)){
            readMessageFromNJS();
            readMessageFromSJS();
        }else{
            logger.info("不生成K线：is_open_WriteCandle="+is_open_WriteCandle);
        }
    }

    public static void main(String args[]){
        ReadMessageFromRedis ReadMessageFromRedis=new ReadMessageFromRedis();
        ReadMessageFromRedis.start();
    }

    public    void readMessageFromNJS(){

        final ScheduledExecutorService schedulerNJS = Executors.newScheduledThreadPool(1);
        final Runnable beeper = new Runnable() {
            public void run() {
                try{

                    Set<String> set=hqService.getSnapshotFromRedisAllAndDelete("NJS");
                    if(null!=set&&set.size()>0){
                        List list=new ArrayList();
                        gjsProductUtils.convertStrToSnapObject(set, list,HQDataInit.exchangeToSnapshot.get("NJS"),false);
                        if(null!=list&&list.size()>0){
                            NJSDataHandleThread njsDataHandleThread=(NJSDataHandleThread) SpringUtil.getBean("njsDataHandleThread");
                            njsDataHandleThread.setMessage(null);
                            njsDataHandleThread.setMessage(list);
                            ProcessorManager.push(njsDataHandleThread);
                        }

                    }
                }catch (Exception ex){
                    logger.error("从redis解析南交所数据异常："+ex.getMessage());
                }

            }
        };
        // 10毫秒钟后运行，并每隔20毫秒运行一次
        final ScheduledFuture beeperHandle = schedulerNJS.scheduleAtFixedRate(beeper, 10, 20, TimeUnit.MILLISECONDS);

    }

    public    void readMessageFromSJS(){

        final ScheduledExecutorService schedulerSJS = Executors.newScheduledThreadPool(1);
        final Runnable beeper = new Runnable() {
            public void run() {
                try{

                    Set<String> set=hqService.getSnapshotFromRedisAllAndDelete("SJS");
                    if(null!=set&&set.size()>0){
                        List list=new ArrayList();
                        gjsProductUtils.convertStrToSnapObject(set, list,HQDataInit.exchangeToSnapshot.get("SJS"),false);
                        if(null!=list&&list.size()>0){
                            SJSDataHandleThread sjsDataHandleThread=(SJSDataHandleThread) SpringUtil.getBean("sjsDataHandleThread");
                            sjsDataHandleThread.setMessage(null);
                            sjsDataHandleThread.setMessage(list);
                            ProcessorManager.push(sjsDataHandleThread);
                        }
                    }
                }catch (Exception ex){
                    logger.error("从redis解析上金所数据异常："+ex.getMessage());
                }
            }
        };
        // 10毫秒钟后运行，并每隔20毫秒运行一次
        final ScheduledFuture beeperHandle = schedulerSJS.scheduleAtFixedRate(beeper, 10, 20, TimeUnit.MILLISECONDS);
    }
}

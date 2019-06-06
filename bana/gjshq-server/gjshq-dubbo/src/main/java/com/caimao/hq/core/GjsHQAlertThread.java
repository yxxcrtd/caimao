package com.caimao.hq.core;

import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.api.entity.CandleReq;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.service.IHQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 贵金属行情提醒
 */
@Component
public class GjsHQAlertThread {

    private static final Logger logger = LoggerFactory.getLogger(GjsHQAlertThread.class);
    //进程池
    private ExecutorService pool;
    @Autowired
    private IHQService hqService;

    public void start() {
        try {
            // 初始化并创建进程池
            logger.info("创建进程池");
            this.pool = new ThreadPoolExecutor(20, 40, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100));

            new Thread() {
                @Override
                public void run() {
                    try {
                        runHQAlert();
                    } catch (Exception e) {
                        logger.error("行情波动提醒失败了{}", e);
                    }
                }
            }.start();
        } catch (Exception e) {
            logger.error("行情波动处理异常 {}", e);
        }
    }

    /**
     * 行情提醒
     * @throws InterruptedException
     */
    public void runHQAlert() throws InterruptedException {
        String[] productList = {"NJS.AG", "SJS.mAu(T+D)", "SJS.Ag(T+D)"};

        while (true) {
            for (String prod:productList){
                try{
                    //获取商品ticker
                    String[] productArray = prod.split("\\.");
                    Snapshot ticker = hqService.queryTicker(productArray[0], productArray[1]);
                    if(ticker == null) return;
                    //获取分时
                    CandleReq obj = new CandleReq();
                    obj.setCycle("1");
                    obj.setNumber(120);
                    obj.setExchange(productArray[0]);
                    obj.setProdCode(productArray[1]);
                    List<Candle> candleList = hqService.queryCandleRedisHistory(obj);
                    if(candleList == null) return;
                    GjsHQAlertRunnable runnable = new GjsHQAlertRunnable();
                    runnable.setSnapshot(ticker);
                    runnable.setCandleList(candleList);
                    pool.execute(runnable);
                }catch(Exception e){
                    logger.error("今日首次行情波动发送失败{}", e);
                }
            }
            Thread.sleep(60000);
        }
    }
}

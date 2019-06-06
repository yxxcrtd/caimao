package com.caimao.hq.core;

import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.api.entity.CandleReq;
import com.caimao.hq.api.entity.Product;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.utils.GJSProductUtils;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 贵金属价格提醒进程
 * Created by Administrator on 2015/11/25.
 * Created by Administrator on 2015/11/25.
 */
@Component
public class GjsPriceAlertThread {

    private static final Logger logger = LoggerFactory.getLogger(GjsPriceAlertThread.class);

    // 进程池
    private ExecutorService pool;

    private List<String> exchangeList = new ArrayList<>();

    @Autowired
    private GJSProductUtils gjsProductUtils;
    @Autowired
    private IHQService hqService;
    @Autowired
    private JRedisUtil jredisUtil;


    public void start() {
        try {
            // 初始化并创建进程池
            logger.info("创建进程池");
            //this.pool = Executors.newFixedThreadPool(20);
            this.pool = new ThreadPoolExecutor(20, 40, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100));

            // 设置执行的交易所
            this.exchangeList.add(EGJSExchange.NJS.getCode());
            this.exchangeList.add(EGJSExchange.SJS.getCode());
            // 开启一个新的进程去循环处理
            new Thread() {
                @Override
                public void run() {
                    try {
                        runPriceAlert();
                    } catch (Exception ignored) {
                    }
                }
            }.start();
        } catch (Exception e) {
            logger.error("价格提醒处理异常 {}", e);
        }
    }

    /**
     * 执行价格提醒的任务
     * @throws InterruptedException
     */
    public void runPriceAlert() throws InterruptedException {
        while (true) {
            // 获取所有的交易所下的产品
            for (String exchange: this.exchangeList) {
                List<Product> productList = this.gjsProductUtils.getProductList(exchange);
                if (productList != null) for (Product product : productList) {
                    try {
                        // 获取该产品的最新价格、涨跌幅，查询这个商品最后的日K线数据
                        CandleReq candleReq = new CandleReq();
                        candleReq.setExchange(exchange);
                        candleReq.setProdCode(product.getProdCode());
                        candleReq.setCycle("6");
                        candleReq.setNumber(1);
                        Candle candle = this.hqService.queryLastCandle(candleReq);

                        // 从redis中对比价格，如果变动了，在扔到线程去处理
                        String mainKey = "_price_alert";
                        String redisKey = exchange + "--" + product.getProdCode();
                        String redisPrice = this.jredisUtil.hget(mainKey, redisKey);
                        if (redisPrice != null && redisPrice.equals(String.valueOf(candle.getLastPx()))) {
                            continue;
                        } else {
                            this.jredisUtil.hset(mainKey, redisKey, String.valueOf(candle.getLastPx()));
                        }

                        // 抛到进程池里去处理
                        //GjsPriceAlertRunnable runnable = (GjsPriceAlertRunnable) SpringUtil.getBean("gjsPriceAlertRunnable");
                        GjsPriceAlertRunnable runnable = new GjsPriceAlertRunnable();
                        runnable.setCandle(candle);
                        pool.execute(runnable);
                    } catch (Exception e) {
                        logger.error("获取价格并抛入线程池异常 {}", e);
                        Thread.sleep(1000);
                    }
                }
            }
            Thread.sleep(2000);
        }
    }

}

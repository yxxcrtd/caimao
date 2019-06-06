package com.caimao.hq.junit;

import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.core.GjsHQAlertThread;
import com.caimao.hq.core.GjsPriceAlertRunnable;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 贵金属行情波动价格提醒
 * Created by Administrator on 2015/11/23.
 */
public class TestHQPriceAlert extends BaseTest {

    @Autowired
    private GjsHQAlertThread gjsHQAlertThread;

    /**
     * 运行价格提醒的服务
     * @throws Exception
     */
    @Test
    public void priceAlertRunTest() throws Exception {
        this.gjsHQAlertThread.start();
        Thread.sleep(1000000);
    }

/*    @Test
    public void alertRunTest() throws Exception {

        Candle candle = new Candle();
        candle.setExchange("NJS");
        candle.setProdCode("W1");
        candle.setProdName("坞");
        candle.setLastPx(150.1);
        candle.setPxChangeRate(0.13);

        GjsPriceAlertRunnable runnable = new GjsPriceAlertRunnable();
        runnable.setCandle(candle);
        runnable.run();

        //this.gjsPriceAlertRunnable.setCandle(candle);

        //this.gjsPriceAlertRunnable.run();

    }*/


}

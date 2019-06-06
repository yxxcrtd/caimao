package com.caimao.hq.junit;

import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.api.entity.GjsPriceAlertEntity;
import com.caimao.hq.api.entity.req.FQueryGjsPriceAlertReq;
import com.caimao.hq.api.enums.EPriceAlertType;
import com.caimao.hq.api.service.IGjsPriceAlertService;
import com.caimao.hq.core.GjsPriceAlertRunnable;
import com.caimao.hq.core.GjsPriceAlertThread;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 贵金属价格提醒
 * Created by Administrator on 2015/11/23.
 */
public class TestGjsPriceAlert extends BaseTest {

    @Resource
    private IGjsPriceAlertService gjsPriceAlertService;
    @Autowired
    private GjsPriceAlertThread gjsPriceAlertThread;

    /**
     * 重置符合条件的触发价格
     * @throws Exception
     */
    @Test
    public void resetPriceAlertTriggerPriceTest() throws Exception {
        this.gjsPriceAlertService.resetPriceAlertTriggerPrice("3", "10");
    }


    /**
     * 查询列表
     * @throws Exception
     */
    @Test
    public void queryGjsPriceAlertListTest() throws Exception {
        FQueryGjsPriceAlertReq req = new FQueryGjsPriceAlertReq();
        req.setExchange("NJS");
        req.setGoodCode("W1");
        req.setOn("1");
        req.setCondition(EPriceAlertType.DY.getValue());
        req.setPrice("0.21");

        List<GjsPriceAlertEntity> list = this.gjsPriceAlertService.queryGjsPriceAlertList(req);
        for (GjsPriceAlertEntity entity: list) {
            System.out.println(ToStringBuilder.reflectionToString(entity));
        }
    }

    /**
     * 查询指定内容
     * @throws Exception
     */
    @Test
    public void selectByIdTest() throws Exception {
        GjsPriceAlertEntity entity = this.gjsPriceAlertService.selectById(1L);
        System.out.println(ToStringBuilder.reflectionToString(entity));
    }

    /**
     * 根据userId，查询提醒设置
     * @throws Exception
     */
    @Test
    public void selectByUserIdTest() throws Exception {
        Long userId = 123L;
        List<GjsPriceAlertEntity> list = this.gjsPriceAlertService.selectByUserId(userId);
        for (GjsPriceAlertEntity entity : list) {
            System.out.println(ToStringBuilder.reflectionToString(entity));
        }
    }

    /**
     * 添加的接口
     * @throws Exception
     */
    @Test
    public void insertTest() throws Exception {
        GjsPriceAlertEntity entity = new GjsPriceAlertEntity();
        entity.setUserId(809250154610689L);
        entity.setExchange(EGJSExchange.NJS.getCode());
        entity.setGoodCode("W");
        entity.setGoodName("坞");
        entity.setCondition(EPriceAlertType.DY.getValue());
        entity.setOn("0");
        entity.setPrice(new BigDecimal("0"));

        this.gjsPriceAlertService.insert(entity);
    }

    @Test
    public void deleteByIdTest() throws Exception {
        Long id = 1L;
        this.gjsPriceAlertService.deleteById(id);
    }

    @Test
    public void updateTest() throws Exception {
        GjsPriceAlertEntity entity = new GjsPriceAlertEntity();
        entity.setId(1L);
        entity.setPrice(new BigDecimal(100));
        entity.setSendNum(12);
        entity.setLastSendTime(new Date());

        this.gjsPriceAlertService.update(entity);
    }

    /**
     * 运行价格提醒的服务
     * @throws Exception
     */
    @Test
    public void priceAlertRunTest() throws Exception {
        this.gjsPriceAlertThread.start();
        Thread.sleep(10000);
    }

    @Test
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

    }


}

package com.caimao.hq.junit.hq;

import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.api.service.IOtherHQService;
import com.caimao.hq.junit.BaseTest;
import com.caimao.hq.utils.DoubleOperationUtil;
import com.caimao.hq.utils.JRedisUtil;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangxinxin@huobi.com on 2015/12/25
 */
public class OtherHQDayCandleTest extends BaseTest {

    @Value("${url.otherCandle.kline.DayCandle}")
    protected String URL_OTHER_CANDLE_KLINE_DAY_CANDLE;

    @Autowired
    private IHQService hqService;

    @Autowired
    private IOtherHQService otherHQService;

    @Autowired
    public JRedisUtil jredisUtil;

    /**
     * 批量往数据库写 伦敦金日K
     */
    @Test
    public void catchLondonGoldK() throws Exception {
        String body = Jsoup.connect(URL_OTHER_CANDLE_KLINE_DAY_CANDLE).ignoreContentType(true).execute().body();
        WyHqMonthRes object = JSON.parseObject(body, WyHqMonthRes.class);
        if ("200".equals(object.getRetCode())) {
            List list = object.getRet();
            int length = list.size();
            System.out.println("总记录数：" + length);
            int mod = 100;
            List<OtherCandle> candleList = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                String[] str = list.get(i).toString().split(",");
                OtherCandle otherCandle = new OtherCandle();
                setOtherCandleData(otherCandle, str);
                candleList.add(otherCandle);
                if (0 == candleList.size() % mod) {
                    System.out.println(i);
                    otherHQService.insertBatchDB(candleList);
                    candleList.clear();
                }
            }
            otherHQService.insertBatchDB(candleList);
        }
    }

    /**
     * set数据
     */
    private void setOtherCandleData(OtherCandle liffe, String[] str) {
        String now = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", new Date());
        liffe.setExchange("LIFFE");
        String date = str[0].replaceAll("\\[", "").replaceAll("\"", "");
        liffe.setMinTime(date + "0000");
        liffe.setOptDate(now);
        liffe.setOpenPx(DoubleOperationUtil.parseDouble(str[1]));
        liffe.setLastPx(DoubleOperationUtil.parseDouble(str[2]));
        liffe.setHighPx(DoubleOperationUtil.parseDouble(str[3]));
        liffe.setLowPx(DoubleOperationUtil.parseDouble(str[4]));
        liffe.setPxChange(DoubleOperationUtil.parseDouble(str[5])); // 涨跌
        liffe.setPxChangeRate(DoubleOperationUtil.parseDouble(str[6].replaceAll("\"", "").replaceAll("%", ""))); // 涨跌幅
        liffe.setBusinessAmount(DoubleOperationUtil.parseDouble(str[7])); // 成交数量
        liffe.setBusinessBalance(DoubleOperationUtil.parseDouble(str[8])); // 成交金额
        liffe.setCycle(CandleCycle.DayCandle);
        liffe.setIsGoods(1);
    }

    /**
     * 往Redis写 AU 日K
     */
    @Test
    public void writeLastAUToRedis() throws IOException {
        String body = Jsoup.connect(URL_OTHER_CANDLE_KLINE_DAY_CANDLE + "&goodsId=AU&num=1").ignoreContentType(true).execute().body();
        WyHqMonthRes object = JSON.parseObject(body, WyHqMonthRes.class);
        if ("200".equals(object.getRetCode())) {
            List list = object.getRet();
            String[] str = list.get(0).toString().split(",");
            OtherCandle otherCandle = new OtherCandle();
            setOtherCandleData(otherCandle, str);
            otherCandle.setProdCode("AU");
            otherCandle.setProdName("伦敦金(美元/盎司)");
            otherCandle.setRedisKey("LIFFEAUDayCandle");
            jredisUtil.set("LIFFEAUDayCandle", JSON.toJSONString(otherCandle));
            System.out.println("================ AU 日K === Redis 写入成功！============");
        }
    }

    /**
     * 从Redis读 AU 日K
     */
    @Test
    public void queryLastAUInRedis() {
        CandleReq candleReq = new CandleReq();
        candleReq.setExchange("LIFFE");
        candleReq.setProdCode("AU");
        candleReq.setCycle("6");
        Candle candle = hqService.queryLastCandle(candleReq);
        System.out.println("========返回的 AU 日K=====" + candle);
    }

    /**
     * 往Redis写 AG 日K
     */
    @Test
    public void writeLastAGToRedis() throws IOException {
        String body = Jsoup.connect(URL_OTHER_CANDLE_KLINE_DAY_CANDLE + "&goodsId=AG&num=1").ignoreContentType(true).execute().body();
        WyHqMonthRes object = JSON.parseObject(body, WyHqMonthRes.class);
        if ("200".equals(object.getRetCode())) {
            List list = object.getRet();
            String[] str = list.get(0).toString().split(",");
            OtherCandle otherCandle = new OtherCandle();
            setOtherCandleData(otherCandle, str);
            otherCandle.setProdCode("AG");
            otherCandle.setProdName("伦敦银(美元/盎司)");
            otherCandle.setRedisKey("LIFFEAGDayCandle");
            jredisUtil.set("LIFFEAGDayCandle", JSON.toJSONString(otherCandle));
            System.out.println("================ AG 日K === Redis 写入成功！============");
        }
    }

    /**
     * 从Redis读 AG 日K
     */
    @Test
    public void queryLastAGInRedis() {
        CandleReq candleReq = new CandleReq();
        candleReq.setExchange("LIFFE");
        candleReq.setProdCode("AG");
        candleReq.setCycle("6");
        Candle candle = hqService.queryLastCandle(candleReq);
        System.out.println("========返回的 AG 日K=====" + candle);
    }

    /**
     * 批量往Redis写 AU 日K 历史
     */
    @Test
    public void writeAUHistoryToRedis() throws IOException {
        String body = Jsoup.connect(URL_OTHER_CANDLE_KLINE_DAY_CANDLE + "&goodsId=AU&num=10000").ignoreContentType(true).execute().body();
        WyHqMonthRes object = JSON.parseObject(body, WyHqMonthRes.class);
        if ("200".equals(object.getRetCode())) {
            List list = object.getRet();
            int length = list.size();
            System.out.println("总记录数：" + length);
            for (int i = 0; i < length; i++) {
                String[] str = list.get(i).toString().split(",");
                OtherCandle otherCandle = new OtherCandle();
                setOtherCandleData(otherCandle, str);
                otherCandle.setProdCode("AU");
                hqService.insertRedisCandleHistory(CandleCycle.DayCandle, otherCandle);
            }
            System.out.println("================ AU 日K 历史 === Redis 批量写入成功！============");
        }
    }

    /**
     * 从Redis读 AU 日K 历史
     */
    @Test
    public void queryAUHistoryInRedis() {
        CandleReq candleReq = new CandleReq();
        candleReq.setExchange("LIFFE");
        candleReq.setProdCode("AU");
        candleReq.setCycle("6");
        candleReq.setNumber(10);
        List<Candle> candleList = hqService.queryCandleRedisHistory(candleReq);
        System.out.println("======== 返回的 AU 日K 历史 =====" + candleList.size());
    }

    /**
     * 批量往Redis写 AG 日K 历史
     */
    @Test
    public void writeAGHistoryToRedis() throws IOException {
        String body = Jsoup.connect(URL_OTHER_CANDLE_KLINE_DAY_CANDLE + "&goodsId=AG&num=10000").ignoreContentType(true).execute().body();
        WyHqMonthRes object = JSON.parseObject(body, WyHqMonthRes.class);
        if ("200".equals(object.getRetCode())) {
            List list = object.getRet();
            int length = list.size();
            System.out.println("总记录数：" + length);
            for (int i = 0; i < length; i++) {
                String[] str = list.get(i).toString().split(",");
                OtherCandle otherCandle = new OtherCandle();
                setOtherCandleData(otherCandle, str);
                otherCandle.setExchange("LIFFE");
                otherCandle.setProdCode("AG");
                hqService.insertRedisCandleHistory(CandleCycle.DayCandle, otherCandle);
            }
            System.out.println("================ AG 日K 历史 === Redis 批量写入成功！============");
        }
    }

    /**
     * 从Redis读 AG 日K 历史
     */
    @Test
    public void queryAGHistoryInRedis() {
        CandleReq candleReq = new CandleReq();
        candleReq.setExchange("LIFFE");
        candleReq.setProdCode("AG");
        candleReq.setCycle("6");
        List<Candle> candleList = hqService.queryCandleRedisHistory(candleReq);
        System.out.println("======== 返回的 AG 日K 历史 =====" + candleList.size());
    }

}

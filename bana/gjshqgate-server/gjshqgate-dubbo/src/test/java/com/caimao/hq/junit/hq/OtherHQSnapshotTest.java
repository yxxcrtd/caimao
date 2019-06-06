package com.caimao.hq.junit.hq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caimao.hq.api.entity.CandleCycle;
import com.caimao.hq.api.entity.OtherCandle;
import com.caimao.hq.api.entity.WyHqMonthRes;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.api.service.IOtherHQService;
import com.caimao.hq.junit.BaseTest;
import com.caimao.hq.utils.DateUtils;
import com.caimao.hq.utils.DoubleOperationUtil;
import com.caimao.hq.utils.JRedisUtil;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

/**
 * Created by yangxinxin@huobi.com on 2016/01/04
 */
public class OtherHQSnapshotTest extends BaseTest {

    @Value("${url.otherCandle.kline.min}")
    protected String URL_OTHER_CANDLE_KLINE_MIN;

    @Autowired
    private IHQService hqService;

    @Autowired
    private IOtherHQService otherHQService;

    @Autowired
    public JRedisUtil jredisUtil;

    /**
     * 往Redis写 AU 分时 K线
     */
    @Test
    public void writeAUMin() throws Exception {
        String now = String.format("%1$tY%1$tm%1$td%1$tH%1$tM", new Date());
        String body = Jsoup.connect(URL_OTHER_CANDLE_KLINE_MIN + "&goodsId=AU&num=1&date=" + DateUtils.addMinue(now, -2)).ignoreContentType(true).execute().body();
        System.out.println("返回的JSON数据：" + body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        if ("200".equals(jsonObject.get("retCode")) && !"".equals(jsonObject.get("retCode"))) {
            WyHqMonthRes object = JSON.parseObject(jsonObject.get("ret").toString(), WyHqMonthRes.class);
            System.out.println("=============" + object.getData());
            List list = object.getData();
            String[] str = list.get(0).toString().split(",");
            OtherCandle otherCandle = new OtherCandle();
            otherCandle.setOptDate(now);
            setOtherCandleData(otherCandle, str);
            otherCandle.setProdCode("AU");
            otherCandle.setProdName("伦敦金(美元/盎司)");
            otherCandle.setRedisKey("LIFFEAUMinute1");
            jredisUtil.set("LIFFEAUMinute1", JSON.toJSONString(otherCandle));
            System.out.println("================ AU 分时 K线 === Redis 写入成功！============");
        }
    }

    /**
     * 往Redis写 AU 分时 K线 全部
     */
    @Test
    public void writeAUMinAll() throws Exception {
        String now = String.format("%1$tY%1$tm%1$td%1$tH%1$tM", new Date());
        String body = Jsoup.connect(URL_OTHER_CANDLE_KLINE_MIN + "&goodsId=AU&num=10000&date=").ignoreContentType(true).execute().body();
        System.out.println("返回的JSON数据：" + body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        if ("200".equals(jsonObject.get("retCode")) && !"".equals(jsonObject.get("retCode"))) {
            WyHqMonthRes object = JSON.parseObject(jsonObject.get("ret").toString(), WyHqMonthRes.class);
            System.out.println("=============" + object.getData());
            List list = object.getData();
            int length = list.size();
            System.out.println("总记录数：" + length);
            for (int i = 0; i < length; i++) {
                String[] str = list.get(0).toString().split(",");
                OtherCandle otherCandle = new OtherCandle();
                otherCandle.setOptDate(now);
                setOtherCandleData(otherCandle, str);
                otherCandle.setProdCode("AU");
                otherCandle.setProdName("伦敦金(美元/盎司)");
                hqService.insertRedisCandleHistory(CandleCycle.Minute1, otherCandle);
            }
            System.out.println("================ AU 分时 K线 全部 === Redis 写入成功！============");
        }
    }


    /**
     * set分时K线
     */
    private void setOtherCandleData(OtherCandle otherCandle, String[] str) {
        otherCandle.setExchange("LIFFE");
        String date = str[0].replaceAll("\\[", "").replaceAll("\"", "");
        otherCandle.setMinTime(date);
        otherCandle.setLastPx(DoubleOperationUtil.parseDouble(str[1]));
        otherCandle.setPxChange(DoubleOperationUtil.parseDouble(str[3])); // 涨跌
        otherCandle.setPxChangeRate(DoubleOperationUtil.parseDouble(str[4].replaceAll("\"", "").replaceAll("%", ""))); // 涨跌幅
        otherCandle.setCycle(CandleCycle.Minute1);
        otherCandle.setIsGoods(1);
    }


}

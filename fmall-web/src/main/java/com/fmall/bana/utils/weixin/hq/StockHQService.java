package com.fmall.bana.utils.weixin.hq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fmall.bana.utils.RedisUtils;
import com.fmall.bana.utils.weixin.entity.TickerEntity;
import com.fmall.bana.utils.weixin.utils.HttpHelper;
import com.huobi.commons.utils.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

/**
 * 股票行情
 * Created by Administrator on 2016/1/5.
 */
@Component
public class StockHQService {

    private static final Logger logger = LoggerFactory.getLogger(StockHQService.class);

    @Resource
    private RedisUtils redisUtils;

    /**
     * 为了解决Redis中文的问题，单独写的方法
     * @param key
     * @param val
     */
    private void setRedis(String key, String val) {
        this.redisUtils.set(key, JSON.toJSONString(val, SerializerFeature.BrowserCompatible), 300L);
    }

    private String getRedis(String key) {
        Object val = this.redisUtils.get(key);
        if (val == null) return null;
        return JSON.parseObject(val.toString(), String.class);
    }


    /**
     * 返回股票代码所属的市场代码
     * @param stockCode
     * @return
     */
    private String getCodeMarket(String stockCode) {
        if(stockCode.length() != 6) return null;
        if (stockCode.equals("000001")) {
            return "sh";
        }
        String marketType = null;
        String stockPre = stockCode.substring(0, 1);
        switch (stockPre) {
            case "0":
            case "1":
            case "2":
            case "3":
                marketType = "sz";
                break;
            case "5":
            case "6":
                marketType = "sh";
                break;
        }
        return marketType;
    }

    /**
     * 返回股票代码的Ticker信息
     * @param code
     * @return
     */
    public TickerEntity getTicket(String code) throws Exception {
        String marketType = this.getCodeMarket(code);
        if (marketType == null) return null;
        Object httpRes = this.getRedis("hq_"+code);
        if (httpRes == null) {
            String url = String.format("http://hq.sinajs.cn/list=%s%s", marketType, code);
            httpRes = HttpUtil.get(url, new ResponseHandler<String>() {
                public String handleResponse(HttpResponse response) throws IOException {
                    if (response.getStatusLine().getStatusCode() == 200) {
                        return EntityUtils.toString(response.getEntity(), "GBK");
                    }
                    return null;
                }
            });
            this.setRedis("hq_"+code, httpRes.toString());
            logger.info("股票行情 url ({}) 返回 ：{}", url, httpRes);
        }
        String tickerStr = httpRes.toString().substring(httpRes.toString().indexOf("\"")+1, httpRes.toString().lastIndexOf("\""));
        if (Objects.equals(tickerStr, "")) return null;
        String[] tickerArr = tickerStr.split(",");
        if (tickerArr.length < 10) return null;
        TickerEntity tickerEntity = new TickerEntity();
        tickerEntity.setName(tickerArr[0]);
        tickerEntity.setCode(code);
        tickerEntity.setOpenPrice(tickerArr[1]);
        tickerEntity.setClosePrice(tickerArr[2]);
        tickerEntity.setCurPrice(("0.00".equals(Float.parseFloat(tickerArr[3])) || "0.00".equals(tickerArr[3])) ? Float.parseFloat(tickerArr[2]) : Float.parseFloat(tickerArr[3]));
        tickerEntity.setHighPrice(tickerArr[4]);
        tickerEntity.setLowPrice(tickerArr[5]);
        tickerEntity.setTotalNum(tickerArr[8]);
        tickerEntity.setTotalMoney(tickerArr[9]);
        return tickerEntity;
    }

}

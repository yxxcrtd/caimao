package com.caimao.hq.service;

import com.alibaba.fastjson.JSONObject;
import com.caimao.hq.api.entity.CandleCycle;
import com.caimao.hq.api.entity.OtherCandle;
import com.caimao.hq.api.entity.OtherSnapshot;
import com.caimao.hq.utils.DoubleOperationUtil;

import java.util.Date;

public class OtherHQServiceHelper {

    /**
     * 设置日K线
     */
    protected static void setOtherCandleDayData(OtherCandle otherCandle, String[] str) {
        otherCandle.setExchange("LIFFE");
        String now = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", new Date());
        String date = str[0].replaceAll("\\[", "").replaceAll("\"", "");
        otherCandle.setMinTime(date + "0000");
        otherCandle.setOptDate(now);
        otherCandle.setOpenPx(DoubleOperationUtil.parseDouble(str[1]));
        otherCandle.setLastPx(DoubleOperationUtil.parseDouble(str[2]));
        otherCandle.setHighPx(DoubleOperationUtil.parseDouble(str[3]));
        otherCandle.setLowPx(DoubleOperationUtil.parseDouble(str[4]));
        otherCandle.setClosePx(DoubleOperationUtil.parseDouble(str[2]));
        otherCandle.setPxChange(DoubleOperationUtil.parseDouble(str[5])); // 涨跌
        otherCandle.setPxChangeRate(DoubleOperationUtil.parseDouble(str[6].replaceAll("\"", "").replaceAll("%", ""))); // 涨跌幅
        otherCandle.setBusinessAmount(DoubleOperationUtil.parseDouble(str[7])); // 成交数量
        otherCandle.setBusinessBalance(DoubleOperationUtil.parseDouble(str[8])); // 成交金额
        otherCandle.setCycle(CandleCycle.DayCandle);
        otherCandle.setIsGoods(1);
    }

    /**
     * 设置分时K线
     */
    protected static void setOtherCandleMinData(OtherCandle otherCandle, String[] str) {
        otherCandle.setExchange("LIFFE");
        String date = str[0].replaceAll("\\[", "").replaceAll("\"", "");
        otherCandle.setMinTime(date);
        otherCandle.setLastPx(DoubleOperationUtil.parseDouble(str[1]));
        otherCandle.setPxChange(DoubleOperationUtil.parseDouble(str[3])); // 涨跌
        otherCandle.setPxChangeRate(DoubleOperationUtil.parseDouble(str[4].replaceAll("\"", "").replaceAll("%", ""))); // 涨跌幅
        otherCandle.setCycle(CandleCycle.Minute1);
        otherCandle.setIsGoods(1);
    }

    /**
     * 设置分时
     */
    protected static void setOtherSnapshotData(OtherSnapshot otherSnapshot, JSONObject jsonObject) {
        otherSnapshot.setExchange("LIFFE");
        String now = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", new Date());
        otherSnapshot.setOptDate(now);
        otherSnapshot.setMinTime(jsonObject.get("fdate").toString() + jsonObject.get("ftime").toString());
        otherSnapshot.setOpenPx(DoubleOperationUtil.parseDouble(jsonObject.get("openPrice").toString()));
        otherSnapshot.setLastPx(DoubleOperationUtil.parseDouble(jsonObject.get("newPrice").toString()));
        otherSnapshot.setHighPx(DoubleOperationUtil.parseDouble(jsonObject.get("highPrice").toString()));
        otherSnapshot.setLowPx(DoubleOperationUtil.parseDouble(jsonObject.get("lowerPrice").toString()));
        otherSnapshot.setClosePx(DoubleOperationUtil.parseDouble(jsonObject.get("newPrice").toString()));
        otherSnapshot.setPxChange(DoubleOperationUtil.parseDouble(jsonObject.get("raiseLoss").toString())); // 涨跌
        otherSnapshot.setPxChangeRate(DoubleOperationUtil.parseDouble(jsonObject.get("upRate").toString().replaceAll("%", ""))); // 涨跌幅
        otherSnapshot.setCycle(CandleCycle.Snap);
        otherSnapshot.setIsGoods(1);
        otherSnapshot.setTradeDate(jsonObject.get("fdate").toString());
        otherSnapshot.setTradeTime(jsonObject.get("ftime").toString());
        otherSnapshot.setLastAmount(1);
    }

}

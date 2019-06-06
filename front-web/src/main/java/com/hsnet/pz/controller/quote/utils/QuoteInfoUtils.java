package com.hsnet.pz.controller.quote.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hsnet.pz.ao.quote.dto.QueryDayDataBigInt;
import com.hsnet.pz.ao.quote.dto.QueryPartRealTimeData;
import com.hsnet.pz.ao.quote.dto.QueryRealTimeData;
import com.hsnet.pz.ao.quote.dto.QueryReportData;
import com.hsnet.pz.ao.quote.dto.QueryTrendData;
import com.hundsun.communication.error.ChannelException;
import com.hundsun.winner.biz.quote.CodeInfo;
import com.hundsun.winner.biz.quote.QuoteConfig;
import com.hundsun.winner.biz.quote.QuoteConstants;
import com.hundsun.winner.biz.quote.QuoteManager;
import com.hundsun.winner.biz.quote.finance.AnsFinanceData;
import com.hundsun.winner.biz.quote.finance.FinanceData;
import com.hundsun.winner.biz.quote.init.StockInitInfo;
import com.hundsun.winner.biz.quote.kline.AnsDayDataBigInt;
import com.hundsun.winner.biz.quote.kline.StockCompDayDataBigInt;
import com.hundsun.winner.biz.quote.leadtrend.AnsLeadData;
import com.hundsun.winner.biz.quote.realtime.AbstractRealTimeData;
import com.hundsun.winner.biz.quote.realtime.HSStockRealTimeData;
import com.hundsun.winner.biz.quote.realtime.RealTimeData;
import com.hundsun.winner.biz.quote.serverCal.AnsSeverCalculate;
import com.hundsun.winner.biz.quote.trend.AnsTrendData;
import com.hundsun.winner.biz.quote.trend.PriceVolItem;
import com.hundsun.winner.biz.quote.trend.StockLeadData;

/**
 * 股票行情接口出参处理工具
 * @author chensz11448
 *@2014-7-11
 */
@Component
public class QuoteInfoUtils {

    /**
     * 行情价格缩小倍数: (原始数据扩大1000倍)
     */
    private static double PRICEMULTIPLE = 1000.0;

    /**
     * 成交量缩小倍数: (原始数据单位：股 -> 手)
     */
    private static int TOTALMULTIPLE = 100;

    /**
     * 成交额扩大倍数： (原始数据单位：千元 -> 元)
     */
    private static int MONEYMULTIPLE = 1000;

    /**
     * 股本扩大倍数：万股 -> 股 
     */
    private static int VALUEMULTIPLE = 10000;

    /**
     * 买卖五档 、内外盘 缩小倍数：(原单位：股 -> 手)
     */
    private static int FIVEDATAMULTIPLE = 100;

    private static String[] LEADTRENDLINECODE = { "1A0001", "2A01" };

    /**
     * 处理排序股票数据
     * @param list
     * @return
     *@create：2014-7-11 下午7:28:06 chensz11448
     *@history
     */
    public static List<QueryReportData> toReportData(List<RealTimeData> list) {
        List<QueryReportData> dataList = new ArrayList<QueryReportData>();
        for (RealTimeData data : list) {
            QueryReportData reportData = new QueryReportData();
            String stock_code = data.getCodeInfo().getCode();
            StockInitInfo initInfo = getStockInit(stock_code);

            double prev_price = initInfo.getPrevClose();
            double new_price = data.getData().getNewPrice();
            double charge_value = (new_price - prev_price) / PRICEMULTIPLE;
            double charge_rate = (new_price - prev_price) / prev_price;

            reportData.setStock_code(stock_code);
            reportData.setStock_name(initInfo.getStockName());
            reportData.setNew_price(new_price / PRICEMULTIPLE + "");
            reportData.setCharge_value(charge_value + "");
            reportData.setCharge_rate(getNum(charge_rate, 4) + "");

            dataList.add(reportData);
        }
        return dataList;
    }

    /**
     * 处理k线数据
     * @param dayData
     * @return
     *@create：2014-7-13 下午3:01:45 chensz11448
     *@history
     */
    public static List<QueryDayDataBigInt> toDayDataBigInt(
            AnsDayDataBigInt dayData) {
        List<QueryDayDataBigInt> dataList = new ArrayList<QueryDayDataBigInt>();
        if (dayData != null) {
            List<StockCompDayDataBigInt> list = dayData.getItems();
            for (StockCompDayDataBigInt data : list) {
                QueryDayDataBigInt day_data = new QueryDayDataBigInt();
                double open_price = data.getOpenPrice() / PRICEMULTIPLE;
                double close_price = data.getClosePrice() / PRICEMULTIPLE;
                double max_price = data.getMaxPrice() / PRICEMULTIPLE;
                double min_price = data.getMinPrice() / PRICEMULTIPLE;
                // 成交量：手
                long total = data.getTotal();
                // 成交额：千元 -> 元
                long money = data.getMoney() * MONEYMULTIPLE;
                long date = data.getDate();

                day_data.setOpen_price(open_price + "");
                day_data.setClose_price(close_price + "");
                day_data.setMax_price(max_price + "");
                day_data.setMin_price(min_price + "");
                day_data.setTotal(total + "");
                day_data.setMoney(money + "");
                day_data.setDate(date + "");

                dataList.add(day_data);
            }
        }
        return dataList;
    }

    /**
     * 处理股票实时数据
     * @param realTimeData
     * @param calculate
     * @return
     *@create：2014-7-13 下午5:20:08 chensz11448
     *@history
     */
    public static QueryRealTimeData toRealTimeData(RealTimeData realTimeData,
            AnsSeverCalculate calculate) {
        QueryRealTimeData data = new QueryRealTimeData();
        if (realTimeData != null) {
            String stock_code = realTimeData.getCodeInfo().getCode();
            StockInitInfo initInfo = getStockInit(stock_code);
            AbstractRealTimeData absRealTimeData = realTimeData.getData();

            String stock_name = initInfo.getStockName();
            double prev_price = initInfo.getPrevClose();
            double new_price = absRealTimeData.getNewPrice();

            double charge_value = 0.0, charge_rate = 0.0;
            if (new_price != 0.0) {
                charge_value = (new_price - prev_price) / PRICEMULTIPLE;
                charge_rate = (new_price - prev_price) / prev_price;
            }
            double up_price = 0.0, down_price = 0.0;
            // 债券无涨跌停
            if (calculate != null
                    && QuoteConstants.KIND_BOND != initInfo.getCodeInfo()
                        .getKind()) {
                up_price = calculate.getDatas().get(0).getUpPrice()
                        / PRICEMULTIPLE;
                down_price = calculate.getDatas().get(0).getDownPrice()
                        / PRICEMULTIPLE;
            }
            double open_price = absRealTimeData.getOpen() / PRICEMULTIPLE;
            double max_price = absRealTimeData.getMaxPrice() / PRICEMULTIPLE;
            double min_price = absRealTimeData.getMinPrice() / PRICEMULTIPLE;
            // 成交量：股 -> 手
            long total = (long) (absRealTimeData.getTotal() / TOTALMULTIPLE);
            // 成交额：元
            long total_amount = (long) absRealTimeData.getTotalAmount();

            data.setKind(initInfo.getCodeInfo().getKind() + "");
            data.setStock_code(stock_code);
            data.setStock_name(stock_name);
            data.setNew_price(new_price / PRICEMULTIPLE + "");
            data.setCharge_value(charge_value + "");
            data.setCharge_rate(getNum(charge_rate, 4) + "");
            data.setUp_price(up_price + "");
            data.setDown_price(down_price + "");
            data.setOpen_price(open_price + "");
            data.setMax_price(max_price + "");
            data.setMin_price(min_price + "");
            data.setPrev_close_price(prev_price / PRICEMULTIPLE + "");
            data.setTotal(total + "");
            data.setTotal_amount(total_amount + "");

            // 振幅 (最高-最低)/昨收盘
            double swing = (max_price - min_price)
                    / (prev_price / PRICEMULTIPLE);
            data.setSwing(getNum(swing, 4) + "");
            // 五档
            long buy_count1 = 0, buy_count2 = 0, buy_count3 = 0, buy_count4 = 0, buy_count5 = 0;
            double buy_price1 = 0d, buy_price2 = 0d, buy_price3 = 0d, buy_price4 = 0d, buy_price5 = 0d;
            long sell_count1 = 0, sell_count2 = 0, sell_count3 = 0, sell_count4 = 0, sell_count5 = 0;
            double sell_price1 = 0, sell_price2 = 0, sell_price3 = 0, sell_price4 = 0, sell_price5 = 0;

            if (absRealTimeData instanceof HSStockRealTimeData) { // 股票
                HSStockRealTimeData hsData = (HSStockRealTimeData) absRealTimeData;
                buy_count1 = hsData.getBuyCount1() / FIVEDATAMULTIPLE;
                buy_count2 = hsData.getBuyCount2() / FIVEDATAMULTIPLE;
                buy_count3 = hsData.getBuyCount3() / FIVEDATAMULTIPLE;
                buy_count4 = hsData.getBuyCount4() / FIVEDATAMULTIPLE;
                buy_count5 = hsData.getBuyCount5() / FIVEDATAMULTIPLE;
                buy_price1 = hsData.getBuyPrice1() / PRICEMULTIPLE;
                buy_price2 = hsData.getBuyPrice2() / PRICEMULTIPLE;
                buy_price3 = hsData.getBuyPrice3() / PRICEMULTIPLE;
                buy_price4 = hsData.getBuyPrice4() / PRICEMULTIPLE;
                buy_price5 = hsData.getBuyPrice5() / PRICEMULTIPLE;
                sell_count1 = hsData.getSellCount1() / FIVEDATAMULTIPLE;
                sell_count2 = hsData.getSellCount2() / FIVEDATAMULTIPLE;
                sell_count3 = hsData.getSellCount3() / FIVEDATAMULTIPLE;
                sell_count4 = hsData.getSellCount4() / FIVEDATAMULTIPLE;
                sell_count5 = hsData.getSellCount5() / FIVEDATAMULTIPLE;
                sell_price1 = hsData.getSellPrice1() / PRICEMULTIPLE;
                sell_price2 = hsData.getSellPrice2() / PRICEMULTIPLE;
                sell_price3 = hsData.getSellPrice3() / PRICEMULTIPLE;
                sell_price4 = hsData.getSellPrice4() / PRICEMULTIPLE;
                sell_price5 = hsData.getSellPrice5() / PRICEMULTIPLE;

            } else { // 指数
                buy_count1 = absRealTimeData.getBuyCount1();
                sell_count1 = absRealTimeData.getSellCount1();
                buy_price1 = absRealTimeData.getBuyPrice1();
                sell_price1 = absRealTimeData.getSellPrice1();
            }

            // 内外盘：股 -> 手
            long inside = realTimeData.getOtherData().getInside()
                    / FIVEDATAMULTIPLE;
            long outside = realTimeData.getOtherData().getOutside()
                    / FIVEDATAMULTIPLE;

            data.setBuy_count1(buy_count1 + "");
            data.setBuy_count2(buy_count2 + "");
            data.setBuy_count3(buy_count3 + "");
            data.setBuy_count4(buy_count4 + "");
            data.setBuy_count5(buy_count5 + "");
            data.setBuy_price1(buy_price1 + "");
            data.setBuy_price2(buy_price2 + "");
            data.setBuy_price3(buy_price3 + "");
            data.setBuy_price4(buy_price4 + "");
            data.setBuy_price5(buy_price5 + "");
            data.setSell_count1(sell_count1 + "");
            data.setSell_count2(sell_count2 + "");
            data.setSell_count3(sell_count3 + "");
            data.setSell_count4(sell_count4 + "");
            data.setSell_count5(sell_count5 + "");
            data.setSell_price1(sell_price1 + "");
            data.setSell_price2(sell_price2 + "");
            data.setSell_price3(sell_price3 + "");
            data.setSell_price4(sell_price4 + "");
            data.setSell_price5(sell_price5 + "");
            data.setInside(inside + "");
            data.setOutside(outside + "");

            /**
             *  期货,指数   weiHe = SWH(1);  weiCha = SWC(1);
             *  其它              weiHe = SWH(1) + SWH(2) + SWH(3) + SWH(4) + SWH(5);
             *         weiCha = SWC(1) + SWC(2) + SWC(3) + SWC(4) + SWC(5);
             */
            CodeInfo codeInfo = realTimeData.getCodeInfo();
            data.setExchangeType(codeInfo.getExchangeType() + "");
            long weiHe, weiCha;
            double weiBi;
            if (codeInfo.getMarket() == QuoteConstants.MARKET_FUTURES
                    || codeInfo.getKind() == QuoteConstants.KIND_INDEX) {// 期货,指数
                weiHe = (buy_count1 + sell_count1);
                weiCha = (buy_count1 - sell_count1);
            } else {
                weiHe = (buy_count1 + sell_count1) + (buy_count2 + sell_count2)
                        + (buy_count3 + sell_count3)
                        + (buy_count4 + sell_count4)
                        + (buy_count5 + sell_count5);
                weiCha = (buy_count1 - sell_count1)
                        + (buy_count2 - sell_count2)
                        + (buy_count3 - sell_count3)
                        + (buy_count4 - sell_count4)
                        + (buy_count5 - sell_count5);
            }
            if (weiHe == 0) {
                weiBi = 0;
            } else {
                weiBi = (100.0 * weiCha / weiHe) / 100.0;
            }

            // 委比保留2位有效数字
            BigDecimal mData = new BigDecimal(Double.toString(weiBi)).setScale(
                4, BigDecimal.ROUND_HALF_UP);

            data.setWeiBi(mData.toString());
            data.setWeiCha(Long.toString(weiCha));
        }
        return data;
    }

    /**
     * 处理分时数据
     * @param trendData
     * @return
     *@create：2014-7-13 下午5:46:57 chensz11448
     *@history
     */
    public static List<QueryTrendData> toTrendData(AnsTrendData trendData,
            String stockCode) {
        List<QueryTrendData> trendList = new ArrayList<QueryTrendData>();
        if (trendData != null) {
            if (ifDrawLeadLine(stockCode)) {
                // 大盘，获取领先线
                trendList = getLeadTrendData(stockCode);
            } else {
                List<PriceVolItem> list = trendData.getPriceVolItems();
                for (int i = 0; i < list.size(); i++) {
                    PriceVolItem item = list.get(i);
                    long pre_total = 0;
                    if (i != 0) {
                        // 成交量：股 -> 手
                        pre_total = list.get(i - 1).getTotal() / TOTALMULTIPLE;
                    }
                    QueryTrendData data = new QueryTrendData();

                    double new_price = item.getNewPrice();
                    // 成交量：股 -> 手
                    long total = item.getTotal() / TOTALMULTIPLE;
                    total = total - pre_total; // 当前成交总量 - 上一分钟成交总量

                    data.setNew_price(new_price / PRICEMULTIPLE + "");
                    data.setTotal(total + "");

                    trendList.add(data);
                }
                // 计算分时均价线
                initAveragePrice(trendData, trendList);
            }
        }

        return trendList;
    }

    /**
     * 是否需要绘制领先线
     * @return 
     * @create: 2014-8-4 上午11:46:22 zhouqs07071
     * @history:
     */
    protected static boolean ifDrawLeadLine(String stockCode) {
        for (String code : LEADTRENDLINECODE) {
            if (stockCode.equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取计算领先线
     * @param stockCode
     * @return 
     * @create: 2014-8-4 下午3:45:48 zhouqs07071
     * @history:
     */
    protected static List<QueryTrendData> getLeadTrendData(String stockCode) {
        List<QueryTrendData> trendList = new ArrayList<QueryTrendData>();
        StockInitInfo stockInitInfo = getStockInit(stockCode);
        try {
            AnsLeadData ansLeadData = QuoteManager.getLeadData(stockInitInfo
                .getCodeInfo());
            double lastClosePrice = stockInitInfo.getPrevClose()
                    / PRICEMULTIPLE;

            List<StockLeadData> leadDataList = ansLeadData.getDatas();
            List<PriceVolItem> list = ansLeadData.getPriceVolItems();
            for (int i = 0; i < leadDataList.size(); i++) {
                PriceVolItem item = list.get(i);
                long pre_total = 0;
                if (i != 0) {
                    // 成交量：股 -> 手
                    pre_total = list.get(i - 1).getTotal() / TOTALMULTIPLE;
                }
                QueryTrendData data = new QueryTrendData();

                double new_price = item.getNewPrice();
                // 成交量：股 -> 手
                long total = item.getTotal() / TOTALMULTIPLE;
                total = total - pre_total; // 当前成交总量 - 上一分钟成交总量

                data.setNew_price(new_price / PRICEMULTIPLE + "");
                data.setTotal(total + "");

                // 计算领先线
                double tlead = leadDataList.get(i).getNLead();
                tlead = (Double) ((tlead + 10000f) * lastClosePrice / 10000f);
                tlead = getNum(tlead, 3);
                data.setAverage_price(Double.toString(tlead));

                trendList.add(data);
            }

        } catch (ChannelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return trendList;
    }

    public static long getTopNLead(List<StockLeadData> leadDataList) {
        long tempTop = 0L;
        for (int i = 0; i < leadDataList.size(); i++) {
            if (i == 0) {
                tempTop = leadDataList.get(i).getNLead();
            } else {
                if (leadDataList.get(i).getNLead() > tempTop) {
                    tempTop = leadDataList.get(i).getNLead();
                }
            }
        }
        return tempTop;
    }

    public static long getBottomNLead(List<StockLeadData> leadDataList) {
        long bottomTop = 0L;
        for (int i = 0; i < leadDataList.size(); i++) {
            if (i == 0) {
                bottomTop = leadDataList.get(i).getNLead();
            } else {
                if (leadDataList.get(i).getNLead() < bottomTop) {
                    bottomTop = leadDataList.get(i).getNLead();
                }
            }
        }
        return bottomTop;
    }

    /**
     * 分时图 - 股票均价线 
     * @param trendData 
     * @create: 2014-7-25 下午4:20:21 zhouqs07071
     * @history:
     */
    protected static void initAveragePrice(AnsTrendData trendData,
            List<QueryTrendData> trendList) {
        double totalMoney = 0;
        long totalAmount = 0;
        long curTotal = 0;
        long preTotal = 0;
        long preDiffTotal = 0;
        long diffTotal = 0;
        double curPrice = 0;
        double average = 0;
        double openPrice = 0.0f;

        List<PriceVolItem> mPriceVolItemList = trendData.getPriceVolItems();
        openPrice = trendData.getStockRealTime().getOpen();
        for (int i = 0; i < mPriceVolItemList.size(); i++) {
            PriceVolItem obj = mPriceVolItemList.get(i);
            PriceVolItem pre_obj = null;
            if (i != 0) {
                pre_obj = mPriceVolItemList.get(i - 1);
            }
            if (pre_obj == null) {
                diffTotal = obj.getTotal();
            } else {
                preTotal = pre_obj.getTotal();
                diffTotal = obj.getTotal() - pre_obj.getTotal();
            }
            curTotal = obj.getTotal();
            curPrice = obj.getNewPrice();

            if (curPrice == 0) {
                if (i == 0) {
                    curPrice = openPrice;
                } else {
                    curPrice = mPriceVolItemList.get(i - 1).getNewPrice();
                }
                PriceVolItem pvi = mPriceVolItemList.get(i);
                pvi.setNewPrice((int) curPrice);
                pvi.setTotal(curTotal);
                mPriceVolItemList.set(i, pvi);
            }

            if (curTotal == 0 && pre_obj != null) {
                curTotal = preTotal;
                diffTotal = preDiffTotal;
            }
            preDiffTotal = diffTotal;
            totalAmount += diffTotal;
            totalMoney += (float) diffTotal * curPrice / PRICEMULTIPLE;
            if (totalAmount == 0) {
                average = curPrice / PRICEMULTIPLE;
            } else {
                average = totalMoney / totalAmount;
                if (average == 0) {
                    average = curPrice / PRICEMULTIPLE;
                }
            }
            BigDecimal b = new BigDecimal(average);
            average = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            trendList.get(i).setAverage_price(Double.toString(average));
        }
    }

    /**
     * 处理五档数据
     * @param ansTrendData
     * @param stock_code
     * @return
     *@create：2014-7-15 下午5:06:04 chensz11448
     *@history
     */
    public static QueryPartRealTimeData toPartRealTimeData(
            AnsTrendData ansTrendData, String stock_code) {
        QueryPartRealTimeData partRealTimeData = new QueryPartRealTimeData();
        if (ansTrendData != null) {
            StockInitInfo initInfo = getStockInit(stock_code);

            double openPrice = ansTrendData.getStockRealTime().getOpen()
                    / PRICEMULTIPLE;
            double preClose = initInfo.getPrevClose() / PRICEMULTIPLE;
            double max_price = ansTrendData.getStockRealTime().getMaxPrice()
                    / PRICEMULTIPLE;
            double min_price = ansTrendData.getStockRealTime().getMinPrice()
                    / PRICEMULTIPLE;

            partRealTimeData.setOpenPrice(openPrice + "");
            partRealTimeData.setPrevClosePrice(preClose + "");
            partRealTimeData.setMaxPrice(max_price + "");
            partRealTimeData.setMinPrice(min_price + "");
        }
        return partRealTimeData;
    }

    /**
     * 处理double数据小数位数
     * @param num
     * @param bit
     * @return
     *@create：2014-7-31 上午10:45:24 chensz11448
     *@history
     */
    public static double getNum(double num, int bit) {
        num = Math.round(num * Math.pow(10, bit)) / Math.pow(10.0, bit);
        return num;
    }

    /**
     * 获取股票初始化数据
     * @param code
     * @return
     *@create：2014-7-11 下午7:27:35 chensz11448
     *@history
     */
    private static StockInitInfo getStockInit(String code) {
        StockInitInfo stockInfo = QuoteConfig.getSHStockInit(code);
        if (stockInfo == null) {
            stockInfo = QuoteConfig.getSZStockInit(code);
        }
        return stockInfo;
    }
}

package com.caimao.bana.controller;

import com.caimao.bana.utils.RequestUtils;
import com.hsnet.pz.ao.quote.IStockQuoteAO;
import com.hsnet.pz.ao.quote.dto.*;
import com.hsnet.pz.controller.quote.mapper.MarketMapper;
import com.hsnet.pz.controller.quote.utils.DozerMapperManager;
import com.hsnet.pz.controller.quote.utils.QuoteInfoUtils;
import com.hundsun.winner.biz.quote.QuoteConfig;
import com.hundsun.winner.biz.quote.QuoteConstants;
import com.hundsun.winner.biz.quote.init.StockInitInfo;
import com.hundsun.winner.biz.quote.reportsort.ReqAnyReport;
import com.hundsun.winner.biz.quote.trend.AnsTrendData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuebj07252 on 2014/8/6.
 */
@RequestMapping(value = "/quote/stock")
@RestController
public class QuoteController {

    @Autowired
    private IStockQuoteAO stockQuoteAO;

    @Autowired
    private DozerMapperManager mapper;

    // 键盘精灵默认取数据条数
    private static int SIZE = 10;

    // 过滤指数
    private static int INDEX = 0;

    /**
     *  市场批量股票信息
     * @param marketType
     * @param page
     * @param limit
     * @param sortType
     * @return
     *@create：2014-7-15 上午11:33:30 chensz11448
     *@history
     */
    @RequestMapping(value = "/report_data", method = RequestMethod.GET)
    @ResponseBody
    public List<QueryReportDataRep> queryReportData(String marketType,
            String page, String limit, String sortType) {
        QueryReportDataDto param = new QueryReportDataDto();
        // 市场类型
        param.setMarket_type(MarketMapper.getMarketType(marketType));
        // 默认50条
        short _limit = (short) transferInteger(limit, 50);
        // 默认从0开始取
        short _begin = (short) ((transferInteger(limit, 50) - 1) * _limit);
        // 排序方式 默认涨跌幅倒序
        short _sortType = ReqAnyReport.COLUMN_HQ_BASE_RISE_RATIO;
        if (transferInteger(sortType, -1) == -1) {
            _sortType = -ReqAnyReport.COLUMN_HQ_BASE_RISE_RATIO;
        }
        param.setLimit(_limit);
        param.setBegin(_begin);
        param.setColumnId(_sortType);
        List<QueryReportData> list = QuoteInfoUtils.toReportData(stockQuoteAO
            .queryReportData(param));
        return mapper.mapper(list, QueryReportDataRep.class);
    }

    /**
     * K线数据（日K,周K,月K）
     * @param stockCode
     * @param period
     * @param count
     * @return
     *@create：2014-7-15 下午1:25:14 chensz11448
     *@history
     */
    @RequestMapping(value = "/{stockCode}/kline_data", method = RequestMethod.GET)
    @ResponseBody
    public List<QueryDayDataBigIntRep> queryDayDataBigInt(
            @PathVariable String stockCode, String period, String count) {
        QueryDayDataBigIntDto param = new QueryDayDataBigIntDto();
        param.setStock_code(stockCode);
        int type = transferInteger(period, 0);
        if (type == 1) {
            param.setPeriod(QuoteConstants.PERIOD_TYPE_DAY);
        } else if (type == 7) {
            param.setPeriod(QuoteConstants.PERIOD_TYPE_WEEK);
        } else if (type == 30) {
            param.setPeriod(QuoteConstants.PERIOD_TYPE_MONTH);
        } else {
            // 非日、周、月，返回空集合
            return new ArrayList<QueryDayDataBigIntRep>();
        }
        param.setDay((short) transferInteger(count, 0));
        // 起始条数设置为0[QueryDayDataBigInt [open_price=29.5, close_price=27.75,
        // max_price=29.8, min_price=27.0, total=1740850, money=4859102000,
        // date=19991110]
        param.setBegin(0);
        List<QueryDayDataBigInt> list = QuoteInfoUtils
            .toDayDataBigInt(stockQuoteAO.queryDayDataBigInt(param));
        List<QueryDayDataBigIntRep> resultList = mapper.mapper(list,
                QueryDayDataBigIntRep.class);
        return resultList;
    }

    /**
     * 实时数据
     * @param stockCode
     * @return
     *@create：2014-7-15 下午4:34:52 chensz11448
     *@history
     */
    @RequestMapping(value = "/{stockCode}/real_time_data", method = RequestMethod.GET)
    @ResponseBody
    public QueryRealTimeDataRep queryRealTimeData(@PathVariable String stockCode) {
        QueryRealTimeDataDto param = new QueryRealTimeDataDto();
        param.setStock_code(stockCode);
        QueryRealTimeData data = QuoteInfoUtils.toRealTimeData(
                stockQuoteAO.queryRealTimeData(param),
                stockQuoteAO.queryStockChange(param));
        return mapper.mapper(data, QueryRealTimeDataRep.class);
    }

    /**
     * 批量股票的实时数据
     * @param stockCodes
     * @return
     *@create：2014-8-4 下午5:03:08 chensz11448
     *@history
     */
    @RequestMapping(value = "/{stockCodes}/batch_data", method = RequestMethod.GET)
    @ResponseBody
    public List<QueryRealTimeDatasRep> queryRealTimeDatas(
            @PathVariable String stockCodes) {
        String[] strs = stockCodes.split(",");
        List<QueryRealTimeDatasRep> result = new ArrayList<QueryRealTimeDatasRep>();
        for (String stockCode : strs) {
            QueryRealTimeDataDto param = new QueryRealTimeDataDto();
            param.setStock_code(stockCode);
            QueryRealTimeData data = QuoteInfoUtils.toRealTimeData(
                stockQuoteAO.queryRealTimeData(param),
                stockQuoteAO.queryStockChange(param));
            QueryRealTimeDatasRep rep = mapper.mapper(data,
                QueryRealTimeDatasRep.class);
            if (rep != null) {
                result.add(rep);
            }
        }
        return result;
    }

    /**
     * 分时
     * @param stockCode
     * @return
     *@create：2014-7-15 下午8:27:42 chensz11448
     *@history
     */
    @RequestMapping(value = "/{stockCode}/trend_data", method = RequestMethod.GET)
    public Map<String, Object> queryTrendData(@PathVariable String stockCode) {
        QueryRealTimeDataDto param = new QueryRealTimeDataDto();
        param.setStock_code(stockCode);

        Map<String, Object> maps = new HashMap<String, Object>();
        AnsTrendData ansTrendData = stockQuoteAO.queryTrendData(param);
        List<QueryTrendData> trendList = QuoteInfoUtils.toTrendData(
            ansTrendData, stockCode);
        mapper.mapper(trendList, QueryTrendDataRep.class);
        maps.put("trendList", mapper.mapper(trendList, QueryTrendDataRep.class));
        QueryPartRealTimeData partRealTimeData = QuoteInfoUtils
            .toPartRealTimeData(ansTrendData, stockCode);

        maps.put("partRealTimeData", partRealTimeData);
        return maps;
    }

    /**
     * 键盘精灵
     * @param character
     * @return
     *@create：2014-7-16 上午11:07:32 chensz11448
     *@history
     */
    @RequestMapping(value = "/stock_data/{character}", method = RequestMethod.GET)
    @ResponseBody
    public List<QueryDataByCodeRep> queryDataByCode(
            @PathVariable String character) {
        if (character != null) {
            character = character.toUpperCase();
        }
        Map<String, StockInitInfo> shcodes = QuoteConfig.getInstance()
            .getShCodesMap();
        Map<String, StockInitInfo> szcodes = QuoteConfig.getInstance()
            .getSzCodesMap();
        Map<String, StockInitInfo> codes = new HashMap<String, StockInitInfo>();
        codes.putAll(szcodes);
        codes.putAll(shcodes);
        List<QueryDataByCodeRep> result = new ArrayList<QueryDataByCodeRep>();
        for (String code : codes.keySet()) {
            StockInitInfo stock = codes.get(code);
            QueryDataByCodeRep data = new QueryDataByCodeRep();
            data.setStockCode(stock.getCode().getCode());
            data.setStockName(stock.getStockName());
            if ((data.getStockCode().indexOf(character) == 0 || stock
                .getStockPinyin().indexOf(character) == 0)
                    && stock.getCode().getKind() != INDEX) {
                result.add(data);
            }
            // 默认取10条记录
            if (result.size() >= SIZE) {
                break;
            }
        }
        return result;
    }

    @RequestMapping(value = "/all_stock_data", method = RequestMethod.GET)
    @ResponseBody
    public List<QueryAllDataRep> queryAllData() {
        Map<String, StockInitInfo> shcodes = QuoteConfig.getInstance()
            .getShCodesMap();
        Map<String, StockInitInfo> szcodes = QuoteConfig.getInstance()
            .getSzCodesMap();
        Map<String, StockInitInfo> codes = new HashMap<String, StockInitInfo>();
        codes.putAll(szcodes);
        codes.putAll(shcodes);
        List<QueryAllDataRep> result = new ArrayList<QueryAllDataRep>();
        for (String code : codes.keySet()) {
            StockInitInfo stock = codes.get(code);
            QueryAllDataRep data = new QueryAllDataRep();
            data.setStockCode(stock.getCode().getCode());
            data.setStockName(stock.getStockName());
            data.setStockPy(stock.getStockPinyin());
            result.add(data);
        }
        return result;
    }

    /**
     * 将字符串转化为数字
     * @param num
     * @return
     * @create：2014-7-20 下午2:03:54 chensz11448
     * @history
     */
    private int transferInteger(String num, int defaultValue) {
        if (num != null && !"".equals(num)) {
            try {
                return Integer.parseInt(num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    /**
     * 获取财猫行情实时成交
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/market_match", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String queryMarketMatch(HttpServletRequest request) throws Exception{
        String payload = RequestUtils.getBody(request);
        return RequestUtils.getMarket(payload);
    }
}

package com.caimao.bana.server.service.ybk;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.dubbo.common.json.JSONObject;
import com.caimao.bana.api.entity.req.ybk.FYBKQueryArticleListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkExchangeQueryListReq;
import com.caimao.bana.api.entity.ybk.*;
import com.caimao.bana.api.enums.EYbkExchangeStatus;
import com.caimao.bana.api.enums.ybk.EArticleCategory;
import com.caimao.bana.api.service.ybk.IYBKJobService;
import com.caimao.bana.api.service.ybk.IYBKLineService;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.bana.api.service.ybk.IYBKSummaryService;
import com.caimao.bana.server.dao.ybk.YBKArticleDao;
import com.caimao.bana.server.dao.ybk.YBKGoodsDao;
import com.caimao.bana.server.dao.ybk.YBKKLineDao;
import com.caimao.bana.server.utils.DateUtil;
import com.caimao.bana.server.utils.HttpClientUtils;
import com.caimao.bana.server.utils.StringProcessUtils;
import com.caimao.bana.server.utils.YbkUtil;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("ybkJobService")
public class YBKJobServiceImpl implements IYBKJobService {
    private static final Logger logger = LoggerFactory.getLogger(YBKJobServiceImpl.class);
    private static final String tempStr= "综合指数";
    private static final String tempPartStr= "指数";
    private static final String wzxqUrl= "http://www.youbizixun.com/plus/wzhx.php/?aid=";
    private static final String baikeUrl= "http://www.youbizixun.com/json/get/?cmd=2002&start=0&num=50&catid=20";
    private static final String priceUrl= "http://hangqing1.youbizixun.com/topic.php/json.index?start=0&num=100";

    @Resource
    private YBKGoodsDao ybkGoodsDao;

    @Resource
    private YBKKLineDao ybkkLineDao;

    @Resource
    private YBKArticleDao ybkArticleDao;

    @Resource
    private IYBKService YBKService;

    @Resource
    private IYBKLineService YBKLineService;

    @Resource
    private IYBKSummaryService ybkSummaryService;

    /**
     * 判断是否在交易日中
     * @param exchange
     * @return
     */
    private boolean isInTradeDay(YbkExchangeEntity exchange) throws Exception {
        // 使用自己写好的那个东东
        return YbkUtil.checkExchangeOpen(exchange);
//        int day = DateUtil.getDayOfWeek();
//        switch (exchange.getTradeDayType()){
//            case 1:
//                if (DateUtil.isWookDay() && day<=5){
//                    return true;
//                }
//                break;
//            case 2:
//                if (day<=5){
//                    return true;
//                }
//                break;
//            case 3:
//                return true;
//            case 4:
//                if (DateUtil.isWookDay() && day<=6){
//                    return true;
//                }
//                break;
//            default:
//                break;
//        }
//        return false;
    }

    @Override
    public void quotationData() throws Exception {
        logger.info("抓取行情数据开始");
        FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
        req.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> entityList = YBKService.queryExchangeList(req);
        for (final YbkExchangeEntity exchange:entityList){
            if(!isInTradeDay(exchange)){
                continue;
            }
            long amBeginTime = DateUtil.getTimeByHhMm(exchange.getAmBeginTime());
            long amEndTime = DateUtil.getTimeByHhMm(exchange.getAmEndTime());
            long pmBeginTime = DateUtil.getTimeByHhMm(exchange.getPmBeginTime());
            long pmEndTime = DateUtil.getTimeByHhMm(exchange.getPmEndTime());
            long now = System.currentTimeMillis()/1000;
            if ((now>=amBeginTime && now<=amEndTime)||(now>=pmBeginTime && now<=pmEndTime)){
                new Thread(){
                    @Override
                    public void run() {
                        String dataStr = HttpClientUtils.getString(exchange.getApiMarketUrl());
                        if (dataStr.isEmpty()){
                            logger.info("没有抓到" + exchange.getName()+"的行情数据，可能是地址不对");
                        }
                        try{
                            List<YBKTimeLineEntity> list = parseToList(exchange,dataStr);
                            for(YBKTimeLineEntity entity:list){
                                YBKLineService.updateKLine(entity);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
        logger.info("抓取行情数据结束");
    }

    private List<YBKTimeLineEntity> parseToList(YbkExchangeEntity exchange, String dataStr)throws Exception{
        List<YBKTimeLineEntity> list = new ArrayList();
        try{
            if (exchange.getApiMarketUrl().endsWith("delay_hq.htm") || exchange.getApiMarketUrl().endsWith("display_hq.htm")){// 解析页面
                parseHtml(exchange,dataStr, list);
            }else if(exchange.getApiMarketUrl().endsWith("quotation.html")){// 解析字符串
                parseString(exchange,dataStr, list);
            }else if(exchange.getApiMarketUrl().endsWith("getHqV_lbData.jsp")){// 解析json
                parseJsonOne(exchange, dataStr, list);
            }else if(exchange.getApiMarketUrl().endsWith("refreshHQ")) {// 解析json
                parseJsonTwo(exchange, dataStr, list);
            } else if (exchange.getApiMarketUrl().contains("epianhong")) {
                parseJsonFour(exchange, dataStr, list);
            }else if(exchange.getApiMarketUrl().endsWith("queryQuotations.do")){// 解析json
                parseJsonThree(exchange,dataStr,list);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     *  解析页面
     * @param dataStr
     * @param list
     * @throws Exception
     */
    private void parseHtml(YbkExchangeEntity exchange,String dataStr, List<YBKTimeLineEntity> list) throws Exception{
        Date date = new Date();
        Parser parser = new Parser(dataStr);
        TagNameFilter tableFilter = new TagNameFilter("Table");// table过滤器
        NodeList tableList = parser.extractAllNodesThatMatch(tableFilter);

        Node table = tableList.elementAt(1);// 取第二个table
        TagNameFilter trFilter = new TagNameFilter("Tr");// tr过滤器
        NodeList trList = table.getChildren().extractAllNodesThatMatch(trFilter);

        SimpleNodeIterator trIterator = trList.elements();
        TagNameFilter tdFilter = new TagNameFilter("Td");// td过滤器
        while(trIterator.hasMoreNodes()) {
            NodeList tdList = trIterator.nextNode().getChildren().extractAllNodesThatMatch(tdFilter);
            long currentPrice = numberFormat(tdList.elementAt(5).getChildren().toHtml());
            if (currentPrice<=0){
                continue;
            }
            YBKTimeLineEntity entity = new YBKTimeLineEntity();
            entity.setCode(tdList.elementAt(1).getChildren().toHtml().trim());
            entity.setExchangeName(exchange.getShortName());
            entity.setDatetime(date);
            entity.setYesterBalancePrice(numberFormat(tdList.elementAt(3).getChildren().toHtml()));
            entity.setOpenPrice(numberFormat(tdList.elementAt(4).getChildren().toHtml()));
            entity.setCurPrice(currentPrice);
            entity.setCurrentGains(numberFormat(tdList.elementAt(6).getChildren().toHtml()));
            entity.setTotalAmount(numberFormat(tdList.elementAt(7).getChildren().toHtml()));
            entity.setTotalMoney(numberFormat(tdList.elementAt(8).getChildren().toHtml()));
            entity.setHighPrice(numberFormat(tdList.elementAt(9).getChildren().toHtml()));
            entity.setLowPrice(numberFormat(tdList.elementAt(10).getChildren().toHtml()));
            list.add(entity);
            String goodName = tdList.elementAt(2).getChildren().toHtml();
            if (goodName.indexOf(tempStr)>-1){// 综合指数
                insertSummary(exchange,entity);
            }
            // 商品数据
            insertGoods(exchange,entity,goodName);
        }
    }

    /**
     * 解析字符串
     * @param dataStr
     * @param list
     */
    private void parseString(YbkExchangeEntity exchange, String dataStr, List<YBKTimeLineEntity> list){
        Date date = new Date();
        String[] data = dataStr.split("\\|");
        for (String dt:data){
            String[] elements = dt.split(",");
            if (elements.length > 1){
                long currentPrice = numberFormat(elements[4]);
                if (currentPrice<=0){
                    continue;
                }
                YBKTimeLineEntity entity = new YBKTimeLineEntity();
                entity.setCode(elements[0]);
                entity.setExchangeName(exchange.getShortName());
                entity.setDatetime(date);
                entity.setYesterBalancePrice(numberFormat(elements[2]));
                entity.setOpenPrice(numberFormat(elements[3]));
                entity.setCurPrice(currentPrice);
                entity.setCurrentGains((numberFormat(elements[5])));
                entity.setTotalAmount( numberFormat(elements[6]));
                entity.setTotalMoney(numberFormat(elements[7]));
                entity.setHighPrice(numberFormat(elements[8]));
                entity.setLowPrice(numberFormat(elements[9]));
                list.add(entity);
                if (elements[1].indexOf(tempStr)>-1){// 综合指数
                    insertSummary(exchange,entity);
                }
                // 商品数据
                insertGoods(exchange,entity,elements[1]);
            }
        }
    }

    private void parseJsonOne(YbkExchangeEntity exchange, String dataStr, List<YBKTimeLineEntity> list) throws Exception{
        Date date = new Date();
        JSONArray array = ((JSONObject) JSON.parse(dataStr)).getArray("tables");
        for (int i=0;i<array.length();i++){
            JSONObject obj = array.getObject(i);
            long CurPrice = numberFormat(obj.getString("CurPrice"));
            if (CurPrice<=0){
                continue;
            }
            long YesterBalancePrice = numberFormat(obj.getString("YesterBalancePrice"));
            long OpenPrice = numberFormat(obj.getString("OpenPrice"));
            long CurrentGains = numberFormat(obj.getString("CurrentGains"));
            long TotalAmount = numberFormat(obj.getString("TotalAmount"));
            long TotalMoney = numberFormat(obj.getString("TotalMoney"));
            long HighPrice = numberFormat(obj.getString("HighPrice"));
            long LowPrice = numberFormat(obj.getString("LowPrice"));
            YBKTimeLineEntity entity = new YBKTimeLineEntity(exchange.getShortName(), obj.getString("code"), date, YesterBalancePrice, OpenPrice, CurPrice,CurrentGains, TotalAmount, TotalMoney, HighPrice, LowPrice);
            list.add(entity);
            if (obj.getString("fullname").indexOf(tempStr)>-1){// 综合指数入库
                insertSummary(exchange,entity);
            }
            // 商品数据
            insertGoods(exchange,entity,obj.getString("fullname"));
        }
    }

    private void parseJsonTwo(YbkExchangeEntity exchange, String dataStr, List<YBKTimeLineEntity> list) throws Exception{
        Date date = new Date();
        JSONArray array = ((JSONObject) JSON.parse(dataStr)).getArray("tables");
        for (int i=0;i<array.length();i++){
            JSONObject obj = array.getObject(i);
            long CurPrice = numberFormat(obj.getString("cp"));
            if (CurPrice<=0){
                continue;
            }
            long YesterBalancePrice = numberFormat(obj.getString("ybp"));
            long OpenPrice = numberFormat(obj.getString("op"));
            long CurrentGains = numberFormat(obj.getString("cg"));
            long TotalAmount = numberFormat(obj.getString("ta"));
            long TotalMoney = numberFormat(obj.getString("tm"));
            long HighPrice = numberFormat(obj.getString("hp"));
            long LowPrice = numberFormat(obj.getString("lp"));
            YBKTimeLineEntity entity = new YBKTimeLineEntity(exchange.getShortName(), obj.getString("c"), date, YesterBalancePrice, OpenPrice, CurPrice, CurrentGains, TotalAmount, TotalMoney, HighPrice, LowPrice);
            list.add(entity);
            if (obj.getString("fn").indexOf(tempStr)>-1){// 综合指数
                insertSummary(exchange,entity);
            }
            // 商品数据
            insertGoods(exchange,entity,obj.getString("fn"));
        }
    }

    private void parseJsonThree(YbkExchangeEntity exchange, String dataStr, List<YBKTimeLineEntity> list) throws Exception{
        Date date = new Date();
        JSONArray array = ((JSONObject) JSON.parse(dataStr)).getArray("data");
        for (int i=0;i<array.length();i++){
            JSONObject obj = array.getObject(i);
            long CurPrice = numberFormat(obj.getString("newestPrice"));
            if (CurPrice<=0){
                continue;
            }
            long YesterBalancePrice = numberFormat(obj.getString("yclosePrice"));
            long OpenPrice = numberFormat(obj.getString("openPrice"));
            String currentGains = obj.getString("advanceDecline").replaceAll("<span class='arrow'><\\/span>", "").replaceAll("%", "");
            long CurrentGains = numberFormat(currentGains);
            String totalAmount = obj.getString("totalVolume").replaceAll("<b>", "").replaceAll("<\\/b>", "");
            long TotalAmount = numberFormat(totalAmount);
            String totalMoney = obj.getString("totalCost").replaceAll("<b>", "").replaceAll("<\\/b>", "");
            long TotalMoney = numberFormat(totalMoney);
            long HighPrice = numberFormat(obj.getString("maxPrice"));
            long LowPrice = numberFormat(obj.getString("minPrice"));
            YBKTimeLineEntity entity = new YBKTimeLineEntity(exchange.getShortName(), obj.getString("goodsId"), date, YesterBalancePrice, OpenPrice, CurPrice, CurrentGains, TotalAmount, TotalMoney, HighPrice, LowPrice);
            list.add(entity);
            if(obj.getString("goodsName").indexOf(tempStr)>-1){// 综合指数
                insertSummary(exchange,entity);
            }
            // 商品数据
            insertGoods(exchange,entity,obj.getString("goodsName"));
        }
    }

    private void parseJsonFour(YbkExchangeEntity exchange, String dataStr, List<YBKTimeLineEntity> list) throws Exception{
        Date date = new Date();
        JSONArray array = ((JSONObject) JSON.parse(dataStr)).getArray("goods");
        for (int i=0;i<array.length();i++){
            JSONObject obj = array.getObject(i);
            String name = new String(obj.getString("name").getBytes("ISO-8859-1"), "UTF-8");
            long CurPrice = numberFormat(obj.getString("recentprice"));
            if (CurPrice<=0){
                continue;
            }
            long YesterBalancePrice = numberFormat(obj.getString("ydclosingprice"));
            long OpenPrice = numberFormat(obj.getString("openingprice"));
            long TotalAmount = numberFormat(obj.getString("dealcount"));
            long TotalMoney = numberFormat(obj.getString("dealprice"));
            long HighPrice = numberFormat(obj.getString("topprice"));
            long LowPrice = numberFormat(obj.getString("bottomprice"));
            long CurrentGains = numberFormat(String.valueOf((Double.valueOf(obj.getString("recentprice")) - Double.valueOf(obj.getString("openingprice"))) / Double.valueOf(obj.getString("openingprice")) * 100));
            YBKTimeLineEntity entity = new YBKTimeLineEntity(exchange.getShortName(), obj.getString("code"), date, YesterBalancePrice, OpenPrice, CurPrice, CurrentGains, TotalAmount, TotalMoney, HighPrice, LowPrice);
            list.add(entity);
            if (name.contains(tempStr)){// 综合指数
                insertSummary(exchange,entity);
            }
            // 商品数据
            insertGoods(exchange,entity,name);
        }
    }

    private long numberFormat(String num){
        num = num.trim();
        if ("&mdash;".equals(num) || "-".equals(num)){
            return 0;
        }
        return (long)(Double.parseDouble(num)*100);
    }

    /**
     * 综合指数入库
     * @param exchange
     * @param entity
     */
    private void insertSummary(YbkExchangeEntity exchange, YBKTimeLineEntity entity){
        try {
            YBKSummaryEntity summary = ybkSummaryService.queryById(exchange.getId());
            if(summary==null){
                summary = new YBKSummaryEntity();
                summary.setExchangeId(exchange.getId());
                summary.setShortName(exchange.getShortName());
                summary.setExchangeName(exchange.getName());
                summary.setYesterBalancePrice(entity.getYesterBalancePrice());
                summary.setOpenPrice(entity.getOpenPrice());
                summary.setCurPrice(entity.getCurPrice());
                summary.setCurrentGains(entity.getCurrentGains());
                summary.setTotalAmount(entity.getTotalAmount());
                summary.setTotalMoney(entity.getTotalMoney());
                summary.setHighPrice(entity.getHighPrice());
                summary.setLowPrice(entity.getLowPrice());
                ybkSummaryService.insert(summary);
            }else{
                summary.setShortName(exchange.getShortName());
                summary.setExchangeName(exchange.getName());
                summary.setYesterBalancePrice(entity.getYesterBalancePrice());
                summary.setOpenPrice(entity.getOpenPrice());
                summary.setCurPrice(entity.getCurPrice());
                summary.setCurrentGains(entity.getCurrentGains());
                summary.setTotalAmount(entity.getTotalAmount());
                summary.setTotalMoney(entity.getTotalMoney());
                summary.setHighPrice(entity.getHighPrice());
                summary.setLowPrice(entity.getLowPrice());
                ybkSummaryService.update(summary);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 商品数据入库
     * @param exchange
     * @param entity
     * @param goodName
     */
    private void insertGoods(YbkExchangeEntity exchange, YBKTimeLineEntity entity, String goodName){
        try {
            YBKGoodsEntity goodsEntity = ybkGoodsDao.queryGood(exchange.getId(), entity.getCode());
            if (goodsEntity == null){
                goodsEntity = new YBKGoodsEntity();
                goodsEntity.setExchangeId(exchange.getId());
                goodsEntity.setExchangeName(exchange.getName());
                goodsEntity.setShortName(exchange.getShortName());
                goodsEntity.setGoodCode(entity.getCode());
                goodsEntity.setGoodName(goodName);
                goodsEntity.setGoodPinyin(StringProcessUtils.StringToPinyin(goodName));
                ybkGoodsDao.insert(goodsEntity);
            }else{
                goodsEntity.setExchangeName(exchange.getName());
                goodsEntity.setShortName(exchange.getShortName());
                goodsEntity.setGoodName(goodName);
                goodsEntity.setGoodPinyin(StringProcessUtils.StringToPinyin(goodName));
                ybkGoodsDao.update(goodsEntity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void articleData() throws Exception {
        logger.info("抓取文章数据开始");
        // 公告抓取
        FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
        req.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> entityList = YBKService.queryExchangeList(req);
        for (YbkExchangeEntity exchange:entityList){
            // 修改个别交易所名称
            if (exchange.getName().equals("广州邮币卡")) {
                exchange.setName("广州文交所");
            }
            if (exchange.getName().equals("汉唐邮币卡")) {
                exchange.setName("汉唐文交所");
            }

            List<YBKArticleEntity> list = ariticleData(exchange,null);
            for (YBKArticleEntity entity:list){
                if (entity.getTitle().indexOf("申购")>-1){// 申购公告
                    entity.setCategoryId(Integer.parseInt(EArticleCategory.DXSG.getCode()));
                }else  if (entity.getTitle().indexOf("公告")>-1){//
                    entity.setCategoryId(Integer.parseInt(EArticleCategory.TPGG.getCode()));
                    // 不再抓取 停牌公告、市场价格、网站百科
                    continue;
                }else {
                    continue;
                }
                //entity.setIsShow(1);
                YBKService.articleInsert(entity);
            }
        }
        // 不再抓取 停牌公告、市场价格、网站百科
        // 百科抓取
//        YbkExchangeEntity exchange = new YbkExchangeEntity();
//        exchange.setId(0);
//        exchange.setArticleUrl(baikeUrl);
//        List<YBKArticleEntity> list = ariticleData(exchange,Integer.parseInt(EArticleCategory.WZBK.getCode()));
//        for (YBKArticleEntity entity:list){
//            entity.setCategoryId(Integer.parseInt(EArticleCategory.WZBK.getCode()));
//            YBKService.articleInsert(entity);
//        }

        // 不再抓取 停牌公告、市场价格、网站百科
        // 市场价格抓取
//        getPriceData();
        logger.info("抓取文章数据结束");
    }

    /**
     * 文章抓取
     * @throws Exception
     */
    private List<YBKArticleEntity> ariticleData(YbkExchangeEntity exchange, Integer categoryId) {
        List<YBKArticleEntity> list = new ArrayList<YBKArticleEntity>();
        String dataStr = HttpClientUtils.getString(exchange.getArticleUrl());
        if (dataStr.isEmpty()){
            logger.info("没有抓到"+exchange.getName()+"的文章数据，可能是地址不对");
            return list;
        }
        try{
            long timeStart = getLastTime(exchange.getId(),categoryId);// 数据库中最新的更新时间
            JSONObject jsonObject= ((JSONObject) JSON.parse(dataStr));
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()){
                String key = iterator.next();
                if (!key.matches("\\d*")){// 不是数字
                    continue;
                }
                JSONObject value = jsonObject.getObject(key);
                long time = Long.parseLong(value.getString("pubdate"))* 1000;
                if (time <= timeStart){// 时间过期
                    continue;
                }
                String content = HttpClientUtils.getString(wzxqUrl + value.getString("id"));
                if (content.isEmpty()){// 没有得到详情
                    continue;
                }
                if (exchange.getName()!=null && value.getString("source").indexOf(exchange.getName())==-1){
                    continue;
                }
                YBKArticleEntity entity = new YBKArticleEntity();
                entity.setExchangeId(exchange.getId());
                entity.setExchangeName(value.getString("source"));
                entity.setContent(content);
                entity.setIsShow(0);
                entity.setTitle(value.getString("title"));
                entity.setCreated(new Date(time));
                list.add(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获得市场价格数据
     */
    private  void getPriceData() {
        String dataStr = HttpClientUtils.getString(priceUrl);
        if (dataStr.isEmpty()){
            logger.info("没有抓到市场价格数据，可能是地址不对");
            return;
        }
        try {
            long timeStart = getLastTime(0,Integer.parseInt(EArticleCategory.SCJG.getCode()));// 数据库中最新的更新时间
            JSONArray jsonArray= ((JSONArray) JSON.parse(dataStr));
            for(int i=0;i<jsonArray.length();i++){
                JSONObject value = jsonArray.getObject(i);
                String cjdate = value.getString("cjdate");
                if (cjdate==null||cjdate.isEmpty()){
                    continue;
                }
                long time = DateUtil.getDatetime(cjdate);
                if (time <= timeStart){// 时间过期
                    continue;
                }
                YBKArticleEntity entity = new YBKArticleEntity();
                entity.setIsShow(0);
                entity.setExchangeId(0);
                entity.setTitle(value.getString("title"));
                entity.setCategoryId(Integer.parseInt(EArticleCategory.SCJG.getCode()));
                entity.setCreated(new Date(time));
                YBKService.articleInsert(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获得数据库中最新的更新时间
     * @param exchangeId
     * @param categoryId
     * @return
     */
    private long getLastTime(Integer exchangeId,Integer categoryId)throws Exception{
        long timeStart = 0;// 数据库中最新的更新时间

        YBKArticleEntity articleEntity = this.ybkArticleDao.queryLastTime(exchangeId, categoryId);
        if (articleEntity != null) {
            timeStart = articleEntity.getCreated().getTime();
        }
//        FYBKQueryArticleListReq articleListReq = new FYBKQueryArticleListReq();
//        articleListReq.setStart(0);
//        articleListReq.setLimit(1);
//        articleListReq.setExchangeId(exchangeId);
//        articleListReq.setCategoryId(categoryId);
//        List<YBKArticleEntity> articleList = YBKService.queryArticleWithPage(articleListReq).getItems();
//
//        if (articleList!=null && articleList.size()>0){
//            timeStart = articleList.get(0).getCreated().getTime();
//        }
        return timeStart;
    }

    /**
     * 修复k线
     * @throws Exception
     */
    public void repairKline()throws Exception{
        Map<String,String> urlMap = new HashMap<>();
//        urlMap.put("njwjs","http://newapi.youbizixun.com/json/interface.php?api.kline/nanjing&code=");
//        urlMap.put("nfwjs","http://newapi.youbizixun.com/json/interface.php?api.kline/nanfang&code=");
        urlMap.put("hxwjs","http://newapi.youbizixun.com/json/interface.php?api.kline/haixi&code=");
//        urlMap.put("jswjs","http://newapi.youbizixun.com/json/interface.php?api.kline/jiangsu&code=");
        urlMap.put("shybk","http://newapi.youbizixun.com/json/interface.php?api.kline/shanghai&code=");
//        urlMap.put("hzwjs","http://newapi.youbizixun.com/json/interface.php?api.kline/huazhong&code=");
//        urlMap.put("jmj","http://newapi.youbizixun.com/json/interface.php?api.kline/beijing&code=");
//        urlMap.put("bjsflt","http://newapi.youbizixun.com/json/interface.php?api.kline/bjfulite&code=");
//        urlMap.put("znwjs","http://newapi.youbizixun.com/json/interface.php?api.kline/zhongnan&code=");
//        urlMap.put("hxwjs1","http://newapi.youbizixun.com/json/interface.php?api.kline/huaxia&code=");


        FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
        req.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> entityList = YBKService.queryExchangeList(req);
        for (YbkExchangeEntity exchange:entityList){
            String u = urlMap.get(exchange.getShortName());
            if (u==null){
                System.out.println(exchange.getShortName()+"没有抓取地址");
                continue;
            }
            String dataStr = HttpClientUtils.getString(exchange.getApiMarketUrl());
            if (dataStr.isEmpty()){
                logger.info("没有抓到"+exchange.getName()+"的行情数据，可能是地址不对");
                continue;
            }
            List<YBKTimeLineEntity> list = parseToList(exchange,dataStr);
            for (YBKTimeLineEntity ybkTimeLineEntity:list){
                dataStr = HttpClientUtils.getString(u + ybkTimeLineEntity.getCode());
                if (dataStr.isEmpty()){
                    logger.info("没有抓到"+exchange.getName()+"-的code="+ybkTimeLineEntity.getCode()+"的行情数据，可能是地址不对");
                    continue;
                }
                JSONArray jsonArray= ((JSONArray) JSON.parse(dataStr));
                for (int i=0;i<jsonArray.length();i++){
                    saveKLine(exchange.getShortName(),ybkTimeLineEntity.getCode(),jsonArray.getObject(i));
                }
            }
        }
    }

    private void saveKLine(String shortName,String code,JSONObject data){
        try {
            long closePrice = numberFormat(data.getString("ClosePrice"));
            if (closePrice <= 0){
                return;
            }
            //整理数据
            YBKKLineEntity ybkkLineEntity = new YBKKLineEntity();
            ybkkLineEntity.setExchangeName(shortName);
            ybkkLineEntity.setCode(code);
            Date date = DateUtil.translate2Date(data.getString("date"), false);
            ybkkLineEntity.setDate(date);
            ybkkLineEntity.setUpdateTime(date);
            ybkkLineEntity.setCurPrice(closePrice);
            ybkkLineEntity.setClosePrice(closePrice);
            ybkkLineEntity.setOpenPrice(numberFormat(data.getString("OpenPrice")));
            ybkkLineEntity.setHighPrice(numberFormat(data.getString("HighPrice")));
            ybkkLineEntity.setLowPrice(numberFormat(data.getString("LowPrice")));
            ybkkLineEntity.setTotalAmount(numberFormat(data.getString("TotalAmount")));

//            ybkkLineEntity.setCurrentGains();
//            ybkkLineEntity.setTotalMoney();

            //更新或者插入数据
            YBKKLineEntity todayKLine = ybkkLineDao.queryExist(ybkkLineEntity);
            if(todayKLine == null){
                ybkkLineDao.insert(ybkkLineEntity);
            }else{
                ybkkLineDao.update(ybkkLineEntity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

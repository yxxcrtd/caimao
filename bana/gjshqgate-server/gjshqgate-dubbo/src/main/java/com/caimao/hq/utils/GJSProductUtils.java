package com.caimao.hq.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.caimao.gjs.api.entity.req.FQueryGjsHolidayReq;
import com.caimao.gjs.api.service.IGjsHolidayService;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.core.HQDataInit;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2015/9/30.
 */
@Service("gjsProductUtils")
public class GJSProductUtils {

    @Autowired
    public HolidayUtil holidayUtil;
    @Autowired
    public IGjsHolidayService gjsHolidayService;

    @Value("${njs.trade.time}")
    protected String njsTradeTime;

    @Value("${sjs.trade.time}")
    protected String sjsTradeTime;

    @Autowired
    private  JRedisUtil jredisUtil;

    private static Logger logger = LoggerFactory.getLogger(GJSProductUtils.class);

    public static Map<String,String> mapProduct=new HashMap();// KEY=SJS, Value= AG.SJS, AG.SJS, AG.SJS====
    public static Map<String,String> mapProductRedis=new HashMap();
    {
        mapProductRedis.put("NJS","njs_goods");
        mapProductRedis.put("SJS","sjs_goods");
        mapProductRedis.put("LIFFE","liffe_goods");
    }


    public static TreeMap<String, Map<String, Object>>  allProductTreeMap =new TreeMap();
    //key为交易所代码，TradeTime  为 当天的交易时间
    public  static Map<String,List<TradeTime>>   mapHoliday=new HashMap();


    /**
     * 获取商品列表缓存
     * @param exchange 缓存key
     * @return HashMap
     * @throws Exception
     */
    public  LinkedHashMap<String, Map<String, Object>> getGoodsCache(String exchange) throws Exception {

        exchange=mapProductRedis.get(exchange);
        Object goodsCacheObj = jredisUtil.get(0,exchange);
        if (goodsCacheObj != null) {
            String goodsCacheStr = goodsCacheObj.toString();
            try {
                return JSON.parseObject(goodsCacheStr, LinkedHashMap.class);
            } catch (Exception e) {
                return null;
            }
        } else {
            logger.debug("获取交易所商品 " +exchange + " 为空 ");
            return null;
        }
    }


    public  List<Product> getProductList(String exchange){

        LinkedHashMap<String, Map<String, Object>> mapTreeMap=null;
        List<Product> list=null;
        try {

            mapTreeMap=getGoodsCache(exchange);
            list=convertTreeMapToList(mapTreeMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getProductString(String exchange){

          return mapProduct.get(exchange);
    }

    public  Product  getProduct(String exchange,String prodCode){
        LinkedHashMap<String, Map<String, Object>> mapTreeMap=null;
        Product product=null;
        try {

            mapTreeMap=getGoodsCache(exchange);
            if(mapTreeMap.containsKey(prodCode)){
                Map map=mapTreeMap.get(prodCode);
                if(null!=map){
                    product=new Product();
                    ClassUtil.setFieldValue(map, product);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }


    public  List<Product> getProductFromExchangeAll(){

        List<Product> all=new ArrayList();
        try {

            LinkedHashMap<String, Map<String, Object>> nsjMapTreeMap=getGoodsCache("NJS");
            all.addAll(convertTreeMapToList(nsjMapTreeMap));
            LinkedHashMap<String, Map<String, Object>> sjsMapTreeMap=getGoodsCache("SJS");
            all.addAll(convertTreeMapToList(sjsMapTreeMap));
            LinkedHashMap<String, Map<String, Object>> otherMapTreeMap=getGoodsCache("LIFFE");
            all.addAll(convertTreeMapToList(otherMapTreeMap));
            allProductTreeMap.putAll(sjsMapTreeMap);
            allProductTreeMap.putAll(nsjMapTreeMap);
            allProductTreeMap.putAll(otherMapTreeMap);
        } catch (Exception e) {
            logger.error("定时同步产品失败:getProductFromExchangeAll "+e.getMessage());
            e.printStackTrace();
            all=null;
         }
        return all;
    }


    public   List<Product> convertTreeMapToList(LinkedHashMap<String,Map<String, Object>> map){

        List<Product> listProduct=new ArrayList();
        if(null!=map) {
            Iterator titer = map.entrySet().iterator();

            while (titer.hasNext()) {
                try{
                    Map.Entry ent = (Map.Entry) titer.next();
                    String keyt = ent.getKey().toString();
                    Map<String, Object> valuet = (Map) ent.getValue();
                    if (null != valuet) {
                        Product product=new Product();
                        ClassUtil.setFieldValue(valuet, product);
                        product.setExchange((String)valuet.get("exchange"));
                        listProduct.add(product);
                    }
                }catch (Exception ex){
                    logger.error("Map to Bean error"+ex.getMessage());
                }

            }
        }
        return listProduct;
    }

    private List<TradeTime> formateTradeTime(String tradeTime){

        List<TradeTime> list=new ArrayList();
        if(!StringUtils.isBlank(tradeTime)){
            int length=0;
            String[] allArray=tradeTime.split(",");
            String nowDate=DateUtils.getNoTime("yyyyMMdd");
            for(String str:allArray){
                if(str.length()==9&&str.contains("-")){
                    TradeTime temp=new TradeTime();
                    temp.setOpentime(DateUtils.getTickTime(nowDate+str.split("-")[0],"yyyyMMddHHmm"));
                    temp.setClosetime(DateUtils.getTickTime(nowDate+str.split("-")[1],"yyyyMMddHHmm"));
                    list.add(temp);
                }else{
                    logger.error("交易时间格式设置错误：正确格式为0900-1100,1300-1500当前格式为："+tradeTime);
                }
            }
        }
        ComparatorHoliday ComparatorHoliday=new ComparatorHoliday();
        Collections.sort(list, ComparatorHoliday);
        return list;
    }
    private  void initHoliday(){

        try {

            FQueryGjsHolidayReq  req=new FQueryGjsHolidayReq();
            req.setHoliday(DateUtils.getNoTime("yyyyMMdd"));
            String strTradeTime ="";
            if(null!=gjsHolidayService){
                strTradeTime=gjsHolidayService.queryGjsHolidayByExchangeAndHoliday("NJS", DateUtils.getNoTime("yyyyMMdd"));
            }

            if (!StringUtils.isBlank(strTradeTime)) {
                mapHoliday.put("NJS", formateTradeTime(strTradeTime));
            } else {
                mapHoliday.put("NJS",formateTradeTime(initTradeTime("NJS")));
            }
            String strTradeTimeSJS="";
            if(null!=strTradeTimeSJS){
                strTradeTimeSJS = gjsHolidayService.queryGjsHolidayByExchangeAndHoliday("SJS", DateUtils.getNoTime("yyyyMMdd"));
            }

            if (!StringUtils.isBlank(strTradeTimeSJS)) {
                mapHoliday.put("SJS", formateTradeTime(strTradeTimeSJS));
            } else {
                mapHoliday.put("SJS",formateTradeTime(initTradeTime("SJS")));
            }

        } catch (Exception e) {
            logger.error("初始化节假日失败："+e.getMessage());
            e.printStackTrace();
        }

    }

    private  String initTradeTime(String exchange){
        String tradeTime="";
        if("njs".equalsIgnoreCase(exchange)){

            if(!StringUtils.isBlank(njsTradeTime)){
                tradeTime=njsTradeTime;
            }else{
                tradeTime="0000-0600,0900-2400";
            }

        }else if("sjs".equalsIgnoreCase(exchange)){
            if(!StringUtils.isBlank(sjsTradeTime)){
                tradeTime=sjsTradeTime;
            }else{
                tradeTime="0900-1130,1330-1530,2000-2359,0000-0230";
            }
        }
        return tradeTime;
    }
    public  void init(){

        try{
            initHoliday();
            sysProduct();
        }catch (Exception ex){

            logger.error(" 初始化失败:GJSProductUtils init 失败:"+ex.getMessage());
        }


    }

    private void  sysProduct(){
        try{
            List<Product> list=getProductFromExchangeAll();
            if(null!=list&&list.size()>0){

                mapProduct.clear();
                allProductTreeMap.clear();
                for(Product pro:list) {
                    if (null != pro) {

                        if (mapProduct.containsKey(pro.getExchange())) {

                            mapProduct.put(pro.getExchange(), mapProduct.get(pro.getExchange()) + "," + pro.getProdCode()+"."+pro.getExchange());
                        } else {
                            mapProduct.put(pro.getExchange(),pro.getProdCode()+"."+pro.getExchange());
                        }
                    }
                }
            }
            logger.info("初始化商品 mapOroduct : " + ToStringBuilder.reflectionToString(mapProduct));
        }catch (Exception ex){
            logger.error("同步产品失败:"+ex.getMessage());
        }
        logger.debug("初始化商品 mapOroduct : " + mapProduct.toString());
    }
    private Map<String,String> convertProduct(String prodCode){//prodCode 格式为 AG.SJS,ABK.SJS,==

        Map<String,String> mapPara=new HashMap<>();
        if(!StringUtils.isBlank(prodCode)){
            String[] str=prodCode.split(",");
            if(null!=str){
                for(String temp:str){
                    if(!StringUtils.isBlank(temp)&&temp.contains(".")){
                        if(mapPara.containsKey(temp.split("\\.")[1])){
                            mapPara.put(temp.split("\\.")[1],mapPara.get(temp.split("\\.")[1])+","+temp.split("\\.")[0]);
                        }else{
                            mapPara.put(temp.split("\\.")[1],temp.split("\\.")[0]);
                        }
                    }
                }
            }
        }
        return mapPara;
    }



    public List<String>  getMinueInCommonTradeTime(Snapshot begin,Snapshot end){

        long number=0;
        List<String> timeList=new ArrayList();
        if(begin!=null&&end!=null){
            number=DateUtils.getMinueSub(end.getMinTime(),begin.getMinTime(),"yyyyMMddHHmm");
        }
        if(number-1>0){
            String temp=end.getMinTime();
            for(int i=0;i<number-1;i++){

                String  time=DateUtils.addMinue(temp,-1) ;
                timeList.add(time);
                temp=time;
            }
        }
        return timeList;
    }







    public static Map<String, String> convertProductSingle (String prodCode) {
        Map<String, String> mapPara = new HashMap<>();
        if (!StringUtils.isBlank(prodCode)) {
            if (prodCode.contains(".")) {
                int index=prodCode.lastIndexOf(".");

                mapPara.put("prodCode",prodCode.substring(0,index));
                mapPara.put("exchange",prodCode.substring(index+1,prodCode.length()));
            }
        }
        return mapPara;
    }

    public static Map<String,String> convertProductMore(String prodCode){//prodCode 格式为 AG.SJS,ABK.SJS,==

        Map<String,String> mapPara=new HashMap<>();
        int index=0;
        if(!StringUtils.isBlank(prodCode)){
            String[] str=prodCode.split(",");
            String finaceMicT="";
            String prodCodeT="";
            if(null!=str){
                for(String temp:str){
                    if(!StringUtils.isBlank(temp)&&temp.contains(".")){
                        index=temp.lastIndexOf(".");
                        finaceMicT=temp.substring(index + 1, temp.length());
                        prodCodeT=temp.substring(0,index);
                        if(mapPara.containsKey(finaceMicT)){
                            mapPara.put(finaceMicT,mapPara.get(finaceMicT)+","+prodCodeT);
                        }else{
                            mapPara.put(finaceMicT,prodCodeT);
                        }
                    }
                }
            }
        }
        return mapPara;
    }

    public void  convertMapToList(Map<String,Snapshot> map,String[] prodCodArray,List<Snapshot> resultList){

        if(null!=prodCodArray&&null!=map&&null!=resultList){
            Product  temp=null;
            for(String str:prodCodArray){
                if(!StringUtils.isBlank(str)){
                    Snapshot  snapshot=map.get(str);
                    if(null==snapshot){
                        snapshot=new Snapshot();

                        snapshot.setProdName(String.valueOf(allProductTreeMap.get(convertProductSingle(str).get("prodCode")).get("prodName")));
                        snapshot.setProdCode(convertProductSingle(str).get("prodCode"));
                        snapshot.setExchange(convertProductSingle(str).get("exchange"));
                    }
                    resultList.add(snapshot);
                }
            }
        }
    }
    public void convertStrToMap(List<String> redisStr, String exchange,Map<String,Snapshot> map){
        Snapshot snapshot=null;
        if(null!=redisStr&&redisStr.size()>0&&null!=redisStr){
            for(String str:redisStr){
                if(!StringUtils.isBlank(str)){
                    snapshot= (Snapshot)JSON.parseObject(str, HQDataInit.exchangeToSnapshot.get(exchange));
                    if(null!=snapshot){

                        map.put(snapshot.getProdCode() + "." + snapshot.getExchange(), snapshot);
                    }
                }
            }
        }
    }

    public void convertStrToSnapObject(List<String> redisStr, List list){
        Snapshot snapshot=null;
        if(null!=redisStr&&redisStr.size()>0&&null!=list){
            for(String str:redisStr){

                if(!StringUtils.isBlank(str)){
                    if(str.contains("exchange\":\"NJS")){

                        snapshot= JSON.parseObject(str, NJSSnapshot.class);
                    }else if(str.contains("exchange\":\"SJS")){

                        snapshot= JSON.parseObject(str, SJSSnapshot.class);
                    } else if (str.contains("exchange\":\"LIFFE")){
                        snapshot= JSON.parseObject(str, OtherSnapshot.class);
                    }
                    
                    if(null!=snapshot){
                        try{
                            snapshot.setMinTime(String.valueOf(DateUtils.getTickTime(snapshot.getMinTime(), "yyyyMMddHHmmss")/1000));
                        }catch (Exception ex){
                            snapshot.setMinTime("");
                            logger.error(ex.getMessage());
                        }
                        list.add(snapshot);
                    }
                }
            }
        }
    }
    public void convertStrToSnapObject(List<String> redisStr,String exchange, List list){
        Snapshot snapshot=null;
        if(null!=redisStr&&redisStr.size()>0&&null!=list){
            for(String str:redisStr){

                if(!StringUtils.isBlank(str)){
                    snapshot= (Snapshot)JSON.parseObject(str, HQDataInit.exchangeToSnapshot.get(exchange));

                    if(null!=snapshot){
                        try{
                            snapshot.setMinTime(String.valueOf(DateUtils.getTickTime(snapshot.getMinTime(), "yyyyMMddHHmmss")/1000));
                        }catch (Exception ex){
                            snapshot.setMinTime("");
                            logger.error(ex.getMessage());
                        }
                        list.add(snapshot);
                    }
                }
            }
        }
    }
    public void convertStrToSnapObject(Set<String> redisStr, List list,Class className,Boolean isConvertMiniTimeToTicker){
        Snapshot snapshot=null;
        if(null!=redisStr&&redisStr.size()>0&&null!= list) {
            for (String str : redisStr) {

                if(!StringUtils.isBlank(str)){
                    snapshot= (Snapshot)JSON.parseObject(str, className);
                    try{
                        if(isConvertMiniTimeToTicker){
                            snapshot.setMinTime(String.valueOf(DateUtils.getTickTime(snapshot.getMinTime(), "yyyyMMddHHmmss")/1000));
                        }
                    }catch (Exception ex){
                        snapshot.setMinTime("");
                        logger.error(ex.getMessage());
                    }

                    if(null!=snapshot){
                        list.add(snapshot);
                    }
                }
            }
        }
    }
    public void convertStrToCandleObject(Set<String> redisStr, String exchange,List list){
        Candle candle=null;
        if(null!=redisStr&&redisStr.size()>0&&null!=list){
            for(String str:redisStr){
                if(!StringUtils.isBlank(str)){
                    candle= (Candle)JSON.parseObject(str, HQDataInit.exchangeToCandle.get(exchange));
                    if(null!=candle){
                        list.add(candle);
                    }
                }
            }
        }
    }

    public void  fillData(){

        List<Product> productsListNJS= getProductList("NJS");
        fillDataFromProductList(productsListNJS);

        List<Product> productsListSJS= getProductList("SJS");
        fillDataFromProductList(productsListSJS);

    }

    public void fillDataFromProductList(List<Product> productsList){

        if(null!=productsList&&productsList.size()>0){
            for(Product product:productsList){
                if(null!=product){
                    fillDataFromProduct(product);
                }
            }
        }

    }

    public void fillDataFromProduct(Product product){

        if(null!=product){
            String nowTime=DateUtils.getNoTime("yyyyMMddHHmm");
            holidayUtil.fillData(product.getExchange(),product.getProdCode(),nowTime, "1");//1分钟
            holidayUtil.fillData(product.getExchange(),product.getProdCode(),nowTime, "2");//5分钟
            holidayUtil.fillData(product.getExchange(),product.getProdCode(),nowTime, "3");//15分钟
            holidayUtil.fillData(product.getExchange(),product.getProdCode(),nowTime, "4");//30分钟
            holidayUtil.fillData(product.getExchange(),product.getProdCode(),nowTime, "5");//60分钟
            holidayUtil.fillData(product.getExchange(),product.getProdCode(),nowTime, "6");//日K
        }

    }
}

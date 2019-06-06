package com.caimao.hq.core;

import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.api.service.*;
import com.caimao.hq.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class HQDataInit {

	@Autowired
	private INJSCandleService njsCandleService;
	@Autowired
	private INJSSnapshotService njsSnapshotService;

	@Autowired
	private ISJSCandleService sjsCandleService;
	@Autowired
	private ISJSSnapshotService sjsSnapshotService;
	@Autowired
	private GJSProductUtils gjsProductUtils;
	@Autowired
	private JRedisUtil jredisUtil;
	@Autowired
	private ImportDataSJS importDataSJS;

	@Autowired
	private IOtherHQService otherHQService;

	@Autowired
	private ImportDataNJS importDataNJS;
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HQDataInit.class);
	public static Map<String,Class> exchangeToSnapshot = new HashMap();
	public static Map<String,Class> exchangeToCandle = new HashMap();

	public static String redisFilePathWin = "C:\\report\\data\\redis";
	public static  String dbFilePathWin = "C:\\report\\data\\db";
	//linux
	public static  String redisFilePathLinux = File.separator + "hbdata" + File.separator + "data" + File.separator + "report"+File.separator +"redis";
	public static String dbFilePathLinux = File.separator + "hbdata" + File.separator + "data" + File.separator +  "report"+File.separator +"db";

	static{

		exchangeToSnapshot.put("NJS",NJSSnapshot.class);
		exchangeToSnapshot.put("SJS",SJSSnapshot.class);
		exchangeToCandle.put("NJS",NJSCandle.class);
		exchangeToCandle.put("SJS",SJSCandle.class);
		exchangeToCandle.put("LIFFE", OtherCandle.class);

	}

	public static String getRedisFilePath(){

		if(FileUtils.isWinOs()){

			return  redisFilePathWin;

		}else{
			return redisFilePathLinux;
		}

	}

	public static String getDbFilePath(){

		if(FileUtils.isWinOs()){

			return  dbFilePathWin;

		}else{
			return dbFilePathLinux;
		}

	}
	private void createHQDataFolder(){

		if(FileUtils.isWinOs()){

			createFileFold(redisFilePathWin+ File.separator +"candle"+ File.separator +"njs");
			createFileFold(redisFilePathWin+File.separator +"candle"+ File.separator +"sjs");
			createFileFold(redisFilePathWin+File.separator +"snapshot"+ File.separator +"njs");
			createFileFold(redisFilePathWin+File.separator +"snapshot"+ File.separator +"sjs");

			createFileFold(dbFilePathWin+File.separator +"candle"+ File.separator +"njs");
			createFileFold(dbFilePathWin+File.separator +"candle"+ File.separator +"sjs");
			createFileFold(dbFilePathWin+File.separator +"snapshot"+ File.separator +"njs");
			createFileFold(dbFilePathWin+File.separator +"snapshot"+ File.separator +"sjs");


		}else{

			createFileFold(redisFilePathLinux+File.separator +"candle"+ File.separator +"njs");
			createFileFold(redisFilePathLinux+File.separator +"candle"+ File.separator +"sjs");
			createFileFold(redisFilePathLinux+File.separator +"snapshot"+ File.separator +"njs");
			createFileFold(redisFilePathLinux+File.separator +"snapshot"+ File.separator +"sjs");

			createFileFold(dbFilePathLinux+File.separator +"candle"+ File.separator +"njs");
			createFileFold(dbFilePathLinux+File.separator +"candle"+ File.separator +"sjs");
			createFileFold(dbFilePathLinux+File.separator +"snapshot"+ File.separator +"njs");
			createFileFold(dbFilePathLinux+File.separator +"snapshot"+ File.separator +"sjs");

		}
	}

	private static void createFileFold(String filePath){
		FileUtils.createDir(filePath);
	}
	/**
	 * 行情初始化,：
	 *                  1 ：初始化南交所，上交所，  如果redis里面没有数据就用DB的数据初始化
	 *                   2   ：  当数据库和当前Redis都没有数据的时候，就使用产品库来出初始化
	 * @param
	 * @throws Exception
	 */

	public void init(){
		try{
			//initProperty();
			createHQDataFolder();//创建行情数据文件夹，如果文件不存在。
			//reportData();
			clear();
			//initRedisAll();
			initRedisAddNoProduct();//初始化产品
		}catch (Exception ex){

			logger.error("启动初始化 HQDataInit init方法失败"+ex.getMessage());
		}


	}
   public void reportData(){

	   //Redis数据导入+修复（上金所）
	   importDataSJS.importRedisCandle();
	   importDataSJS.importRedisSnap();

	   //上金所 candle,snapshot导入
	   importDataSJS.importDBCandle();
	   importDataSJS.importDBSnap();
	   //南交所 candle  导入
	   importDataNJS.importDBCandle();
	   importDataNJS.importRedisCandle();

   }

	public void initRedisAddNoProduct(){

		initRedisAddNoProduct("NJS");
		initRedisAddNoProduct("SJS");
		//initRedisAddNoProduct("LIFFE");
	}
	public  void clear(){


		String is_clear_redis_last=ConfigUtils.getApplicationProperties().getProperty("is_clear_redis_last");
		if(StringUtils.isBlank(is_clear_redis_last)){
			is_clear_redis_last="true";
		}
		if(!com.alibaba.dubbo.common.utils.StringUtils.isBlank(is_clear_redis_last)&&is_clear_redis_last.equalsIgnoreCase("true")){
			redisClear("SJS");
			redisClear("NJS");
		}
	}
	public  void redisClear(String finance_mic){
		if(!StringUtils.isBlank(finance_mic)){

			List<Product> productsList= gjsProductUtils.getProductList(finance_mic);
			if(null!=productsList){
				for(Product product:productsList){
					clearRedisKeyFromProduct(product);
				}

			}
		}
	}
	public  void redisClearHistory(String finance_mic,String prodCode,CandleCycle cycle,String miniTime){
		if(!StringUtils.isBlank(finance_mic)){

			List<Product> productsList= gjsProductUtils.getProductList(finance_mic);
			if(null!=productsList){
				for(Product product:productsList){
					if(StringUtils.isBlank(prodCode)){
						clearRedisKeyHistoryFromProduct(product, miniTime,cycle);
					}else{
						if(product.getProdCode().equalsIgnoreCase(prodCode)){
							clearRedisKeyHistoryFromProduct(product, miniTime,cycle);
						}
					}
				}

			}
		}
	}

	public  void initRedisAddNoProduct(String finance_mic){
		if(!StringUtils.isBlank(finance_mic)){

			List<Product> productsList= gjsProductUtils.getProductList(finance_mic);
			if(null!=productsList){
				fillRedisKeyFromProductList(productsList);
			}
		}
	}


	private void  fillRedisKeyFromProductList(List<Product> listProduct){

		if(null!=listProduct){

			for(Product product:listProduct){

				fillRedisKeyFromProduct(product);

			}

		}

	}
	private void redisClear(List<String> redisKeyList){

		logger.debug("redis clear 开始执行:");
		if(null!=redisKeyList&&redisKeyList.size()>0){
			for(String redisKey:redisKeyList){
				try{
					jredisUtil.del(redisKey);
				}catch (Exception ex){
					logger.error("清理RedisKey 错误:"+ex.getMessage()+"|redisKey="+redisKey);
				}

			}
		}
	}

	private void initRedisAddNoProduct(List<String> redisKeyList,Product product){

		logger.debug("redis clear 开始执行:");
		if(null!=redisKeyList&&redisKeyList.size()>0){
			for(String redisKey:redisKeyList){
				try{
					if(!jredisUtil.exists(redisKey)){

						jredisUtil.setex(redisKey, "", 0);

					}
				}catch (Exception ex){
					logger.error("清理RedisKey 错误:"+ex.getMessage()+"|redisKey="+redisKey);
				}

			}
		}
	}
	private void fillRedisKeyFromProduct(Product product){

		if(null!=product){
			fiiRedisFromProduct(product,CandleCycle.Snap);
			fiiRedisFromProduct(product,CandleCycle.Minute1);
			fiiRedisFromProduct(product,CandleCycle.Minute5);
			fiiRedisFromProduct(product,CandleCycle.Minute30);
			fiiRedisFromProduct(product,CandleCycle.Hour1);
			fiiRedisFromProduct(product,CandleCycle.Hour4);
			fiiRedisFromProduct(product,CandleCycle.DayCandle);
			fiiRedisFromProduct(product,CandleCycle.Week);
			fiiRedisFromProduct(product,CandleCycle.Month);
			fiiRedisFromProduct(product,CandleCycle.Year);
		}
	}
	private void clearRedisKeyFromProduct(Product product){
		if(null!=product){
			clearRedisFromProduct(product, CandleCycle.Snap);
			clearRedisFromProduct(product, CandleCycle.Minute1);
			clearRedisFromProduct(product, CandleCycle.Minute5);
			clearRedisFromProduct(product, CandleCycle.Minute30);
			clearRedisFromProduct(product, CandleCycle.Hour1);
			clearRedisFromProduct(product, CandleCycle.Hour4);
			//因为是跨天的，一般数据日期有问题很少
//			clearRedisFromProduct(product, CandleCycle.DayCandle);
//			clearRedisFromProduct(product, CandleCycle.Week);
//			clearRedisFromProduct(product, CandleCycle.Month);
//			clearRedisFromProduct(product,CandleCycle.Year);
		}
	}

	private void clearRedisKeyHistoryFromProduct(Product product,String miniTime,CandleCycle cycle){
		if(null!=product){
			if(!StringUtils.isBlank(miniTime)&&miniTime.length()==12){
				clearRedisHistoryFromProduct(product, cycle,miniTime);
			}else{
				logger.error("clearRedisKeyHistoryFromProduct 错误，传入的miniTime长度必须为12位 ");
			}

		}
	}
	private void clearRedisFromProduct(Product product, CandleCycle cycle) {
		if (null != product){

			String redisKey=MinTimeUtil.getRedisKey(cycle,product.getExchange(),product.getProdCode());
			if(!StringUtils.isBlank(redisKey)){
				jredisUtil.del(redisKey);
			}
		}
	}

	private void clearRedisHistoryFromProduct(Product product, CandleCycle cycle,String miniTime) {
		if (null != product){

			String redisKey=MinTimeUtil.getRedisKeyHistory(cycle, product.getExchange(), product.getProdCode());
			if(!StringUtils.isBlank(redisKey)){

				Long score=DateUtils.getTickTime(miniTime,"yyyyMMddHHmm");
				try{
					Set<String> set=jredisUtil.zrangeByScore(redisKey,score,score);
					if(null!=set&&set.size()>0){
						jredisUtil.zrem(redisKey, (String[]) set.toArray(new String[set.size()]));
					}
				}catch (Exception ex){

					logger.error("isExistToDelete信息异常  {}"+ex.getMessage());
				}
			}
		}
	}
	private void fiiRedisFromProduct(Product product, CandleCycle cycle){

	   if(null!=product){
		   String redisKey=MinTimeUtil.getRedisKey(cycle,product.getExchange(),product.getProdCode());
		   if(cycle!=CandleCycle.Snap){

			   if(!jredisUtil.exists(redisKey)){
				   try {
					   Candle candle= (Candle)getCandleClassByExchange(product.getExchange()).newInstance();
					   candle.setExchange(product.getExchange());
					   candle.setProdCode(product.getProdCode());
					   candle.setProdName(product.getProdName());
					   candle.setMinTime(DateUtils.getNoTime("yyyyMMddHHmm"));
					   candle.setRedisKey(redisKey);
					   String jsonString = JSON.toJSONString(candle);
					   jredisUtil.setex(redisKey, jsonString, 0);
				   } catch (Exception e) {
					   logger.error("初始化redis失败:"+e.getMessage());
					   e.printStackTrace();
				   }
			   }
		   }else{

			   if(!jredisUtil.exists(redisKey)){
				   try {
					   Snapshot snapshot= (Snapshot)getSnapshotClassByExchange(product.getExchange()).newInstance();
					   snapshot.setExchange(product.getExchange());
					   snapshot.setProdCode(product.getProdCode());
					   snapshot.setProdName(product.getProdName());
					   snapshot.setRedisKey(redisKey);
					   snapshot.setMinTime(DateUtils.getNoTime("yyyyMMddHHmm"));
					   String jsonString = JSON.toJSONString(snapshot);
					   jredisUtil.setex(redisKey, jsonString, 0);
				   } catch (Exception e) {
					   logger.error("初始化redis失败:"+e.getMessage());
					   e.printStackTrace();
				   }
			   }
		   }
	   }
   }

	private Class getCandleClassByExchange(String exchange){

		Class classT=null;
		if("njs".equalsIgnoreCase(exchange)){

			classT= NJSCandle.class;
		}else if("sjs".equalsIgnoreCase(exchange)){

			classT= SJSCandle.class;
		}else{
			throw new RuntimeException("初始化交易所产品到redis错误，传入不可识别的交易所代码"+exchange);
		}
		return classT;
	}

	private Class getSnapshotClassByExchange(String exchange){

		Class classT=null;
		if("njs".equalsIgnoreCase(exchange)){

			classT= NJSSnapshot.class;
		}else if("sjs".equalsIgnoreCase(exchange)){

			classT= SJSSnapshot.class;
		}else{
			throw new RuntimeException("初始化交易所产品到redis错误，传入不可识别的交易所代码"+exchange);
		}
		return classT;
	}
	public void initRedisAll(){

		initSJS();
		initNJS();

	}
	private void initNJS(){
		njsSnapshotService.redisInit("NJS");
		njsCandleService.redisInit("NJS");
	}
	private void initSJS(){
		sjsSnapshotService.redisInit("SJS");
		sjsCandleService.redisInit("SJS");
	}


	HQDataInit() {
//		try{
//			Timer timer = new Timer();
//			timer.schedule(new CatchDayOtherHQ(), 500, 5000); // 日K（每隔5秒钟更新一次）
//			timer.schedule(new CatchDayHistoryOtherHQ(), getTime()); // 日K历史
//			timer.schedule(new CatchMinOtherHQ(), 1000, 30000); // 1分钟K线（每隔30秒钟更新一次）
//			// timer.schedule(new CatchDay5OtherHQ(), 1500, 30000); // 5日分时（每隔30秒钟更新一次）
//			timer.schedule(new CatchSnapshotOtherHQ(), 1000, 1000); // 分时
//		}catch (Exception ex){
//			logger.error("伦敦金抓取启动失败:"+ex.getMessage());
//
//		}

	}

	private class CatchDayOtherHQ extends TimerTask {
		@Override
		public void run() {
			logger.info(String.format(" 开始更新其他行情的日K：%s", String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date())));
			try {
				otherHQService.updateDayOtherHQToRedis();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class CatchDayHistoryOtherHQ extends TimerTask {
		@Override
		public void run() {
			logger.info(String.format(" 每天的06:00:00开始更新其他行情的日K历史：%s", String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date())));
			try {
				otherHQService.updateDayHistoryOtherHQToRedis();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class CatchDay5OtherHQ extends TimerTask {
		@Override
		public void run() {
			logger.info(String.format("开始更新其他行情的5日分时：%s", String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date())));
			try {
				otherHQService.updateDay5OtherHQToRedis();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class CatchMinOtherHQ extends TimerTask {
		@Override
		public void run() {
			logger.info(String.format("开始更新其他行情的分时：%s", String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date())));
			try {
				otherHQService.updateMinOtherHQToRedis();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class CatchSnapshotOtherHQ extends TimerTask {
		@Override
		public void run() {
			logger.info(String.format("开始更新其他行情的分时：%s", String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date())));
			try {
				otherHQService.updateSnapshotOtherHQToRedis();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Date getTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 6);
		calendar.set(Calendar.MINUTE, 5);
		calendar.set(Calendar.SECOND, 00);
		return calendar.getTime();
	}

}

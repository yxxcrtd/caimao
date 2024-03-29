package com.caimao.hq.core;

import com.alibaba.fastjson.JSON;


import com.caimao.hq.api.entity.CandleCycle;
import com.caimao.hq.api.entity.Product;
import com.caimao.hq.api.entity.SJSSnapshot;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.service.IGjsErrorService;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.api.service.ISJSCandleService;
import com.caimao.hq.api.service.ISJSSnapshotService;
import com.caimao.hq.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service("sjsDataHandleThread")
public class SJSDataHandleThread implements Runnable{



	@Autowired
	private GJSProductUtils gjsProductUtils;
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(NJSDataHandleThread.class);
	public static Map<String,String> mapProduct=new HashMap();
	@Autowired
	private ISJSCandleService sjsCandleService;
	@Autowired
	private ISJSSnapshotService sjsSnapshotService;
	@Autowired
	private JRedisUtil jredisUtil;


	private List<Snapshot> data;



	@Override
	public void run() {

		if(null!=data&&data.size()>0){
			for(Snapshot snapshot:data){
				try{

					execute(snapshot);
				}catch (Exception ex){
					logger.error("上交所数据解析异常:"+ex.getMessage());
				}
			}
		}
	}

	public void setMessage(List<Snapshot> data) {
		this.data=data;
	}

	public  void redisClear(String financeMic){
		if(!StringUtils.isBlank(financeMic)){

			List<Product> productsList= gjsProductUtils.getProductList(financeMic);
			if(null!=productsList){
				List redisKeyList=new ArrayList();
				fillRedisKeyFromProductList(redisKeyList,productsList);
				redisClear(redisKeyList);
			}
		}
	}

	private void  fillRedisKeyFromProductList(List redisKeyList,List<Product> listProduct){

		if(null!=listProduct){

			if(null==redisKeyList){

				redisKeyList=new ArrayList();

			}
			for(Product product:listProduct){

				fillRedisKeyFromProduct(redisKeyList, product);

			}

		}

	}
	private void redisClear(List<String> redisKeyList){

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

	private void fillRedisKeyFromProduct(List redisKeyList,Product product){

		if(null!=product){
			if(redisKeyList==null){
				redisKeyList=new ArrayList();
			}
			Snapshot temp=new Snapshot();
			temp.setExchange(product.getExchange());
			temp.setProdCode(product.getProdCode());
			redisKeyList.add(SJSMinTimeUtil.getRedisKey(CandleCycle.Minute1, temp));
			redisKeyList.add(SJSMinTimeUtil.getRedisKey(CandleCycle.Snap, temp));
			redisKeyList.add(SJSMinTimeUtil.getRedisKey(CandleCycle.DayCandle, temp));
			redisKeyList.add(SJSMinTimeUtil.getRedisKey(CandleCycle.Hour1, temp));
			redisKeyList.add(SJSMinTimeUtil.getRedisKey(CandleCycle.Hour4, temp));
			redisKeyList.add(SJSMinTimeUtil.getRedisKey(CandleCycle.Minute30, temp));
			redisKeyList.add(SJSMinTimeUtil.getRedisKey(CandleCycle.Minute5, temp));
			redisKeyList.add(SJSMinTimeUtil.getRedisKey(CandleCycle.Month, temp));
			redisKeyList.add(SJSMinTimeUtil.getRedisKey(CandleCycle.Week, temp));
			redisKeyList.add(SJSMinTimeUtil.getRedisKey(CandleCycle.Year, temp));
		}
	}


	private void execute(Snapshot snapshot)throws Exception{


		SJSSnapshot temp=(SJSSnapshot)snapshot.clone();
		//修改当前成交量
		SnapshotFormate.formateSnapshotSJS(CandleCycle.Snap, temp);//格式化时间和rediskey
		String jsonStringOlde = jredisUtil.get(temp.getRedisKey());
		SJSSnapshot snapshotOld=JSON.parseObject(jsonStringOlde, SJSSnapshot.class);//用成交量 相减
		if(null!=snapshotOld){

			if(temp.getBusinessAmount()>snapshotOld.getBusinessAmount()){
				temp.setLastAmount(DoubleOperationUtil.sub(temp.getBusinessAmount(), snapshotOld.getBusinessAmount()));
			}
		}
		String jsonString = JSON.toJSONString(temp);
		snapshot.setLastAmount(temp.getLastAmount());
		jredisUtil.setex(temp.getRedisKey(), jsonString, 0);
		sjsSnapshotService.insert(temp);
		if(isTradeTime(snapshot.getMinTime())){
			sjsCandleService.save(snapshot);
		}else{
			logger.error("获取节假日tradeService is false ");
		}


	}
	public static Boolean isTradeTime(String  time){
		Boolean isTradeTime=true;

		long sourceTime=DateUtils.getTickTime(time,"yyyyMMddHHmmss");
		String strEnd2000=time.substring(0,8)+"200000";
		String strBegin1530=time.substring(0,8)+"153000";
		long begin1530=DateUtils.getTickTime(strBegin1530,"yyyyMMddHHmmss");
		long end2000=DateUtils.getTickTime(strEnd2000,"yyyyMMddHHmmss");


		String strEnd0900=time.substring(0,8)+"090000";
		String strBegin0230=time.substring(0,8)+"023000";
		long begin0230=DateUtils.getTickTime(strBegin0230,"yyyyMMddHHmmss");
		long end0900=DateUtils.getTickTime(strEnd0900, "yyyyMMddHHmmss");



		String strEnd1330=time.substring(0,8)+"133000";
		String strBegin1130=time.substring(0,8)+"113000";
		long begin1130=DateUtils.getTickTime(strBegin1130,"yyyyMMddHHmmss");
		long end1330=DateUtils.getTickTime(strEnd1330,"yyyyMMddHHmmss");

		if(sourceTime>=begin1530&&sourceTime<=end2000){
			isTradeTime=false;
		}else if(sourceTime>=begin0230&&sourceTime<=end0900){

			isTradeTime=false;
		}else if(sourceTime>=begin1130&&sourceTime<=end1330){
			isTradeTime=false;

		}else{
			isTradeTime=true;
		}
		return isTradeTime;

	}

	public void importData(List<Snapshot> data){

		for(Snapshot snapshot:data){
			try{
				execute(snapshot);
			}catch (Exception ex){
				logger.error("上交所数据解析异常:" + ex.getMessage());
			}
		}
	}

	private void convertProductToMap(List<Product> productList,String finance_mic){
		SJSDataHandleThread.mapProduct.clear();
		if(null!=productList){
			for(Product product:productList){
				SJSDataHandleThread.mapProduct.put(finance_mic+product.getProdCode(),finance_mic+product.getProdCode());
			}
		}
	}
}

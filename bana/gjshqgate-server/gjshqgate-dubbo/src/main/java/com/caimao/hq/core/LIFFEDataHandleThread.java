package com.caimao.hq.core;

import com.caimao.hq.api.entity.CandleCycle;
import com.caimao.hq.api.entity.NJSSnapshot;
import com.caimao.hq.api.entity.Product;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.service.IGjsErrorService;
import com.caimao.hq.api.service.INJSCandleService;
import com.caimao.hq.api.service.INJSSnapshotService;
import com.caimao.hq.utils.GJSProductUtils;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.MinTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("liffeDataHandleThread")
public class LIFFEDataHandleThread implements Runnable{
	@Autowired
	private GJSProductUtils gjsProductUtils;
	private Logger logger = Logger.getLogger(LIFFEDataHandleThread.class);
	@Autowired
	private INJSCandleService njsCandleService;
	@Autowired
	private INJSSnapshotService njsSnapshotService;
	@Autowired
	private JRedisUtil jredisUtil;

	private   volatile List<Snapshot> data=null;

	@Override
	public void run() {
		if(null!=data&&data.size()>0){

				for(Snapshot snapshot:data){
					try{
						execute(snapshot);
					}catch (Exception ex){
						logger.error("南交所数据解析异常:"+ex.getMessage());
						//gjsErrorService.insert("NJS",ex.getMessage(),snapshot.toString(),"0");
					}

				}
			}

	}




	public void setMessage(List data) {
		this.data=data;
	}


	public void clear(String importField){

		redisClear("NJS",importField);
		redisClear("SJS",importField);
	}
	public  void redisClear(String finance_mic,String importField){
		if(!StringUtils.isBlank(finance_mic)){

			List<Product> productsList= gjsProductUtils.getProductList(finance_mic);
			if(null!=productsList){
				List redisKeyList=new ArrayList();
				fillRedisKeyFromProductList(redisKeyList,productsList,importField);
				redisClear(redisKeyList);
			}
		}
	}

	private void  fillRedisKeyFromProductList(List redisKeyList,List<Product> listProduct,String importField){

		if(null!=listProduct){

			if(null==redisKeyList){

				redisKeyList=new ArrayList();

			}
			for(Product product:listProduct){

				fillRedisKeyFromProduct(redisKeyList, product,importField);

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

	private void fillRedisKeyFromProduct(List redisKeyList,Product product,String  importField){

		if(null!=product){
			if(redisKeyList==null){
				redisKeyList=new ArrayList();
			}


			Snapshot temp=new Snapshot();
			temp.setExchange(product.getExchange());
			temp.setProdCode(product.getProdCode());
			if(!StringUtils.isBlank(importField)){
				redisKeyList.add(MinTimeUtil.getRedisKeyImport(CandleCycle.Minute1, temp, importField));
				redisKeyList.add(MinTimeUtil.getRedisKeyImport(CandleCycle.Snap, temp, importField));
				redisKeyList.add(MinTimeUtil.getRedisKeyImport(CandleCycle.DayCandle, temp, importField));
				redisKeyList.add(MinTimeUtil.getRedisKeyImport(CandleCycle.Hour1, temp, importField));
				redisKeyList.add(MinTimeUtil.getRedisKeyImport(CandleCycle.Hour4, temp, importField));
				redisKeyList.add(MinTimeUtil.getRedisKeyImport(CandleCycle.Minute30, temp, importField));
				redisKeyList.add(MinTimeUtil.getRedisKeyImport(CandleCycle.Minute5, temp, importField));
				redisKeyList.add(MinTimeUtil.getRedisKeyImport(CandleCycle.Month, temp, importField));
				redisKeyList.add(MinTimeUtil.getRedisKeyImport(CandleCycle.Week, temp, importField));

				redisKeyList.add(MinTimeUtil.getRedisKeyImport(CandleCycle.Month, temp, importField));

				redisKeyList.add(MinTimeUtil.getRedisKeyImport(CandleCycle.Year, temp, importField));
			}else{
				redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Minute1, temp));
				redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Snap, temp));
				redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.DayCandle, temp));
				redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Hour1, temp));
				redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Hour4, temp));
				redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Minute30, temp));
				redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Minute5, temp));
				redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Month, temp));
				redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Week, temp));

				redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Month, temp));

				redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Year, temp));
			}

		}
	}

	@Transactional
	private void execute(Snapshot snapshot) throws Exception {

		NJSSnapshot snap = (NJSSnapshot) snapshot.clone();
		njsCandleService.save(snapshot);
		njsSnapshotService.insert(snap);

	}


}

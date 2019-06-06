package com.caimao.hq.core;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.utils.ConfigUtils;
import com.caimao.hq.utils.DoubleOperationUtil;
import com.caimao.hq.utils.HttpUtils;
import com.caimao.hq.utils.MinTimeUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 伦敦交易所行情 hrrp 返回的行情string
 * 
 * @author Administrator
 * 
 */
public class LIFFEQuoteParseUtils {

	private static Logger logger = Logger.getLogger(LIFFEQuoteParseUtils.class);


	public LIFFECandleFrom163Res liffeCandleDayCandleRequestTo163(String prodCode){
		String temp= ConfigUtils.getApplicationProperties().getProperty("163.candle.DayCandle.url");
		if(StringUtils.isBlank(temp)){
			logger.error("伦敦金抓取数据异常：/META-INF/conf/application.properties未配置163.candle.DayCandle.url 参数");
			throw new RuntimeException("伦敦金抓取数据异常：/META-INF/conf/application.properties未配置163.candle.DayCandle.url 参数");
		}
		String response=HttpUtils.getString(temp);
		LIFFECandleFrom163Res  res=JSON.parseObject(response, LIFFECandleFrom163Res.class);
        if(null!=res){
			res.setProdCode(prodCode);
		}
		return JSON.parseObject(response, LIFFECandleFrom163Res.class);

	}


	private  List<OtherCandle> convertLIFFECandleDay(LIFFECandleFrom163Res res){
		List<OtherCandle>  liffeCandleList=new ArrayList();
		if(null!=res){
			List<String>  messageList=res.getRet();
			if(null!=messageList&&messageList.size()>0){

				for(String str:messageList){
					if(!StringUtils.isBlank(str)){
						OtherCandle candle=new OtherCandle();
						candle.setCycle(CandleCycle.DayCandle);
						candle.setProdCode(res.getProdCode());
						candle.setRedisKey(MinTimeUtil.getRedisKey(CandleCycle.DayCandle, candle));
						convertLIFFECandle(str, candle);
						liffeCandleList.add(candle);
					}
				}
			}
		}
		return liffeCandleList;
	}

	private void  convertLIFFECandle(String message,OtherCandle candle){

		if(!StringUtils.isBlank(message)&&null!=candle){
			candle=new OtherCandle();
			String[] str=message.split(",");
			candle.setMinTime(str[0] + "000000");//交易时间
			candle.setOpenPx(DoubleOperationUtil.parseDouble(str[1]));
			candle.setLastPx(DoubleOperationUtil.parseDouble(str[2]));
			candle.setHighPx(DoubleOperationUtil.parseDouble(str[3]));
			candle.setLowPx(DoubleOperationUtil.parseDouble(str[4]));
			candle.setPxChange(DoubleOperationUtil.parseDouble(str[5]));
			candle.setPxChangeRate(DoubleOperationUtil.parseDouble(str[6]));
		}
	}

}

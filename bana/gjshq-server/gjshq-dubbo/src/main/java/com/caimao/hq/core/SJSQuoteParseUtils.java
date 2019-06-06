package com.caimao.hq.core;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.hq.api.entity.NJSSnapshot;
import com.caimao.hq.api.entity.SJSSnapshot;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.utils.DoubleOperationUtil;
import com.caimao.hq.utils.GJSProductUtils;
import com.caimao.hq.utils.DateUtils;
import org.apache.log4j.Logger;

import java.util.*;


/**
 * 
 * 解析南交所socket 返回的行情string
 * 
 * @author Administrator
 * 
 */
public class SJSQuoteParseUtils {

	private static Logger logger = Logger.getLogger(SJSQuoteParseUtils.class);
	private  static GJSProductUtils gjsProductUtils=new GJSProductUtils();

	private  static Boolean messageValid(String message){
		Boolean isRight=false;
		if(StringUtils.isBlank(message)){
			isRight=false;
		}else{
			if(message.contains("##ApiName")){
				isRight=true;
			}
		}
		return isRight;
	}

	private static void main(String args[]){


	}
	private static String[] messageSplite(String message){
               String [] product=null;
               if(!StringUtils.isBlank(message)){
				   if(message.contains("##")){
					   product= message.split("##");
				   }else{
					   product= new String[]{message};
				   }
			   }
		return product;
	}
	public static  List<Snapshot>  convert(String message){

		List<Snapshot> list=null;
		String[] messageList=null;
		if(messageValid(message)){
			list=new ArrayList<>();
			messageList=messageSplite(message);
			for(int i=1;i<messageList.length;i++){
				try{
					SJSSnapshot temp = new SJSSnapshot();
					convertProduct(messageList[i], temp);
					if(null!=temp){
						if(temp.getOpenPx()==0||temp.getBusinessAmount()==0){
							break;
						}
						list.add(temp);
					}

				}catch (Exception ex){
					logger.error("SJS数据转换异常:"+ex.getMessage());
				}

			}
		}else{
			logger.error("接收消息格式转换失败：" + message);
		}
		return  list;
	}


	public static  List<Snapshot>  convertImport(String message){

		List<Snapshot> list=null;
		String[] messageList=null;
		if(messageValid(message)){

			message=message.substring(message.indexOf("##"),message.length());
			list=convert(message);
		}else{
			logger.error("接收消息格式转换失败：" + message);
		}
		return  list;
	}

	private static Snapshot convertIndex(String strSplite,Snapshot baseSnapshot){
		//strSplite=strSplite.replaceAll(" ", strSplite);
		//System.out.println("指数strSplite array :"+strSplite+":strSplite size="+strSplite.split("\\|").length);
		return null;
	}

	private static void convertStrToMap(String strSplite,Map<String,String> mapPara){

		if(null==mapPara){
			mapPara=new HashMap<>();
		}
		if(!StringUtils.isBlank(strSplite)){
			String[] keyValue=strSplite.split("#");
			String[] temp=null;
			for(String str:keyValue){
				if(!StringUtils.isBlank(str)&&str.contains("=")){
					temp=str.split("=");
					if(null!=temp&&temp.length==2){
						mapPara.put(temp[0],temp[1]);
					}
				}
			}
		}
	}
	private static void fillSnapshotFromMap(SJSSnapshot baseSnapshot,Map<String,String> mapPara){

		baseSnapshot.setExchange("SJS");
		baseSnapshot.setHighPx(DoubleOperationUtil.parseDouble(mapPara.get("high")));//设置最高价
		baseSnapshot.setProdCode(mapPara.get("instID"));//设置合约代码
		baseSnapshot.setProdName(mapPara.get("name"));//设置合约名称
		baseSnapshot.setPreclosePx(DoubleOperationUtil.parseDouble(mapPara.get("lastClose")));//设置昨收盘
		baseSnapshot.setOpenPx(DoubleOperationUtil.parseDouble(mapPara.get("open")));//开盘价
		baseSnapshot.setHighPx(DoubleOperationUtil.parseDouble(mapPara.get("high")));//最高价
		baseSnapshot.setLowPx(DoubleOperationUtil.parseDouble(mapPara.get("low")));//最低价
		baseSnapshot.setLastPx(DoubleOperationUtil.parseDouble(mapPara.get("last")));//最新价
		baseSnapshot.setClosePx(DoubleOperationUtil.parseDouble(mapPara.get("close")));//收盘价
		baseSnapshot.setBusinessAmount(DoubleOperationUtil.parseDouble(mapPara.get("volume")));;//成交量（双边）
		//baseSnapshot.setLastAmount(DoubleOperationUtil.parseDouble(mapPara.get("volume")));//成交量（双边）
		baseSnapshot.setLastWeight(DoubleOperationUtil.parseDouble(mapPara.get("weight")));//成交（双边）重量
		baseSnapshot.setHighLimit(DoubleOperationUtil.parseDouble(mapPara.get("highLimit")));//涨停板
		baseSnapshot.setLowLimit(DoubleOperationUtil.parseDouble(mapPara.get("lowLimit")));//跌停板
		baseSnapshot.setPxChange(DoubleOperationUtil.parseDouble(mapPara.get("upDown")));//涨跌
		if(DoubleOperationUtil.parseDouble(mapPara.get("upDown"))<0){
			baseSnapshot.setPxChangeRate(-DoubleOperationUtil.parseDouble(mapPara.get("upDownRate"))*100);//涨跌幅度
		}else{
			baseSnapshot.setPxChangeRate(DoubleOperationUtil.parseDouble(mapPara.get("upDownRate"))*100);//涨跌幅度
		}

		baseSnapshot.setBusinessBalance(DoubleOperationUtil.parseDouble(mapPara.get("turnOver")));//成交额
		baseSnapshot.setAveragePx(DoubleOperationUtil.parseDouble(mapPara.get("average")));//均价

		baseSnapshot.setTradeTime(convertTradeTime(mapPara.get("quoteTime")));//行情时间
		baseSnapshot.setSequenceNo(mapPara.get("sequenceNo"));
		baseSnapshot.setBidGrp(getbidGrp(mapPara));//设置买盘
		baseSnapshot.setOfferGrp(getOfferGrp(mapPara));//设置卖盘
		baseSnapshot.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
		baseSnapshot.setStatus("0");
		if(mapPara.get("ApiName").equalsIgnoreCase("onRecvSpotQuotation")){//如果是现货
			baseSnapshot.setIsGoods(1);
		}else{
			baseSnapshot.setIsGoods(2);
		}
		baseSnapshot.setTradeDate(convertTradeTime(mapPara.get("quoteDate")));//行情日期
//		try {
//
//			Map productMap=GJSProductUtils.sjsProductTreeMap.get(baseSnapshot.getProdCode());
//			if(null!=productMap){
//				String tradeType=String.valueOf(productMap.get("tradeType"));
//				if(!StringUtils.isBlank(tradeType)){
//					baseSnapshot.setIsGoods(Integer.parseInt(tradeType));
//				}
//			}
//		} catch (Exception e) {
//			logger.error("数据异常：找不到对应的商品"+e.getMessage());
//		}
		baseSnapshot.setApdRecvTime(DateUtils.convert(Long.parseLong(mapPara.get("ApdRecvTime")),"yyyyMMddHHmmss").substring(0,8));
		//如果交易日是周1，需要单独处理周五的自然日
		if(DateUtils.getWeekDay(baseSnapshot.getTradeDate(),"yyyyMMdd").equalsIgnoreCase("2")){
			if(isSubDate(baseSnapshot)){
				baseSnapshot.setApdRecvTime(DateUtils.addDay(baseSnapshot.getTradeDate(), -3, "yyyyMMdd"));//自然日
			}else{

				int iNowTime=Integer.parseInt(baseSnapshot.getTradeTime());
				if(iNowTime>=0&&iNowTime<=23000){
					baseSnapshot.setApdRecvTime(DateUtils.addDay(baseSnapshot.getTradeDate(), -2, "yyyyMMdd"));//自然日
				}else{
					baseSnapshot.setApdRecvTime(baseSnapshot.getTradeDate());
				}
			}

		}else{
			//如果不是周五，并且时间是晚上8-24点，自然日=交易日-1，
			if(isSubDate(baseSnapshot)){
				baseSnapshot.setApdRecvTime(DateUtils.addDay(baseSnapshot.getTradeDate(), -1, "yyyyMMdd"));//自然日
			}else{
				baseSnapshot.setApdRecvTime(baseSnapshot.getTradeDate());
			}

		}

		baseSnapshot.setMinTime(baseSnapshot.getApdRecvTime() + baseSnapshot.getTradeTime());
		baseSnapshot.setLastSettle(DoubleOperationUtil.parseDouble(mapPara.get("lastSettle")));//昨结算价
		baseSnapshot.setSettle(DoubleOperationUtil.parseDouble(mapPara.get("settle")));//结算价
	}
    //交易日 --》自然日转换    晚上8点-24点 分时时间为交易日-1
	private static Boolean isSubDate(SJSSnapshot snapshot){

		Boolean isSubDate=false;
		if(null!=snapshot&&!StringUtils.isBlank(snapshot.getTradeTime())){
			int iNowTime=Integer.parseInt(snapshot.getTradeTime());
			if(iNowTime>=200000&&iNowTime<=240000){
				isSubDate=true;
			}
		}
		return isSubDate;
	}
	//09:43:08 to  094308
	private static String convertTradeTime(String tradeTime){
		String result="";
		if(!StringUtils.isBlank(tradeTime)){
			result=tradeTime.replaceAll(":","");
		}else{
			throw new RuntimeException("南交所数据格式解析错误：quoteTime is null");
		}
		return result;
	}

	private static void convertProduct(String strSplite,SJSSnapshot baseSnapshot){
	    Map mapPara=new HashMap();
		if(null!=baseSnapshot&&!StringUtils.isBlank(strSplite)){
			convertStrToMap(strSplite, mapPara);

			if(mapPara.get("sequenceNo").equals("0")||mapPara.get("volume").equals("0")){
				baseSnapshot=null;

			}else{
				fillSnapshotFromMap(baseSnapshot, mapPara);
			}


		}
	}
    //获取委托买盘
	private static String getbidGrp(Map<String,String> mapPara){

        StringBuffer bigGrp=new StringBuffer();
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("bid1")));//买一价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("bidLot1")));//买一量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("bid2")));//买2价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("bidLot2")));//买2量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("bid3")));//买3价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("bidLot3")));//买3量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("bid4")));//买4价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("bidLot4")));//买4量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("bid5")));//买5价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("bidLot5")));//买5量
		return bigGrp.toString();
	}


	//获取委托卖盘
	private static String getOfferGrp(Map<String,String> mapPara){
		StringBuffer bigGrp=new StringBuffer();
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("ask1")));//卖一价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("askLot1")));//卖一量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("ask2")));//卖2价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("askLot2")));//卖2量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("ask3")));//卖3价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("askLot3")));//卖3量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("ask4")));//卖4价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("askLot4")));//卖4量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("ask5")));//卖5价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(mapPara.get("askLot5")));//卖5量
		return bigGrp.toString();
	}

	private static Boolean isIndex(String messageSingle){
		Boolean isIndex=false;
		if(!StringUtils.isBlank(messageSingle)){
			if(messageSingle.contains("|90|ZS|")){
				isIndex=true;
			}
		}
		return isIndex;
	}

	
}

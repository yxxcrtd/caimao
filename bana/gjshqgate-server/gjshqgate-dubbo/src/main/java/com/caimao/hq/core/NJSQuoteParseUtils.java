package com.caimao.hq.core;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.hq.api.entity.NJSSnapshot;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.utils.DateUtils;
import com.caimao.hq.utils.DoubleOperationUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 解析南交所socket 返回的行情string
 * 
 * @author Administrator
 * 
 */
public class NJSQuoteParseUtils {

	private static Logger logger = Logger.getLogger(NJSQuoteParseUtils.class);
	private static List<Snapshot> getSnapshot(String message){
		return null;
	}

	private  static Boolean messageValid(String message){
		Boolean isRight=false;
		if(StringUtils.isBlank(message)){
			isRight=false;
		}else{
			if(message.contains("|90|90|")||message.contains("|90|ZS|")){
				isRight=true;
			}
		}
		return isRight;
	}

	private static String[] messageSplite(String message){
               String [] product=null;
               if(!StringUtils.isBlank(message)){
				   if(message.contains("\u0006")){
					   product= message.split("\u0006");
				   }else{
					   product= new String[]{message};
				   }
			   }
//		       if(product!=null){
//					for(String str:product){
//					   if(!StringUtils.isBlank(str)){
//						   //如果含有产品和指数，需要分拆
//						   if(str.contains("\\|90\\|ZS")){
//							   index=str.split("\\|90\\|ZS");
//							   for(String strIndex:index){
//								   list.add(strIndex);
//							   }
//						   }else{
//							   list.add(str);
//						   }
//					   }
//				   }
//
//			   }
		return product;
	}
	public static  List<Snapshot>  convert(String message){

		List<Snapshot> list=null;
		String[] messageList=null;
		if(messageValid(message)){
			list=new ArrayList<>();
			messageList=messageSplite( message);
			for(String messageSingle:messageList) {
				NJSSnapshot temp = new NJSSnapshot();
				convertProductNJS(messageSingle, temp, list);
			}
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

	private static void convertProductNJS(String strSplite,NJSSnapshot baseSnapshot,List list){
		if(null!=baseSnapshot){
			baseSnapshot.setExchange("NJS");
			String[] strSplit=strSplite.split("\\|");
			if(null!=baseSnapshot&&strSplit!=null&&strSplit.length== 43) {
				baseSnapshot.setBoardCode(strSplit[3]);
				baseSnapshot.setProdCode(strSplit[4]);//商品代码
				baseSnapshot.setProdName(strSplit[5]);//商品名称
				baseSnapshot.setOpenPx(DoubleOperationUtil.parseDouble(strSplit[6]));//开盘价
				baseSnapshot.setLastPx(DoubleOperationUtil.parseDouble(strSplit[7]));//最新价
				baseSnapshot.setPxChange(DoubleOperationUtil.parseDouble(strSplit[8]));//价格涨跌
				baseSnapshot.setPxChangeRate(DoubleOperationUtil.parseDouble(strSplit[9]));//涨跌幅
				baseSnapshot.setHighPx(DoubleOperationUtil.parseDouble(strSplit[10]));////最高价
				baseSnapshot.setLowPx(DoubleOperationUtil.parseDouble(strSplit[11]));//最低价
				baseSnapshot.setAveragePx(DoubleOperationUtil.parseDouble(strSplit[12]));//平均价
				baseSnapshot.setBusinessAmount(DoubleOperationUtil.parseDouble(strSplit[13]));//成交数量
				baseSnapshot.setOrderAmount(DoubleOperationUtil.parseDouble(strSplit[14]));//订货数量
				baseSnapshot.setPreclosePx(DoubleOperationUtil.parseDouble(strSplit[15]));//昨收盘
				baseSnapshot.setBidGrp(getbidGrp(strSplit));//买盘
				baseSnapshot.setOfferGrp(getOfferGrp(strSplit));//卖盘
				baseSnapshot.setBusinessBalance(DoubleOperationUtil.parseDouble(strSplit[36]));//成交金额，按手计算
				baseSnapshot.setLastAmount(DoubleOperationUtil.parseDouble(strSplit[37]));//现量
				baseSnapshot.setTradeDate(strSplit[38]);
				baseSnapshot.setTradeTime(strSplit[39]);
				baseSnapshot.setBusinessBalanceHand(DoubleOperationUtil.parseDouble(strSplit[40]));
				//baseSnapshot.setMinTime(baseSnapshot.getTradeDate() + baseSnapshot.getTradeTime());
				baseSnapshot.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
				baseSnapshot.setStatus("0");
				baseSnapshot.setIsGoods(1);//1现货，2期货
				if(baseSnapshot.getOpenPx()==0||baseSnapshot.getBusinessAmount()==0){
					return;
				}
				if(isAddDate(baseSnapshot)){
					baseSnapshot.setApdRecvTime(DateUtils.addDay(baseSnapshot.getTradeDate(), 1, "yyyyMMdd"));//自然日
				}else{
					baseSnapshot.setApdRecvTime(baseSnapshot.getTradeDate());
				}
				baseSnapshot.setMinTime(baseSnapshot.getApdRecvTime() + baseSnapshot.getTradeTime());
				list.add(baseSnapshot);
			}
		}
	}

	private static Boolean isAddDate(NJSSnapshot snapshot){

			Boolean isAddDate=false;
			if(null!=snapshot&&!StringUtils.isBlank(snapshot.getTradeTime())){
				int iNowTime=Integer.parseInt(snapshot.getTradeTime());
				if(iNowTime>0&&iNowTime<=60000){

					isAddDate=true;
				}
			}
			return isAddDate;
	}
    //获取委托买盘
	private static String getbidGrp(String[] strSplit){
        StringBuffer bigGrp=new StringBuffer();
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[16]));//买一价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[21]));//买一量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[17]));//买2价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[22]));//买2量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[18]));//买3价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[23]));//买3量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[19]));//买4价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[24]));//买4量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[20]));//买5价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[25]));//买5量
		return bigGrp.toString();
	}


	//获取委托卖盘
	private static String getOfferGrp(String strSplit[]){
		StringBuffer bigGrp=new StringBuffer();
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[26]));//卖一价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[31]));//卖一量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[27]));//卖2价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[32]));//卖2量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[28]));//卖3价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[33]));//卖3量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[29]));//卖4价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[34]));//卖4量
		bigGrp.append("|");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[30]));//卖5价
		bigGrp.append(",");
		bigGrp.append(DoubleOperationUtil.parseDouble(strSplit[35]));//卖5量
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

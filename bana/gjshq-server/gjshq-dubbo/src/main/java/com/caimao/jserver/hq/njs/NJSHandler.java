package com.caimao.jserver.hq.njs;

import com.caimao.hq.api.entity.NJSSnapshot;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.core.NJSDataHandleThread;
import com.caimao.hq.core.NJSQuoteParseUtils;
import com.caimao.hq.core.ProcessorManager;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.SpringUtil;
import com.caimao.jserver.server.core.HQServer;
import com.caimao.jserver.socket.core.IoFuture;
import com.caimao.jserver.socket.core.IoHandlerAdapter;
import com.caimao.jserver.socket.core.IoSession;
import com.caimao.jserver.socket.core.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NJSHandler extends IoHandlerAdapter {

	private JRedisUtil jredisUtil=null;
	private Logger logger = Logger.getLogger(NJSHandler.class);
	private static int CONSTANTS_HEADLEN = 32;// 报头长度
	private static int CONSTANTS_LOGINLEN = 20;// 登录包长度
	// 登陆包
	private static byte[] byteLogin = new byte[CONSTANTS_HEADLEN + CONSTANTS_LOGINLEN];
	// 心跳包
	private static byte[] byteHeart = new byte[CONSTANTS_HEADLEN];

	/**
	 * 登陆行情服务器的用户名
	 */
	public static String MARKET_LOGIN_USER = "NFXG";
	/**
	 * 登陆行情服务器的密码
	 */
	public static String MARKET_LOGIN_PWD = "nfxg";

	public NJSHandler(String user,String pwd) {

		jredisUtil =(JRedisUtil) SpringUtil.getBean("jredisUtil");
		MARKET_LOGIN_USER=user;
		MARKET_LOGIN_PWD=pwd;
		initLoginPack();
		initHeartPack();

	}
	/**
	 * 初始化登陆包的验证
	 *
	 * @return
	 */
	private static void initLoginPack() {
		byteLogin[0] = (new Integer(0)).byteValue();// 登录包的长度
		byteLogin[1] = (new Integer(CONSTANTS_LOGINLEN)).byteValue();// 登录包的长度
		byteLogin[2] = (new Integer(96)).byteValue();// 登录主协议号
		byteLogin[3] = (new Integer(1)).byteValue();// 登录子协议号
		byteLogin[4] = '0';// 状态
		byteLogin[5] = '0';// 状态
		byteLogin[6] = '0';// 状态
		byteLogin[7] = '0';// 状态
		byteLogin[8] = '0';// 状态
		byteLogin[9] = 'N'; // 是否有下一包

		byteLogin[10] = ' ';
		byteLogin[11] = '0';// 是否压缩

		byteLogin[12] = ' ';// 保留位
		byteLogin[13] = ' ';// 保留位
		byteLogin[14] = ' ';// 保留位
		byteLogin[15] = ' ';// 保留位

		byteLogin[16] = ' ';// 交易账号
		byteLogin[17] = ' ';// 交易账号
		byteLogin[18] = ' ';// 交易账号
		byteLogin[19] = ' ';// 交易账号
		byteLogin[20] = ' ';// 交易账号
		byteLogin[21] = ' ';// 交易账号
		byteLogin[22] = ' ';// 交易账号
		byteLogin[23] = ' ';// 交易账号

		byteLogin[24] = ' ';// 交易账号
		byteLogin[25] = ' ';// 交易账号
		byteLogin[26] = ' ';// 交易账号
		byteLogin[27] = ' ';// 交易账号
		byteLogin[28] = ' ';// 交易账号
		byteLogin[29] = ' ';// 交易账号
		byteLogin[30] = ' ';// 交易账号
		byteLogin[31] = ' ';// 交易账号

		byteLogin[32] = ' ';// IP地址
		byteLogin[33] = ' ';// IP地址
		byteLogin[34] = ' ';// IP地址
		byteLogin[35] = ' ';// IP地址
		/** ***********设置用户************* */
		// TODO 获取行情用户名和密码的长度
		int nLen = MARKET_LOGIN_USER.length();
		char ch = ' ';
		for (int i = 0; i < 8; i++) {
			if (i < nLen) {
				ch = MARKET_LOGIN_USER.charAt(i);
				byteLogin[36 + i] = (byte) ch;
			} else
				byteLogin[36 + i] = ' ';
		}

		/** **********设置密码************ */
		nLen = MARKET_LOGIN_PWD.length();
		for (int i = 0; i < 8; i++) {
			if (i < nLen) {
				ch = MARKET_LOGIN_PWD.charAt(i);
				byteLogin[44 + i] = (byte) ch;
			} else
				byteLogin[44 + i] = ' ';
		}
	}

	private static void initHeartPack() {
		for (int i = 0, n = byteHeart.length; i < n; i++) {
			if (i == 9) {
				byteHeart[i] = 'N';
			} else {
				byteHeart[i] = (new Integer(0)).byteValue();
			}
		}
	}

	/**
	 * 开始登陆认证
	 *
	 */
	public void loginToServer(IoSession session) {
		session.write(new Request(byteLogin));
		logger.debug("开始认证登录...");
	}

	/**
	 * 开始登陆认证
	 *
	 */
	public void sendHeartPart(IoSession session) {
		session.write(new Request(byteHeart));
	}
	@Override
	public void messageReceived(IoSession session, Message msg) {

		Response data=(Response)msg;
		int type=data.getMainType();
		int subType=data.getSubType();
		logger.debug("协议:" + type + ":" + subType + data.getBody());
		if(type==0x60){
			//System.out.println("登录成功");
		}else if(type==0x68){
			if(subType==0x00){
				//System.out.println("静态行情");
			}else if(subType==0x01){
				List<Snapshot> list= NJSQuoteParseUtils.convert(data.getBody());
				List<Snapshot> removeRepeatList=removeRepeat(list);
				if(null!=removeRepeatList&&removeRepeatList.size()>0){

					NJSDataHandleThread njsDataHandleThread=(NJSDataHandleThread) SpringUtil.getBean("njsDataHandleThread");
					njsDataHandleThread.setMessage(null);
					njsDataHandleThread.setMessage(removeRepeatList);
					ProcessorManager.push(njsDataHandleThread);

				}

			}else if(subType==0x04){
				//System.out.println("商品信息");
			}else{

			}
		}
	}
	private synchronized List<Snapshot> removeRepeat(List<Snapshot> list){
		List<Snapshot> listResult= Collections.synchronizedList(new ArrayList());
		if(null!=list){

          for(Snapshot snapshot:list){
			  if(snapshot.getBusinessAmount()==0||snapshot.getOpenPx()==0||snapshot.getHighPx()==0||snapshot.getLowPx()==0){
				  continue;
			  }
			 if(!isRepeat(snapshot)){

				 jredisUtil.setex(getIsRepeatKey(snapshot), "true", 36000000);//如果不重复，就添加
				 listResult.add(snapshot);
			 }
		  }

		}
		return listResult;
	}
	private DecimalFormat decimalFormat = new DecimalFormat("###0.00");//格式化设置

	private String getIsRepeatKey(Snapshot snapshot){
		StringBuffer  sb=new StringBuffer();
		if(null!=snapshot) {
			NJSSnapshot temp = (NJSSnapshot) snapshot;
			if (null != snapshot) {
				if(StringUtils.isBlank(temp.getApdRecvTime())){
					sb.append(temp.getTradeDate());
				}else{
					sb.append(temp.getApdRecvTime());
				}
				sb.append(temp.getExchange());
				sb.append(temp.getProdCode());
				sb.append(decimalFormat.format(temp.getBusinessAmount()));
			}

		}
		return sb.toString();
	}


	public Boolean  isRepeat(Snapshot snapshot){

		Boolean isRepeat=false;
		String redisIsRepeatKey=getIsRepeatKey(snapshot);
		if(!StringUtils.isBlank(redisIsRepeatKey)) {

			if (jredisUtil.exists(redisIsRepeatKey)){
				isRepeat=true;
			}else{
				isRepeat=false;
			}
		}else{
			isRepeat=true;
		}
		return isRepeat;
	}
	@Override
	public void sessionRegistered(IoSession session) {
		loginToServer(session);
		logger.debug("连接成功");
	}

	@Override
	public void sessionClosed(IoFuture future) {

		while(null==future||future.getSession().isClose()){

			try {
				HQServer.start("NJS");
				Thread.sleep(200000);
			} catch (Exception ex) {

				System.out.println("sessionClosed:"+ex.getMessage());

			}
		}

	}
	@Override
	public void messageReceived(IoSession session, byte[] msgdata) {

	}


}

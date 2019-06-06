package com.caimao.jserver.hq.sjs;

import com.caimao.hq.api.entity.SJSSnapshot;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.core.ProcessorManager;
import com.caimao.hq.core.SJSDataHandleThread;
import com.caimao.hq.core.SJSQuoteParseUtils;
import com.caimao.hq.utils.DateUtils;
import com.caimao.hq.utils.FileUtils;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.SpringUtil;
import com.caimao.jserver.server.base.Sequence;
import com.caimao.jserver.server.core.HQServer;
import com.caimao.jserver.socket.core.IoFuture;
import com.caimao.jserver.socket.core.IoHandlerAdapter;
import com.caimao.jserver.socket.core.IoSession;
import com.caimao.jserver.socket.core.Message;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SJSHandler extends IoHandlerAdapter{
	private JRedisUtil jredisUtil=null;
	/**
	 * 登陆行情服务器的用户名
	 */
	public static String MARKET_LOGIN_USER;
	/**
	 * 登陆行情服务器的密码
	 */
	public static String MARKET_LOGIN_PWD;

	// 登陆包
	private static byte[] byteLogin =null;
	// 心跳包
	private static byte[] byteHeart = null;

	private static String loginStr="00000107term_type=05#user_key=97641239412631#user_type=3#user_id=1080012271#branch_id=B0077001#lan_ip=10.14.14.133#";
	private static String heartStr="00000011ConnectTest";
	private static Sequence sequence=new Sequence(10);

	public SJSHandler(String user, String pwd) {
		jredisUtil =(JRedisUtil) SpringUtil.getBean("jredisUtil");
		MARKET_LOGIN_USER = user;
		MARKET_LOGIN_PWD = pwd;
		initLoginPack();
		initHeartPack();
	}
	private DecimalFormat decimalFormat = new DecimalFormat("###0.00");//格式化设置


	private String getIsRepeatKey(Snapshot snapshot){
		StringBuffer  sb=new StringBuffer();
		if(null!=snapshot) {
			SJSSnapshot temp = (SJSSnapshot) snapshot;
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
	private Boolean  isRepeat(Snapshot snapshot){

		Boolean isRepeat=false;
		String redisIsRepeatKey=getIsRepeatKey(snapshot);
		if(!StringUtils.isBlank(redisIsRepeatKey)) {

			if (jredisUtil.exists(redisIsRepeatKey)){
				isRepeat=true;
			}
		}
		return isRepeat;
	}
	/**
	 * 初始化登陆包的验证
	 * 
	 * @return
	 */
	private static void initLoginPack() {

		byteLogin=loginStr.getBytes();
	}

	private static void initHeartPack() {

		byteHeart=heartStr.getBytes();
	}

	/**
	 * 开始登陆认证
	 * 
	 */
	public void loginToServer(IoSession session) {
			session.write(new Request(byteLogin));
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

		try{
    		Response data = (Response) msg;
			List<Snapshot> list= SJSQuoteParseUtils.convert(data.getBody());
			List<Snapshot> removeRepeatList=removeRepeat(list);
//			for(Snapshot snapshot:removeRepeatList){
//				System.out.println(snapshot.toString());
//				FileUtils.appendWrite("C://sjssnapshot.txt", DateUtils.getNoTime("yyyyMMddHHmmss")+"###"+snapshot.toString());
//			}

			SJSDataHandleThread sjsDataHandleThread=(SJSDataHandleThread)SpringUtil.getBean("sjsDataHandleThread");
			sjsDataHandleThread.setMessage(null);
			sjsDataHandleThread.setMessage(removeRepeatList);
			ProcessorManager.push(sjsDataHandleThread);
		}catch (Exception ex){
			System.out.println("SJS解析数据错误:"+ex.getMessage());
		}

	}
	private synchronized List<Snapshot> removeRepeat(List<Snapshot> list){

		List<Snapshot> listResult= Collections.synchronizedList(new ArrayList());
		if(null!=list&&list.size()>0){

			for(Snapshot snapshot:list){

				if(snapshot.getBusinessAmount()==0||snapshot.getOpenPx()==0||snapshot.getHighPx()==0||snapshot.getLowPx()==0){
					continue;
				}
				if(!isRepeat(snapshot)){

					listResult.add(snapshot);
					jredisUtil.setex(getIsRepeatKey(snapshot), "true", 36000000);//如果不重复，就添加
				}
			}

		}
		return listResult;
	}
	@Override
	public void sessionRegistered(IoSession session) {
		loginToServer(session);
	}

	@Override
	public void sessionClosed(IoFuture future) {

		while(null==future||future.getSession().isClose()){

			try {
				HQServer.start("SJS");
				Thread.sleep(20000);
			} catch (Exception ex) {

				System.out.println("sessionClosed:"+ex.getMessage());

			}
		}
	}

	@Override
	public void messageReceived(IoSession session, byte[] msgdata) {

	}

}

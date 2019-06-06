package com.caimao.hq.core;
import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.List;

import com.caimao.hq.api.entity.NJSSnapshot;
import com.caimao.hq.api.entity.Snapshot;
import org.apache.log4j.Logger;




/**
 * Description: 数据接收线程
 */
public class RecThread implements Runnable {
	
	private Logger log = Logger.getLogger(RecThread.class);
	
	public Socket m_recSocket = null;
	private InputStream streamIn = null;
	private OutputStream streamOut = null;
	private static int CONSTANTS_HEADLEN = 32;// 报头长度
	private static int CONSTANTS_LOGINLEN = 20;// 登录包长度
	private boolean bThreadStatus = false;// 线程的运行状态
	private long ts=System.currentTimeMillis();//当前系统时间

	// 登陆包
	private byte[] byteLogin = new byte[CONSTANTS_HEADLEN + CONSTANTS_LOGINLEN];
	// 心跳包
	private byte[] byteHeart = new byte[CONSTANTS_HEADLEN];
	// 开辟100K的缓冲区
	private byte[] recBuffer = new byte[102400];
	
	private String strThreadFlag = "";
	
	/**
	 * 是否发送心跳
	 * 
	 */
	public static boolean HEART_FLAG=true;
	
	/**
	 * 是否已连接行情服务器
	 */
	public static boolean LINK_MARKET_FLAG = false;
	/**
	 * 是否已登录行情服务器
	 */
	public static boolean LOGIN_MARKET_FLAG = false;
	/**
	 * 行情线程是否活跃
	 */
	public static boolean ACTIVITY_MARKET_FALG = false;
	/**
	 * 行情连接地址
	 */
	public static String MARKET_IP =  "183.62.138.189";
	/**
	 * 行情连接端口
	 */
	public static int MARKET_PORT = 13132;
	/**
	 * 登陆行情服务器的用户名
	 */
	public static String MARKET_LOGIN_USER = "HUOBI";
	/**
	 * 登陆行情服务器的密码
	 */
	public static String MARKET_LOGIN_PWD = "huobi";

	/**
	 * 是否终止行情的处理
	 *
	 */ 
	private boolean interruptMarketReceive = false;
	
	private RecThread() {}

	private static RecThread instance = new RecThread();
	public static void main(String[] args) {
		RecThread.getInstance().run(); 
	}
	public static RecThread getInstance() {
		return instance;
	}

	@Override
	public void run() {
//		try {
//			System.out.println("行情接收线程延迟15秒...");
//			// 等待交易状态灯数据调度完成，延迟行情接收
//			Thread.sleep(15 * 1000);
//		}
//		catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}

		// 初始化数据包
		initLoginPack();
		initHeartPack();

		// 做一个死循环,如果没有异常就永远不退出
		while (true) {		
			try {
			
				// 无法连接行情服务器
				if (!LINK_MARKET_FLAG) {
					linkToServer();
				}

				// 行情服务已连接未登陆成功的执行登陆
				if (LINK_MARKET_FLAG && !LOGIN_MARKET_FLAG) {
					loginToServer();
				}
				// 登陆成功执行数据接收
				if (LOGIN_MARKET_FLAG) {
					doToday();
				}else{
					resetSocket();
				}
		
			}
			catch (Exception e) {
				System.out.println("与行情服务器交互出现异常:" + e.getMessage());
			}
			finally {// 程序出现异常，现在开始清理资源
				try {
					streamIn = null;
					streamOut = null;
					if (m_recSocket != null) {
						m_recSocket.close();
						m_recSocket = null;
					}
				}
				catch (IOException e) {
					System.out.println("释放资源出现异常:" + e.getMessage());
				}
				catch (Exception e) {
					System.out.println("释放资源出现异常:" + e.getMessage());
				}finally{
					resetSocket();
				}
			}
		}
	}

	/**
	 * 与行情服务器建立连接
	 * 
	 * @return
	 */
	public boolean linkToServer() {
		try {
			m_recSocket = new Socket(MARKET_IP, MARKET_PORT);// 建立与服务器的连接

			if (m_recSocket != null) {
				streamIn = m_recSocket.getInputStream();
				streamOut = m_recSocket.getOutputStream();

				LINK_MARKET_FLAG = true;
				System.out.println("与行情服务器\"" + MARKET_IP + "(" + MARKET_PORT + ")\"成功建立连接...");

				return true;
			}
		}
		catch (IOException e) {
			System.out.println("与行情服务器建立连接出现IO异常:" + e.getMessage());
		}
		catch (Exception e) {
			System.out.println("与行情服务器建立连接出现异常:" + e.getMessage());
		}
		return false;
	}

	/**
	 * 开始登陆认证
	 * 
	 * @return true:登陆成功
	 */
	public boolean loginToServer() {
		try {
			streamOut.write(byteLogin, 0, byteLogin.length);
			streamOut.flush();

			LOGIN_MARKET_FLAG = checkLogin();
			ACTIVITY_MARKET_FALG = true;
			System.out.println("行情登陆成功...");
			return true;
		}
		catch (Exception e) {
			System.out.println("发送登陆认证出现异常:" + e.getMessage());
		}
		return false;
	}
	
	/**
	 * 取得登录是否成功验证
	 */
	private boolean checkLogin(){
		boolean loginCkeck=true;
		int iLen = 0; 
		int	iType = 0;
		int iState = 0;
		int iTempLen1 = 0;
		int iTempLen2 = 0;
		byte[] headerBuffer = new byte[CONSTANTS_HEADLEN];
		String next = "N";// 是否有下一包
		int iPosStart = 0;
	try{
		    int iHeadRead = streamIn.read(headerBuffer, 0, CONSTANTS_HEADLEN);
		    if (iHeadRead == CONSTANTS_HEADLEN) {
			    next = new String(headerBuffer, 9, 1);
//			    System.out.println("是否有下一包标示：" + next);
			    iTempLen1 = headerBuffer[0] & 0xff;
			    iTempLen2 = headerBuffer[1] & 0xff;
			    iLen = (iTempLen1 << 8) | iTempLen2;
			    iType = headerBuffer[2] & 0xff;
			    iState = headerBuffer[3] & 0xff;

			    int iHasRead = 0;
			      while (iHasRead < iLen) {
				  int iLeftNum = iLen - iHasRead;
				  int iRealRead;
				  iRealRead = streamIn.read(recBuffer, iPosStart + iHasRead, iLeftNum);
				  if (iRealRead > 0) {
					  iHasRead += iRealRead;
				  }
		        }
	         }	
		
	     }catch(Exception e){
		        e.printStackTrace();
		    }
		
		
//			    System.out.println("类型:" + iType);
//			    System.out.println("接收到:" + new String(recBuffer, 0, iPosStart + iLen, Charset.forName("GBK")));
		       if(iType==96&&iState==1){
			       loginCkeck=true;
		         }else{
			       loginCkeck=false;
		        }
		     return loginCkeck;
	}
	
	/**
	 * 初始化登陆包的验证
	 * 
	 * @return
	 */
	private void initLoginPack() {
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
	
	private void initHeartPack() {
		for (int i = 0, n = byteHeart.length; i < n; i++) {
			if (i == 9) {
				byteHeart[i] = 'N';
			} else {
				byteHeart[i] = (new Integer(0)).byteValue();
			}
		}
	}
	
	/**
	 * 处理当天的所有数据接收,应可以随时终止
	 * 
	 * @return
	 */
	public int doToday() {
		int iFlag = 0;

		int iLen = 0, iType = 0, iState = 0;
		int iTempLen1 = 0, iTempLen2 = 0;
		interruptMarketReceive = false;
		while (ACTIVITY_MARKET_FALG && !interruptMarketReceive) {
			try {
				// 非盘中交易直接退出
//				boolean flag = !MemoryMarketUtils.getInstance().getTradeState().isTrading();
//				if (flag) {
//					System.out.println("交易闭市，暂停对行情的处理...");
//					break;
//				}
				
				
				 updateHeartFlag();
	                if(HEART_FLAG){
	        			System.out.println("发送心跳包");
	        			RecThread.getInstance().sendHeartPack();
	        			}
				
				


				if (null == streamIn) {
					break;
				}
				byte[] headerBuffer = new byte[CONSTANTS_HEADLEN];
				String next = "N";// 是否有下一包
				int iPosStart = 0;// 每包数据在缓存中的开始位置
				do {
					// 清空上次的BUFFER空间
					int iHeadRead = streamIn.read(headerBuffer, 0, CONSTANTS_HEADLEN);

					if (iHeadRead == CONSTANTS_HEADLEN) {// 读到了SOCKET缓冲区的数据,此处为阻塞的操作
						next = new String(headerBuffer, 9, 1);
//						System.out.println("是否有下一包标示：" + next);
						iTempLen1 = headerBuffer[0] & 0xff;
						iTempLen2 = headerBuffer[1] & 0xff;

						iLen = (iTempLen1 << 8) | iTempLen2;
						iType = headerBuffer[2] & 0xff;
						iState = headerBuffer[3] & 0xff;

						int iHasRead = 0;

						while (iHasRead < iLen) {
							int iLeftNum = iLen - iHasRead;
							int iRealRead = streamIn.read(recBuffer, iPosStart + iHasRead, iLeftNum);
														
							if (iRealRead > 0) {
								iHasRead += iRealRead;
							}
							// 外部请求了结束线程
							if (!ACTIVITY_MARKET_FALG) {
								break;
							}
						}



						if(iType==104&&(iState==0||iState==1)){
							String str=new String(recBuffer, 0, iPosStart + iLen, Charset.forName("GBK"));
//							List<Snapshot> list= NJSQuoteParseUtils.convert(str);
//							NJSSnapshot temp=null;
//							Double double1;
//							DecimalFormat decimalFormat = new DecimalFormat("###0.00");//格式化设置
//							for(Snapshot snapshot:list){
//								if(null!=snapshot&&snapshot.getProdCode().equalsIgnoreCase("AG")){
//									 double1 =((NJSSnapshot) snapshot).getBusinessAmount();
//									 System.out.println("AG:" + snapshot.getLastAmount() + "|" + decimalFormat.format(double1) + "|" + snapshot.toString());
//								}
//
//							}
//							NJSDataHandleThread njsDataHandleThread=(NJSDataHandleThread) SpringUtil.getBean("njsDataHandleThread");
//							njsDataHandleThread.setMessage(str);
//							ProcessorManager.push(njsDataHandleThread);
							System.out.print(str);
						}
						if (iHasRead == iLen) {// 读取传过来的行情数据
							if ("N".equals(next)) {// 不含下一包
							
								// 即期
								if (iType == 0x64) {
									if (!do0x64(iState, iPosStart + iLen)) {
										break;
									}
								}					
								iPosStart = 0;// 归零
							} else if ("Y".equals(next)) {// 还有下一包
								iPosStart += iLen;// 累加本次包的长度
							}
						}
					}

				} while ("Y".equals(next));// 处理有下一包数据的情况
				Thread.sleep(1);
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("接收行情出现异常:" + e.getMessage());
				interruptMarketReceive = false;
				break;
			}
		}
		return iFlag;
	}

	/**
	 * 判断时间为10
	 */
	public void updateHeartFlag(){
		long tsNow=System.currentTimeMillis();
		if(tsNow-this.ts>=10000){
			this.ts=tsNow;
			HEART_FLAG=true;
		}else{
			HEART_FLAG=false;
		}
	}
	
	/**
	 * 撮合\即期
	 * 
	 * @throws Exception
	 */
	private boolean do0x64(int iState, int iLen) {
		String strRecData = "";
		strRecData = new String(recBuffer, 0, iLen, Charset.forName("GBK"));
		if (iState == 0x00) {
			System.out.println("即期静态行情：" + strRecData);
		} else if (iState == 0x01) {
			System.out.println("即期动态行情：" + strRecData);
		}
		return true;
	}
	
	/**
	 * 发送状态检测包，以保持连接
	 */
	public void sendHeartPack() {
		try {
			if (streamOut != null) {
				streamOut.write(byteHeart, 0, byteHeart.length);
				streamOut.flush();
				// 将行情线程处于活跃状态
				ACTIVITY_MARKET_FALG = true;
				log.debug("向行情服务器发送检测包...");
			} else {
				LINK_MARKET_FLAG = false;
				LOGIN_MARKET_FLAG = false;
			}
		}
		catch (Exception e) {
			LINK_MARKET_FLAG = false;
			LOGIN_MARKET_FLAG = false;
			ACTIVITY_MARKET_FALG = false;
			interruptMarketReceive = true;
			log.error("发送检测包出现异常:" + e.getMessage());
		}
	}

	/**
	 * 断开与行情服务器的连接，以重连
	 */
	public void resetSocket() {
		try {
			/** **********首先终止今天的接收行情循环**************** */
			LOGIN_MARKET_FLAG = false;
			LINK_MARKET_FLAG=false;
			if (streamIn != null)
				streamIn = null;
			if (streamOut != null)
				streamOut = null;
			if (m_recSocket != null) {
				m_recSocket.close();
				m_recSocket = null;
			}
		}
		catch (Exception e) {
			System.out.println("重置与行情服务器的连接出现异常:" + e.getMessage());
		}
	}

	/**
	 * 停止线程
	 */
	public void stopThread() {
		while (bThreadStatus) {
			try {
				Thread.sleep(1000);
				System.out.println("等待接收线程" + strThreadFlag + "的退出...");
			}
			catch (Exception e) {
				System.out.println("线程休眠出现异常:" + e.getMessage());
			}
		}
	}
}

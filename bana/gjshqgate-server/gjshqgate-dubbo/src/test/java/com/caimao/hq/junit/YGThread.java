package com.caimao.hq.junit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;


public class YGThread implements Runnable{
	

	public Socket socket = null;
	private InputStream streamIn = null;
	private OutputStream streamOut = null;
	
	public static String MARKET_IP =  "112.95.144.26";
	public static int MARKET_PORT = 48002;
//	public static String loginStr="00000105term_type=05#" +
//			"user_key=97641239412631#" +
//			"user_type=3#" +
//			"user_id=1020782897#" +
//			"user_pwd=123321#branch_id=B00000#" +
//			"lan_ip=192.168.10.133#";
	
	public static String loginStr="00000107term_type=05#user_key=97641239412631#user_type=3#user_id=1080012271#branch_id=B0077001#lan_ip=10.14.14.133#";
	
	public static String heartStr="00000011ConnectTest";
	
	public static boolean HEART_FLAG=true;
	
	private byte[] recBuffer = new byte[102400];

	@Override
	public void run() {
		linkToServer();
		loginToServer(loginStr);
		doToday();
		
		
	}
	
	
	/*
	 * 连接服务器
	 */
	public boolean linkToServer(){
		try {
			socket = new Socket(MARKET_IP, MARKET_PORT);// 建立与服务器的连接

			if (socket != null) {
				streamIn = socket.getInputStream();
				streamOut = socket.getOutputStream();
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
	
	
	/*
	 * 登录服务器
	 */
	public boolean loginToServer(String loginStr) {
		
		try {
			
			byte buff[]=loginStr.getBytes();
			
			streamOut.write(buff,0,buff.length);
			streamOut.flush();
			System.out.println("行情登陆成功...");
			
			return true;
		}
		catch (Exception e) {
			System.out.println("发送登陆认证出现异常:" + e.getMessage());
		}
		return false;
	}
	
	
	/*
	 * 处理数据
	 */
	public boolean doToday() {
		
		try {
			while(true){
			byte d[]=new byte[8];
			int iPosStart = 0;
				

			streamIn.read(d,0,8);	
//			System.out.println("包头"+new String(d,0,8,Charset.forName("GBK")));
			int len=Integer.parseInt(new String(d,0,8,Charset.forName("GBK")));
//			System.out.println("包长："+len);
			
			
			int iHasRead = 0;
			while (iHasRead < len) {
				int iLeftNum = len - iHasRead;
				int iRealRead = streamIn.read(recBuffer, iPosStart + iHasRead, iLeftNum);
				if (iRealRead > 0) {
					iHasRead += iRealRead;
				}
				
			}
			
			//String data=new String(recBuffer,0,len);
			//BaseService.getYG(data);
			System.out.println(new String(recBuffer,0,len));
			Thread.sleep(100);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	    return true;
	}
	
	
	/*
	 * 发送心跳包
	 */
	public void sendHeartPack() {
		byte heart[]=heartStr.getBytes();
		try {
			if (streamOut != null) {
				System.out.println("心跳检测----------"+heartStr);
				streamOut.write(heart, 0, heart.length);
				streamOut.flush();
				
			} else {
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private YGThread() {}
	
	private static YGThread instance = new YGThread();
	
	public static YGThread getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		YGThread.getInstance().run(); 
	}


  }

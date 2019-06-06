package com.caimao.jserver.server.base;

import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;

import org.w3c.dom.Element;

import com.caimao.jserver.server.core.TradePlugin;
import com.caimao.jserver.socket.core.Configure;
import com.caimao.jserver.socket.core.IoHandler;
import com.caimao.jserver.socket.core.IoProtocol;
import com.caimao.jserver.socket.core.IoSession;
import com.caimao.jserver.socket.core.OverTimeHandler;
import com.caimao.jserver.socket.expand.IoConnector;

public abstract class BaseSocketTradePlugin implements TradePlugin {
	protected IoConnector server;
	private Configure config;
	private LinkedBlockingQueue<IoSession> queue;
	protected abstract OverTimeHandler getOverTimeHandler();
	protected abstract IoProtocol getProtocol();
	protected abstract IoHandler getHandler();
	@Override
	public void init(Element el) {
		config = new Configure();
		config.setAddress(new InetSocketAddress(el.getAttribute("ip"), Integer.parseInt(el.getAttribute("port"))));
		config.setProtocolHandler(getProtocol());
		config.setIoHandler(getHandler());
		config.setOverTimeHandler(getOverTimeHandler());
		config.setBothOverTime(20000);
		try {
			server = new IoConnector(1);
		} catch (Exception e) {

		}
		queue = new LinkedBlockingQueue<>(Integer.parseInt(el.getAttribute("connect")));
	}
	
	
	@Override
	public void start() {
		try {
			config.start(server);
			//启动一个连接池
			int l = queue.remainingCapacity();
			for (int i = 0; i < l; i++) {
				IoSession session = IoConnector.newSession(server);
				
				System.out.println("正在连接："+server.getConfigure().getAddress().toString());
				session.connect();
				System.out.println("连接成功："+server.getConfigure().getAddress().toString());
				
				queue.add(session);
			}

		} catch (Exception e) {

		}

	}

	@Override
	public void stop() {
		server.stop();
	}
	
	/**
	 * 
	 * @param session
	 * @param service
	 * @param request
	 * @return
	 */
	protected abstract Object sendAndRev(IoSession session,String service,Object request);
	
	@Override
	public Object sendAndRev(String service,Object request) {
		IoSession session=queue.poll();
		Object v=sendAndRev(session,service,request);
		queue.add(session);
		return v;
	}
	
	@Override
	public Object sendAndRev(String service) {
		IoSession session=queue.poll();
		Object v=sendAndRev(session,service,null);
		queue.add(session);
		return v;
	}
	
	
	/**
	 * 
	 * @param session
	 * @param service
	 * @param request
	 */
	protected abstract void send(IoSession session,String service,Object request);
	@Override
	public void send(String service, Object request) {
		IoSession session=queue.poll();
		sendAndRev(session,service,request);
		queue.add(session);
	}

}

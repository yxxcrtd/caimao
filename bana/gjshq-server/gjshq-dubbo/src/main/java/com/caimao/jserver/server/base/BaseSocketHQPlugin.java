package com.caimao.jserver.server.base;

import java.net.InetSocketAddress;

import com.caimao.jserver.server.core.HQServer;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.caimao.jserver.server.core.HQPlugin;
import com.caimao.jserver.socket.core.Configure;
import com.caimao.jserver.socket.core.IoHandler;
import com.caimao.jserver.socket.core.IoSession;
import com.caimao.jserver.socket.core.OverTimeHandler;
import com.caimao.jserver.socket.core.IoProtocol;
import com.caimao.jserver.socket.expand.IoConnector;
public abstract class BaseSocketHQPlugin implements HQPlugin {

	private Logger logger = Logger.getLogger(BaseSocketHQPlugin.class);
	protected IoConnector server;
	private Configure config;
	protected abstract IoHandler getHandler();

	protected abstract IoProtocol getProtocol();
	protected abstract OverTimeHandler getOverTimeHandler();
	@Override
	public void init(Element el) {
		config = new Configure();
		//设置连接
		config.setAddress(new InetSocketAddress(el.getAttribute("ip"), Integer.parseInt(el.getAttribute("port"))));
		//设置协议
		config.setProtocolHandler(getProtocol());
		//设置处理器
		config.setIoHandler(getHandler());
		//设置闲置超时
		config.setOverTimeHandler(getOverTimeHandler());
		int maxIdle=Integer.parseInt(el.getAttribute("idle"));
		config.setWriteOverTime(maxIdle);
		config.setBothOverTime(maxIdle);
		config.setReadOverTime(maxIdle);
		try {
			
			server = new IoConnector(Integer.parseInt(el.getAttribute("thread")));
		} catch (Exception e) {
			logger.error("socket 初始化错误："+e.getMessage());
		}
	}

	@Override
	public void start() {

		try {

			config.start(server);
			IoSession session = IoConnector.newSession(server);

			session.connect();

		} catch (Exception e) {
			logger.error("socket plugin start失败："+e.getMessage());
			throw new  RuntimeException("行情socket启动失败"+e.getMessage());

		}

	}

	@Override
	public void stop() {
		server.stop();
	}

}

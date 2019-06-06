package com.caimao.jserver.hq.njs;

import java.nio.ByteBuffer;

import com.caimao.jserver.server.core.HQServer;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.caimao.jserver.server.base.BaseSocketHQPlugin;
import com.caimao.jserver.socket.core.IoHandler;
import com.caimao.jserver.socket.core.IoSession;
import com.caimao.jserver.socket.core.Message;
import com.caimao.jserver.socket.core.OverTimeHandler;
import com.caimao.jserver.socket.core.IoProtocol;
public class NJSHQPlugin extends BaseSocketHQPlugin {
	private Logger logger = Logger.getLogger(NJSHQPlugin.class);
	public IoHandler njsHandler;
	public static Element el;
	@Override
	public String getName() {
		return "NJS";
	}

	@Override
	protected IoHandler getHandler() {
		return njsHandler;
	}
	@Override
	public void init(Element el) {
		NJSHQPlugin.el=el;
		//这里做登录用户名，密码可配置
		njsHandler=new NJSHandler(el.getAttribute("user"),el.getAttribute("pwd"));
		super.init(NJSHQPlugin.el);
	}

	@Override
	public Element getEl() {
		return NJSHQPlugin.el;
	}

	@Override
	protected IoProtocol getProtocol() {
		return new IoProtocol() {
			@Override
			public Message onData(ByteBuffer data, IoSession session) {
				// 从缓存中取一下上次是否读过数据
				Response response = (Response) session.getAttribute("data");
				// 如果上次没有存储过，开始读报头
				if (response == null) {
					response = new Response();

				}
				if (response.readHead(data) && response.readBody(data)) {
					session.addAttribute("data", null);
					return response;

				} else {
					session.addAttribute("data", response);
					// 读完再处理
					return null;
				}

			}

		};
	}
	@Override
	public void start(){
		try{
			super.start();

		}catch (Exception ex){

			try {
				HQServer.start("NJS");
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}
	@Override
	protected OverTimeHandler getOverTimeHandler() {

		return new NJSOverTimeHandler(njsHandler);
	}

}

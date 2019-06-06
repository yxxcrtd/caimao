package com.caimao.jserver.hq.sjs;

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

public class SJSHQPlugin extends BaseSocketHQPlugin {
	private IoHandler handler;
	private Logger logger = Logger.getLogger(SJSHQPlugin.class);
	@Override
	public String getName() {
		return "SJS";
	}
	public static Element el;
	@Override
	protected IoHandler getHandler() {
		return handler;
	}
	@Override
	public void init(Element el) {
		SJSHQPlugin.el=el;
		//这里做登录用户名，密码可配置
		handler=new SJSHandler(el.getAttribute("user"),el.getAttribute("pwd"));
		super.init(el);
	}

	@Override
	public Element getEl() {
		return SJSHQPlugin.el;
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
		   System.out.println("上金所行情启动 异常："+ex.getMessage());
		   HQServer.start("SJS");
		   try {
			   Thread.sleep(200000);
		   } catch (InterruptedException e) {
			   e.printStackTrace();
		   }
	   }


	}



	@Override
	protected OverTimeHandler getOverTimeHandler() {
		return new SJSOverTimeHandler(handler);
	}

}

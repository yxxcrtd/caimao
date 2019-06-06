package com.caimao.jserver.hq.njs;

import com.caimao.jserver.socket.core.IoHandler;
import com.caimao.jserver.socket.core.IoSession;
import com.caimao.jserver.socket.core.OverTimeHandler;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


public class NJSOverTimeHandler implements OverTimeHandler{

	private Logger logger = Logger.getLogger(NJSHQPlugin.class);
	private NJSHandler njsHandler;

	@Override
	public void onReadOverTime(IoSession session) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onWriterOverTime(IoSession session) {
		// TODO Auto-generated method stub
		
	}
	public  NJSOverTimeHandler(IoHandler handler){
		if(handler instanceof  NJSHandler){
			njsHandler=(NJSHandler)handler;
		}
	}

	@Override
	public void onBothOverTime(IoSession session) {
		if(null!=njsHandler){
			njsHandler.sendHeartPart(session);
		}else{
			logger.error("发送心跳失败：njsHandler is null");
		}

	}
}

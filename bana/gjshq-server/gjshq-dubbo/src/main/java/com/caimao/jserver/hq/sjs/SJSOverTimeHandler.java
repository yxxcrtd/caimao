package com.caimao.jserver.hq.sjs;

import com.caimao.jserver.socket.core.IoHandler;
import com.caimao.jserver.socket.core.IoSession;
import com.caimao.jserver.socket.core.OverTimeHandler;
import org.apache.log4j.Logger;

public class SJSOverTimeHandler implements OverTimeHandler{
	private Logger logger = Logger.getLogger(SJSOverTimeHandler.class);
	private SJSHandler sjsHandler;
	@Override
	public void onReadOverTime(IoSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWriterOverTime(IoSession session) {
		// TODO Auto-generated method stub
		
	}

	public SJSOverTimeHandler(IoHandler handler){
		if(handler instanceof  SJSHandler){
			sjsHandler=(SJSHandler)handler;
		}
	}
	@Override
	public void onBothOverTime(IoSession session) {

		if(null!=sjsHandler){
			sjsHandler.sendHeartPart(session);
		}else{
			logger.error("发送心跳失败：sjsHandler is null");
		}
	}
}

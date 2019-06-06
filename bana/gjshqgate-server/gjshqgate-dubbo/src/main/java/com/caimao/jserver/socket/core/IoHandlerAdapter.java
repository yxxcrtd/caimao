package com.caimao.jserver.socket.core;

/**
 * <p>
 * Io处理适配器
 * </p>
 * <br>
 * @author lvqi
 *
 */
public abstract class IoHandlerAdapter implements IoHandler {

	@Override
	public void sessionClosed(IoFuture future) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageReceived(IoSession session, Message msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageReceived(IoSession session, byte[] msgdata) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void sessionRegistered(IoSession session) {
		// TODO Auto-generated method stub
		
	}

}

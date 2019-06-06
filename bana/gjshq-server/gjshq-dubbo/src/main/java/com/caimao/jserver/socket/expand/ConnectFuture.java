package com.caimao.jserver.socket.expand;

import com.caimao.jserver.socket.core.IoSession;
import com.caimao.jserver.socket.impl.AbstractIoFuture;

/**
 * <p>
 * 连接的异步计算结果
 * </p>
 * <br>
 * @author lvqi
 *
 */
public class ConnectFuture extends AbstractIoFuture {

	public ConnectFuture(IoSession session) {
		super(session);
	}

	@Override
	protected void completeRun() {
		// TODO Auto-generated method stub

	}

}

package com.caimao.jserver.socket.impl;

import com.caimao.jserver.socket.core.IoSession;

class CloseFuture extends AbstractIoFuture {

	public CloseFuture(IoSession session) {
		super(session);
	}

	@Override
	public void completeRun() {

	}

}

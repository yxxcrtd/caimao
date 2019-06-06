package com.caimao.jserver.socket.impl;

import java.nio.ByteBuffer;

import com.caimao.jserver.socket.core.IoSession;

class WriteFuture extends AbstractIoFuture {

	private ByteBuffer buffer;

	WriteFuture(IoSession session, ByteBuffer buffer) {
		super(session);
		this.buffer = buffer;
	}


	ByteBuffer getBuffer() {
		return buffer;
	}


	@Override
	public void completeRun() {


	}
}

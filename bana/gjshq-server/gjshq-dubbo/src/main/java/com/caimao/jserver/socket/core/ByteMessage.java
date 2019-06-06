package com.caimao.jserver.socket.core;

public class ByteMessage implements Message{
	private byte[] b;
	public ByteMessage(byte[] b) {
		this.b=b;
	}
	@Override
	public byte[] getBytes() {
		return b;
	}

}

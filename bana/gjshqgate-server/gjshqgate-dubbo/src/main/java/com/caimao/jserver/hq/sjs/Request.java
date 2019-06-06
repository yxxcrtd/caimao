package com.caimao.jserver.hq.sjs;

import com.caimao.jserver.socket.core.Message;

public class Request implements Message{
	
	private byte[] content;
	
	public Request(String str) {
		int len=str.length();
		str=String.format("%08d", len)+str;
		System.out.println(str);
		content=str.getBytes();
	}
	public Request(byte[] content) {
		this.content=content;
	}
	@Override
	public byte[] getBytes() {
		return content;
	}

}

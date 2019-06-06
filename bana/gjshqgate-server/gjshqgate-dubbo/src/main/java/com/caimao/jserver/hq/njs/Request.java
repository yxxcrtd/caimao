package com.caimao.jserver.hq.njs;

import com.caimao.jserver.socket.core.Message;
import org.springframework.stereotype.Service;

public class Request implements Message{
	
	private byte[] content;
	
	public Request(byte[] content) {
		this.content=content;
	}
	
	@Override
	public byte[] getBytes() {
		return content;
	}

}

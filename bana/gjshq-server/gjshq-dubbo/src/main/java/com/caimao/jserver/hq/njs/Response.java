package com.caimao.jserver.hq.njs;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.caimao.jserver.socket.core.Message;
import org.springframework.stereotype.Service;

public class Response implements Message {
	private static Charset GBK = Charset.forName("GBK");

	/**
	 * 包体长度
	 */
	private int dataLen;
	/**
	 * 主协议号
	 */
	private int mainType;
	/**
	 * 字协议号
	 */
	private int subType;
	/**
	 * 状态
	 */
	private String state;

	/**
	 * 是否有下一个包
	 */
	private String next;
	
	private char sign;
	private char compress;
	/**
	 * 包体
	 */
	private String body;
	private byte[] bodyBytes;
	private int bodyPos = 0;
	/**
	 * 报头
	 */
	private byte[] headBytes = new byte[32];
	private int headPos = 0;

	public boolean readHead(ByteBuffer data) {
		if (headPos == 32) {
			return true;
		}
		int size = data.remaining();
		if (size < 32 - headPos) {
			
			data.get(headBytes, headPos, size);
			headPos += size;
			return false;
		} else {
			
			data.get(headBytes, headPos, 32 - headPos);
			int iTempLen1 = headBytes[0] & 0xff;
			int iTempLen2 = headBytes[1] & 0xff;
			next = new String(headBytes, 9, 1);
			dataLen = (iTempLen1 << 8) | iTempLen2;
			mainType = headBytes[2] & 0xff;
			subType = headBytes[3] & 0xff;
			bodyBytes = new byte[dataLen];
			headPos = 32;
			return true;
		}

	}

	public boolean readBody(ByteBuffer data) {
		int size = data.remaining();
		if (size < dataLen - bodyPos) {
			data.get(bodyBytes, bodyPos, size);
			bodyPos += size;
			return false;
		} else {
			data.get(bodyBytes, bodyPos, dataLen-bodyPos);
			this.body = new String(bodyBytes, GBK);
			return true;
		}

	}

	public String getBody() {
		return body;
	}

	public int getDataLen() {
		return dataLen;
	}

	public void setDataLen(int dataLen) {
		this.dataLen = dataLen;
	}

	public int getMainType() {
		return mainType;
	}

	public void setMainType(int mainType) {
		this.mainType = mainType;
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public char getSign() {
		return sign;
	}

	public void setSign(char sign) {
		this.sign = sign;
	}

	public char getCompress() {
		return compress;
	}

	public void setCompress(char compress) {
		this.compress = compress;
	}

	@Override
	public byte[] getBytes() {
		return bodyBytes;
	}

}

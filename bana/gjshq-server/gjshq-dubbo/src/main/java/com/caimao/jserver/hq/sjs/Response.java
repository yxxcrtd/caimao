package com.caimao.jserver.hq.sjs;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.caimao.jserver.socket.core.Message;

public class Response implements Message {
	private static Charset GBK = Charset.forName("GBK");
	private static int HEADLENGTH = 8;
	/**
	 * 包体长度
	 */
	private int dataLen;
	/**
	 * 包体
	 */
	private String body;
	private byte[] bodyBytes;
	private int bodyPos = 0;
	/**
	 * 报头
	 */
	private byte[] headBytes = new byte[HEADLENGTH];
	private int headPos = 0;

	public boolean readHead(ByteBuffer data) {
		if (headPos == HEADLENGTH) {
			return true;
		}
		int size = data.remaining();
		if (size < HEADLENGTH - headPos) {
			data.get(headBytes, headPos, size);
			headPos += size;
			return false;
		} else {

			data.get(headBytes, headPos, HEADLENGTH - headPos);

			dataLen = Integer.parseInt(new String(headBytes));

			bodyBytes = new byte[dataLen];
			headPos = HEADLENGTH;
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
			data.get(bodyBytes, bodyPos, dataLen - bodyPos);
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

	@Override
	public byte[] getBytes() {
		return bodyBytes;
	}

}

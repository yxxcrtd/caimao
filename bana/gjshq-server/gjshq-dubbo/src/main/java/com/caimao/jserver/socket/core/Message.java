package com.caimao.jserver.socket.core;

/**
 * <p>
 * 网络消息
 * </p>
 * <br>
 *
 * @author lvqi
 *
 */
public interface Message {
	/** 错误消息 */
	public static final Message ERROR_MSG = new Message(){

		@Override
		public byte[] getBytes() {
			return null;
		}

	};


	public byte[] getBytes();
}

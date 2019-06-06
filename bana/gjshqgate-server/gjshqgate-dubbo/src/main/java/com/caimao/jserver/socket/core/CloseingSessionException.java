package com.caimao.jserver.socket.core;

import java.io.IOException;

/**
 * <p>
 * ??????????????IO??
 * </p>
 * <br>
 * @author lvqi
 *
 */
public class CloseingSessionException extends IOException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CloseingSessionException(String msg) {
		super(msg);
	}
}

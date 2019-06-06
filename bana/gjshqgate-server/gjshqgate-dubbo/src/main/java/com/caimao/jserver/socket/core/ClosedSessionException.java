package com.caimao.jserver.socket.core;

import java.io.IOException;

/**
 * <p>
 * 会话已经被关闭了，再在会话上进行IO操作异常
 * </p>
 * <br>
 * @author lvqi
 *
 */
public class ClosedSessionException extends IOException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ClosedSessionException(String msg) {
        super(msg);
    }
}

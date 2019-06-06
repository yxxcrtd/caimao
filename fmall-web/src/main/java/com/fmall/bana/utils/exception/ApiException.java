package com.fmall.bana.utils.exception;

import com.caimao.bana.common.api.exception.CustomerException;

/**
 * API 抛出的异常
 * Created by Administrator on 2015/9/21.
 */
public class ApiException extends CustomerException {
    public ApiException(Integer code) {
        super(code);
    }

    public ApiException(String message, Integer code) {
        super(message, code);
    }

    public ApiException(String message, Integer code, String sourceType) {
        super(message, code, sourceType);
    }

    public ApiException(String code, String message, String sourceType) {
        super(code, message, sourceType);
    }

    public ApiException(String message, Throwable cause, Integer code) {
        super(message, cause, code);
    }

    public ApiException(Throwable cause, Integer code) {
        super(cause, code);
    }

    public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }
}

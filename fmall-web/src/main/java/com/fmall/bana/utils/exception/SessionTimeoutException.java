package com.fmall.bana.utils.exception;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fmall.bana.utils.enums.ExceptionTypeConstants;
import com.fmall.bana.utils.enums.Separator;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Created by Administrator on 2015/9/29.
 */
@JsonFilter("sessionTimeoutException")
public class SessionTimeoutException extends BaseRuntimeException {
    private Boolean timeout;
    private String msg;

    public SessionTimeoutException() {
        this.timeout = Boolean.TRUE;
        this.seteType(ExceptionTypeConstants.BIZ_EXCEPTION.value());
    }

    public SessionTimeoutException(String... params) {
        super(params);
        this.timeout = Boolean.TRUE;
        this.seteType(ExceptionTypeConstants.BIZ_EXCEPTION.value());
    }

    public void addErrorMessage(String... params) {
        if (this.exceptions == null) {
            this.exceptions = new LinkedHashSet();
        }

        this.exceptions.add(new ErrorMessage("-1000", params[0]));
    }

    public String[] packProperties() {
        return new String[]{"success", "timeout", "msg"};
    }

    public Boolean getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Boolean timeout) {
        this.timeout = timeout;
    }

    public String getMsg() {
        return StringUtils.isBlank(this.msg) ? this.toMsg() : this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toMsg() {
        StringBuilder msg = new StringBuilder();
        Iterator var3 = this.getExceptions().iterator();

        while (var3.hasNext()) {
            Error res = (Error) var3.next();
            msg.append(res.getInfo()).append(Separator.SEMICOLON.value());
        }

        msg.deleteCharAt(msg.length() - 1);
        String res1 = msg.toString();
        this.setMsg(res1);
        return res1;
    }
}

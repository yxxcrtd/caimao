package com.caimao.bana.utils.exception;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.caimao.bana.utils.enums.Separator;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Administrator on 2015/9/29.
 */
@JsonFilter("baseRuntimeException")
public abstract class BaseRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -5087090997512824055L;
    protected Boolean success;
    protected Integer eType;
    protected String message;
    protected String msg;
    protected Collection<Error> exceptions;

    public BaseRuntimeException() {
        this.success = Boolean.FALSE;
    }

    public BaseRuntimeException(String... params) {
        this.success = Boolean.FALSE;
        this.addErrorMessage(params);
    }

    public abstract void addErrorMessage(String... var1);

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Collection<Error> getExceptions() {
        return this.exceptions;
    }

    public void setExceptions(Collection<Error> exceptions) {
        this.exceptions = exceptions;
    }

    public Integer geteType() {
        return this.eType;
    }

    public void seteType(Integer eType) {
        this.eType = eType;
    }

    public String getMessage() {
        return StringUtils.isBlank(this.message)?super.getMessage():this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsg() {
        return StringUtils.isBlank(this.message)?this.toMsg():this.message;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void toMessage() {
        StringBuilder message = new StringBuilder();
        Iterator var3 = this.getExceptions().iterator();

        while(var3.hasNext()) {
            Error msg = (Error)var3.next();
            message.append(msg.getInfo()).append(Separator.SEMICOLON.value());
        }

        message.deleteCharAt(message.length() - 1).append(Separator.PERIOD.value());
        this.setMessage(message.toString());
    }

    public String toMsg() {
        StringBuilder msg = new StringBuilder();
        Iterator var3 = this.getExceptions().iterator();

        while(var3.hasNext()) {
            Error res = (Error)var3.next();
            msg.append(res.getInfo()).append(Separator.SEMICOLON.value());
        }

        msg.deleteCharAt(msg.length() - 1);
        String res1 = msg.toString();
        this.setMsg(res1);
        return res1;
    }

    public String[] packProperties() {
        return new String[]{"success", "msg"};
    }
}

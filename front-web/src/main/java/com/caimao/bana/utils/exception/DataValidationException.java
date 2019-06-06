package com.caimao.bana.utils.exception;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.caimao.bana.utils.enums.ExceptionTypeConstants;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Created by Administrator on 2015/9/29.
 */
@JsonFilter("dataException")
public class DataValidationException extends BaseRuntimeException {
    public DataValidationException() {
        this.seteType(Integer.valueOf(ExceptionTypeConstants.DATA_VALIDATION_EXCEPTION.value()));
    }

    public DataValidationException(String filedName, String errorInfo) {
        super(new String[]{filedName, errorInfo});
        this.seteType(Integer.valueOf(ExceptionTypeConstants.DATA_VALIDATION_EXCEPTION.value()));
    }

    public void addErrorMessage(String... params) {
        if(this.exceptions == null) {
            this.exceptions = new LinkedHashSet();
        }

        this.exceptions.add(new ErrorFiled(params[0], params[1]));
    }

    public void toMessage() {
        StringBuilder message = new StringBuilder();
        Iterator var3 = this.getExceptions().iterator();

        while(var3.hasNext()) {
            Object filed = var3.next();
            message.append(((ErrorFiled)filed).getInfo());
        }

        this.setMessage(message.toString());
    }
}

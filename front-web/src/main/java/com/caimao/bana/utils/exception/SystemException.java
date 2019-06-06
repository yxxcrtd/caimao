package com.caimao.bana.utils.exception;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.caimao.bana.utils.enums.ExceptionTypeConstants;

import java.util.LinkedHashSet;

/**
 * Created by Administrator on 2015/9/29.
 */
@JsonFilter("systemException")
public class SystemException extends BaseRuntimeException {
    private static final long serialVersionUID = 9150472741411593400L;

    public SystemException() {
    }

    public SystemException(String no, String info) {
        super(new String[]{no, info});
        this.seteType(Integer.valueOf(ExceptionTypeConstants.SYSTEM_EXCEPTION.value()));
    }

    public void addErrorMessage(String... params) {
        if(this.exceptions == null) {
            this.exceptions = new LinkedHashSet();
        }

        this.exceptions.add(new ErrorMessage(params[0], params[1]));
    }
}

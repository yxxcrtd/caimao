package com.caimao.gjs.server.utils;

import com.caimao.bana.common.api.exception.CustomerException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yanjg
 *         2015年3月23日
 */
@Component
public class ExceptionUtils {
    @Resource
    private MessageSource messageSource;

    public CustomerException getCustomerException(int code) {
        String message = messageSource.getMessage("error." + code, null, null);
        return new CustomerException(message, code);
    }
}

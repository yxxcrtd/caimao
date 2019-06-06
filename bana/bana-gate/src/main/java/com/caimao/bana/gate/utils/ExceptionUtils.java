package com.caimao.bana.gate.utils;

import com.caimao.bana.common.api.exception.CustomerException;
import org.springframework.stereotype.Component;

/**
 * @author yanjg 2015年4月11日
 */
@Component
public class ExceptionUtils {
    
    /**
     * message = messageSource.getMessage("error.#{错误代码}", null, null)
     *
     * @param code 错误代码
     * @return new CustomerException(错误信息, 错误代码)
     */
    public CustomerException getCustomerException(int code) {
        // 牵涉到多语言的问题，需要在gate端进行判断，设置某一语言的message
        return new CustomerException(code);
    }

}

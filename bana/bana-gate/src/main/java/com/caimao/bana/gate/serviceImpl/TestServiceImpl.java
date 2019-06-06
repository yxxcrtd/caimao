/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.gate.serviceImpl;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.caimao.bana.gate.service.TestService;

/**
 * @author yanjg
 * 2015年4月8日
 */
@Service("testService")
public class TestServiceImpl implements TestService {

    /* (non-Javadoc)
     * @see com.caimao.bana.gate.service.TestService#test(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Object test(HttpServletRequest request) throws Exception {
        // TODO Auto-generated method stub
        return "test:"+System.currentTimeMillis();
    }

}

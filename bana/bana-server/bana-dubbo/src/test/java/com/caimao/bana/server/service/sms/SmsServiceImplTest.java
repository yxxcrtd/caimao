/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.service.sms;

import static org.junit.Assert.*;

import com.caimao.bana.api.entity.req.FSmsListReq;
import com.caimao.bana.api.entity.res.FSmsListRes;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import parent.BaseTest;

/**
 * @author yanjg
 * 2015年4月29日
 */
public class SmsServiceImplTest extends BaseTest{
    @Autowired
    private SmsServiceImpl smsService;
    /**
     * Test method for {@link com.caimao.bana.server.service.sms.SmsServiceImpl#doSendSmsCheckCode(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testDoSendSmsCheckCode() {
        try {
            smsService.doSendSmsCheckCode("15810351906", "5");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("over");
    }


    @Test
    public void queryUnRegisterMobileWithPageTest() throws Exception {
        FSmsListReq req = new FSmsListReq();

        req = this.smsService.queryUnRegisterMobileWithPage(req);

        for (FSmsListRes res : req.getItems()) {
            System.out.println(ToStringBuilder.reflectionToString(res));
        }

    }


}

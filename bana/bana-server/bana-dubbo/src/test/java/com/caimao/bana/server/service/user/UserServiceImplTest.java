/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.service.user;

import com.caimao.bana.api.entity.req.FUserFindLoginPwdReq;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

/**
 * @author yanjg
 * 2015年4月28日
 */
public class UserServiceImplTest extends BaseTest{
    @Autowired
    private UserServiceImpl userService;
    /**
     * Test method for {@link com.caimao.bana.server.service.user.UserServiceImpl#doFindLoginPwd(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testDoFindLoginPwd() {
        try{
            FUserFindLoginPwdReq userFindLoginPwdReq = new FUserFindLoginPwdReq();
            userFindLoginPwdReq.setMobile("18600391314");
            userFindLoginPwdReq.setUserLoginPwd("123456");
            userFindLoginPwdReq.setCheckCode("4353");
            userService.doFindLoginPwd(userFindLoginPwdReq);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

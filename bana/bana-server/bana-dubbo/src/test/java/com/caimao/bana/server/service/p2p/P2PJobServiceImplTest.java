/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.service.p2p;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import parent.BaseTest;

/**
 * @author yanjg
 * 2015年6月11日
 */
public class P2PJobServiceImplTest extends BaseTest{
    @Autowired
    private P2PJobServiceImpl p2pJobService;

    /**
     * Test method for {@link com.caimao.bana.server.service.p2p.P2PJobServiceImpl#doCommitP2PLoanApply()}.
     */
    @Test
    public void testDoCommitP2PLoanApply() {
        try {
            p2pJobService.doCommitP2PLoanApply();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("###########运行结束##############");
    }

    /**
     * Test method for {@link com.caimao.bana.server.service.p2p.P2PJobServiceImpl#doDistributeInterest()}.
     */
    @Test
    public void testDoDistributeInterest() {
        try {
            p2pJobService.doDistributeInterest();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("###########运行结束##############");
    }

    /**
     * Test method for {@link com.caimao.bana.server.service.p2p.P2PJobServiceImpl#doFailedTarget()}.
     */
    @Test
    public void testDoFailedTarget() {
        try {
            p2pJobService.doFailedTarget();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("###########运行结束##############");
    }

    /**
     * Test method for {@link com.caimao.bana.server.service.p2p.P2PJobServiceImpl#doSetInterestTime()}.
     */
    @Test
    public void testDoSetInterestTime() {
        try {
            p2pJobService.doSetInterestTime();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("###########运行结束##############");
    }

}

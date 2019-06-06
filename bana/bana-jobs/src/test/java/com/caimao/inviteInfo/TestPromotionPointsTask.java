/*
*PromotionPointsTash.java
*Created on 2015/5/26 17:24
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.inviteInfo;

import com.caimao.bana.jobs.tasks.PromotionPoints;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class TestPromotionPointsTask extends BaseTest {

    @Resource
    private PromotionPoints promotionPoints;

    @Test
    public void testAddScore() {
        promotionPoints.addPoints();
    }

}

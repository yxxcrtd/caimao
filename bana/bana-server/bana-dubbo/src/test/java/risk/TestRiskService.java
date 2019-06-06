/*
*TestInviteReturn.java
*Created on 2015/5/25 16:45
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package risk;

import com.caimao.bana.server.service.risk.RiskServiceImpl;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class TestRiskService extends BaseTest {

    @Resource
    private RiskServiceImpl riskService;

    @Test
    public void testDoSendRiskSms() throws Exception{
        riskService.doSendRiskSms();
    }
}

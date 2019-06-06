package com.caimao.riskSms;

import com.caimao.bana.jobs.tasks.RiskSms;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

public class TestRiskSms extends BaseTest {
    @Resource
    private RiskSms riskSms;

    @Test
    public void testRiskSms() throws Exception{
        riskSms.doSendRiskSms();
    }
}

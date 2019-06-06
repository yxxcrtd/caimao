package com.caimao.gjs;

import com.caimao.gjs.api.service.IAccountService;
import com.caimao.gjs.api.service.ITradeJobService;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

public class TestGJS extends BaseTest {
    @Resource
    private IAccountService accountService;
    @Resource
    private ITradeJobService tradeJobService;

    @Test
    public void testGoods() throws Exception{
        tradeJobService.updateGoods();
    }

    @Test
    public void testAccountTraderId() throws Exception{
        tradeJobService.updateTraderId();
    }
}

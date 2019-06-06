package com.caimao.inviteInfo;

import com.caimao.bana.jobs.tasks.InviteInfo;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

public class TestInviteInfo extends BaseTest {
    @Resource
    InviteInfo inviteInfo;

    @Test
    public void testDoUpdateWeChatForecast() throws Exception{
        inviteInfo.doUpdateInviteInfo();
    }
}

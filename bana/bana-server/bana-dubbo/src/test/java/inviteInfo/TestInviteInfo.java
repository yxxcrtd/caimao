package inviteInfo;


import com.caimao.bana.api.entity.req.FInviteReturnPageReq;
import com.caimao.bana.api.service.InviteInfoService;
import com.caimao.bana.api.service.InviteReturnHistoryService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

public class TestInviteInfo extends BaseTest {
    @Resource
    private InviteInfoService inviteInfoService;

    @Resource
    InviteReturnHistoryService inviteReturnHistoryService;

    @Test
    public void TestUpdateInviteInfoTask() throws Exception{
        inviteInfoService.updateInviteInfoTask();
    }

    @Test
    public void TestGetInviteReturnHistoryListByUserId() throws Exception{
        FInviteReturnPageReq returnPageReq = new FInviteReturnPageReq();
        FInviteReturnPageReq returnPageReq1 = inviteReturnHistoryService.getInviteReturnHistoryListByUserId(returnPageReq);
        System.out.println(ToStringBuilder.reflectionToString(returnPageReq1));
    }
}

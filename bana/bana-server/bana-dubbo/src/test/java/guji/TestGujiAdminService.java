package guji;

import com.caimao.bana.api.entity.guji.GujiShareRecordEntity;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.entity.req.guji.FQueryAdminShareListReq;
import com.caimao.bana.api.entity.req.guji.FQueryAdminUserListReq;
import com.caimao.bana.api.enums.guji.EGujiAuthStatus;
import com.caimao.bana.api.enums.guji.EGujiShareType;
import com.caimao.bana.api.enums.guji.EGujiStockType;
import com.caimao.bana.api.service.guji.IGujiAdminService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

/**
 * 股计后台服务接口单元测试
 * Created by Administrator on 2016/1/14.
 */
public class TestGujiAdminService extends BaseTest {

    @Autowired
    private IGujiAdminService gujiAdminService;

    /**
     * 查询用户列表
     * @throws Exception
     */
    @Test
    public void queryUserList() throws Exception {
        FQueryAdminUserListReq userListReq = new FQueryAdminUserListReq();
        //userListReq.setAuthStatus(EGujiAuthStatus.NO.getCode());
        //userListReq.setNickName("王");
        userListReq = this.gujiAdminService.queryUserList(userListReq);

        System.out.println(ToStringBuilder.reflectionToString(userListReq));
        for (GujiUserEntity userEntity : userListReq.getItems()) {
            System.out.println(ToStringBuilder.reflectionToString(userEntity));
        }
    }

    /**
     * 查询用户推荐股票列表
     * @throws Exception
     */
    @Test
    public void queryShareList() throws Exception {
        FQueryAdminShareListReq shareListReq = new FQueryAdminShareListReq();
        //shareListReq.setNickName("王");
        //shareListReq.setOperType(EGujiShareType.JC.getCode());
        //shareListReq.setStockType(EGujiStockType.GP.getCode());
        //shareListReq.setStockCode("601601");
        shareListReq = this.gujiAdminService.queryShareList(shareListReq);

        System.out.println(ToStringBuilder.reflectionToString(shareListReq));
        for (GujiShareRecordEntity entity: shareListReq.getItems()) {
            System.out.println(ToStringBuilder.reflectionToString(entity));
        }
    }

    /**
     * 更新认证用户状态
     * @throws Exception
     */
    @Test
    public void authUserStatus() throws Exception {
        Long wxId = 4L;
        Integer authStatus = EGujiAuthStatus.AUDIT.getCode();

        this.gujiAdminService.authUserStatus(wxId, authStatus);
    }


}

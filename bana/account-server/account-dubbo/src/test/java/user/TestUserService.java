package user;


import com.caimao.account.api.service.IUserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

/**
 * 用户服务单元测试
 * Created by WangXu on 2015/5/25.
 */
public class TestUserService extends BaseTest {

    @Autowired
    private IUserService userService;

    @Test
    public void TestGetUserIdByPhone() throws Exception {
        String phone = "18612839215";
        Long userId = this.userService.getUserIdByPhone(phone);
        System.out.println("根据手机号查询用户ID，返回：" + userId);
    }


}

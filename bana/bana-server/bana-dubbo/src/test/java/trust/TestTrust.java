package trust;

import com.caimao.bana.api.service.IUserTrustService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import parent.BaseTest;

import javax.annotation.Resource;
import java.util.Random;


/**
 * 实名认证相关测试
 * Created by WangXu on 2015/5/18.
 */
public class TestTrust extends BaseTest {

    @Resource
    private IUserTrustService trustService;

    @Test
    public void TestVoid() {

    }

    @Test
    public void TestDoVerifyIdentity() throws Exception {
        Long userId = 800795779923969L;
        String realName = "韦伟";
        String idCardKind = "1";
        String idCard = "350702199112242322";

        try {
            Long res = this.trustService.doVerifyIdentity(userId, realName, idCardKind, idCard);
            System.out.println("实名认证成功，返回 ：" + res);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

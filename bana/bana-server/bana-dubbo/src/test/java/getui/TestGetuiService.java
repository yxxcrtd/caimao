package getui;

import com.caimao.bana.api.entity.getui.GetuiUserIdMapEntity;
import com.caimao.bana.api.entity.req.getui.FGetuiPushMessageReq;
import com.caimao.bana.api.enums.getui.EGetuiActionType;
import com.caimao.bana.api.enums.getui.EGetuiDeviceType;
import com.caimao.bana.api.service.getui.IGetuiJobService;
import com.caimao.bana.api.service.getui.IGetuiService;
import com.caimao.bana.server.service.getui.GetuiSendRunnable;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * 个推服务测试
 * Created by Administrator on 2015/11/5.
 */
public class TestGetuiService extends BaseTest {

    @Resource
    IGetuiService getuiService;
    @Resource
    IGetuiJobService getuiJobService;

    /**
     * 绑定客户端与用户之间的关联关系
     */
    @Test
    public void bindCidAndUserIdTest() {
        GetuiUserIdMapEntity userIdMapEntity = new GetuiUserIdMapEntity();
        /** android */
//        userIdMapEntity.setUserId(123123L);
//        userIdMapEntity.setCid("503ca26ba524ad5f5da86e6f11a0949e");

        /** iphone */
        userIdMapEntity.setUserId(808758951280641L);
        userIdMapEntity.setCid("5c59c3e05c8772f748383705b9b2ad89");

        userIdMapEntity.setDeviceType(EGetuiDeviceType.ANDROID.getValue());
        userIdMapEntity.setDeviceToken("051fe328f602aec55308ec1aa7deb03f");

        this.getuiService.bindCidAndUserId(userIdMapEntity);
    }

    /**
     * 解绑客户端与用户之间的关连关系
     */
    @Test
    public void unbindCidAndUserIdTest() {
        GetuiUserIdMapEntity userIdMapEntity = new GetuiUserIdMapEntity();
        userIdMapEntity.setUserId(808758951280641L);
        userIdMapEntity.setCid("b848491d03cbcb8483159d701549164f");

        this.getuiService.unbindCidAndUserId(userIdMapEntity);
    }

    /**
     * 添加单个用户的推送消息
     */
    @Test
    public void pushMessageToSingleTest() throws InterruptedException {
        FGetuiPushMessageReq req = new FGetuiPushMessageReq();
        req.setUserId(809250154610689L);


        req.setActionType(EGetuiActionType.TYPE_OPENAPP.getValue());
        req.setContent("透传APP的消息内容");
        req.setTitle("透传APP的消息标题");
        req.setSource("app");
        req.setUrl("https://www.caimao.com");

        this.getuiService.pushMessageToSingle(req);

        Thread.sleep(10000);
    }

    /**
     * 发送推送消息
     * @throws Exception
     */
    @Test
    public void runGetuiSendTest() throws Exception {
        this.getuiJobService.start();
    }

}

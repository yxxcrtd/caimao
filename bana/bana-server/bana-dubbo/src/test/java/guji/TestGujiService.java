package guji;

import com.caimao.bana.api.entity.guji.GujiShareRecordEntity;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.entity.guji.GujiUserStockEntity;
import com.caimao.bana.api.entity.req.guji.FQueryGujiFollowShareListReq;
import com.caimao.bana.api.entity.req.guji.FQueryGujiHallShareListReq;
import com.caimao.bana.api.enums.guji.EGujiAuthStatus;
import com.caimao.bana.api.enums.guji.EGujiShareType;
import com.caimao.bana.api.enums.guji.EGujiStockType;
import com.caimao.bana.api.service.guji.IGujiService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.List;

/**
 * 股计服务测试
 * Created by Administrator on 2016/1/8.
 */
public class TestGujiService extends BaseTest {

    @Autowired
    private IGujiService gujiService;

    /**
     * 根据数据库中的ID获取微信用户信息
     * @throws Exception
     */
    @Test
    public void getUserByIdTest() throws Exception {
        Long wxId = 1L;
        GujiUserEntity userEntity = this.gujiService.getUserById(wxId);
        System.out.println(ToStringBuilder.reflectionToString(userEntity));
    }

    /**
     * 根据微信唯一标示获取数据库中的用户信息
     * @throws Exception
     */
    @Test
    public void getUserByOpenIdTest() throws Exception {
        String openId = "123123123123123123";
        GujiUserEntity userEntity = this.gujiService.getUserByOpenId(openId);
        System.out.println(ToStringBuilder.reflectionToString(userEntity));
    }

    /**
     * 添加/更新微信用户个人信息
     * @throws Exception
     */
    @Test
    public void addOrUpdateUserInfoTest() throws Exception {
        GujiUserEntity userEntity = new GujiUserEntity();
        userEntity.setOpenId("o4BrVsjqjAIqH2SvNeX772AXBK_8");
//        userEntity.setNickname("123");
//        userEntity.setHeadimgurl("456");
        userEntity.setSubscribe(1);
//        userEntity.setPublicRecom(1);
//        userEntity.setCertificationAuth("auth");
//        userEntity.setCardPic("pic");
//        userEntity.setAuthStatus(EGujiAuthStatus.NO.getCode());
        Boolean res = this.gujiService.addOrUpdateUserInfo(userEntity);

        System.out.println(res);
    }



    /**
     * 添加股票推荐的信息
     * @throws Exception
     */
    @Test
    public void addShareStockInfoTest() throws Exception {
        GujiShareRecordEntity entity = new GujiShareRecordEntity();
        entity.setWxId(3L);
        entity.setOpenId("564565465465");
        entity.setStockType(EGujiStockType.GP.getCode());
        entity.setStockCode("100002");
        entity.setStockName("股票名称");
        entity.setStockPrice("123.12");
        entity.setPositions(40);
        entity.setOperType(EGujiShareType.TC.getCode());
        entity.setReason("推荐理由");

        Boolean res = this.gujiService.addShareStockInfo(entity);
        System.out.println(res);
    }

    /**
     * 查询大厅中用户的股票推荐列表
     * @throws Exception
     */
    @Test
    public void queryHallShareListTest() throws Exception {
        FQueryGujiHallShareListReq req = new FQueryGujiHallShareListReq();
        req.setCheckOpenId("123123123123123123");
        req.setCurrentPage(1);
        req.setLimit(20);

        req = this.gujiService.queryHallShareList(req);

        for (GujiShareRecordEntity entity : req.getItems()) {
            System.out.println(ToStringBuilder.reflectionToString(entity));
        }
        System.out.println(ToStringBuilder.reflectionToString(req));
    }

    /**
     * 查询我关注用户的股票推荐列表
     * @throws Exception
     */
    @Test
    public void queryFollowShareListTest() throws Exception {
        FQueryGujiFollowShareListReq req = new FQueryGujiFollowShareListReq();
        req.setWxId(3L);
        req.setOpenId("564565465465");
        req.setCurrentPage(1);
        req.setLimit(20);

        req = this.gujiService.queryFollowShareList(req);

        for (GujiShareRecordEntity entity : req.getItems()) {
            System.out.println(ToStringBuilder.reflectionToString(entity));
        }
        System.out.println(ToStringBuilder.reflectionToString(req));
    }

    /**
     * 获取我的股票持仓列表
     * @throws Exception
     */
    @Test
    public void getMyStockListTest() throws Exception {
        Long wxId = 4L;
        List<GujiUserStockEntity> userStockEntityList = this.gujiService.getMyStockList(wxId);
        for (GujiUserStockEntity entity : userStockEntityList) {
            System.out.println(ToStringBuilder.reflectionToString(entity));
        }
    }

    /**
     * 查询特定用户推荐的股票历史列表
     * @throws Exception
     */
    @Test
    public void queryStockShareHistoryListTest() throws Exception {
        Long wxId = 3L;
        String stockCode = "100002";
        String checkOpenId = null; //"564565465465";

        List<GujiShareRecordEntity> shareRecordEntityList = this.gujiService.queryStockShareHistoryList(wxId, stockCode, checkOpenId);

        for (GujiShareRecordEntity entity: shareRecordEntityList) {
            System.out.println(ToStringBuilder.reflectionToString(entity));
        }

    }

    /**
     * 检查用户之间是否关注关系
     * @throws Exception
     */
    @Test
    public void checkIsFollowTest() throws Exception {
        String openId = "564565465465";
        Long focusWxId = 3L;
        Boolean res = this.gujiService.checkIsFollow(openId, focusWxId);
        System.out.println(res);
    }

    /**
     * 我给你一个赞 ！
     * @throws Exception
     */
    @Test
    public void giveLikeTest() throws Exception {
        String openId = "564565465465";
        Long shareId = 5L;
        Boolean res = this.gujiService.giveLike(openId, shareId);
        System.out.println(res);
    }

}

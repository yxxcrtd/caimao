package ybk;

import com.caimao.bana.api.entity.req.ybk.FYbkApiQueryAccountListReq;
import com.caimao.bana.api.entity.res.ybk.FYbkAccountSimpleRes;
import com.caimao.bana.api.entity.ybk.YBKAccountEntity;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import com.caimao.bana.api.service.ybk.IYBKAccountService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.List;

/**
 * 邮币卡账户申请的服务测试
 * Created by Administrator on 2015/9/16.
 */
public class YbkAccountServerTest extends BaseTest {

    @Autowired
    private IYBKAccountService ybkAccountService;

    @Test
    public void queryApiAccountApplyTest() throws Exception {
        FYbkApiQueryAccountListReq req = new FYbkApiQueryAccountListReq();
        req.setUserId(809250154610689L);
        req = this.ybkAccountService.queryApiAccountApply(req);
        if (req.getItems() != null) {
            for (FYbkAccountSimpleRes res : req.getItems()) {
                System.out.println(ToStringBuilder.reflectionToString(res));
            }
        }
    }

    @Test
    public void insertTest() throws Exception {
        YBKAccountEntity entity = new YBKAccountEntity();
        entity.setUserId(123123L);
        entity.setUserName("Test");
        entity.setPhoneNo("123123");
        entity.setCardType(1);
        entity.setCardPath("11111");
        entity.setCardOppositePath("2222");
        entity.setCardNumber("3333");
        entity.setBankCode("001");
        entity.setBankNum("112312312312");
        entity.setBankPath("444444");
        entity.setExchangeIdApply(16);

        this.ybkAccountService.insert(entity);
    }


    @Test
    public void mayOpenExchangeTest() throws Exception {
        Long userId = 808418038251521L;

        List<YbkExchangeEntity> list = this.ybkAccountService.mayOpenExchange(userId);

        for (YbkExchangeEntity entity : list) {
            System.out.println(ToStringBuilder.reflectionToString(entity));
        }

    }

    @Test
    public void oneStepOpenExchangeTest() throws Exception {
        Long userId = 809250154610689L;
        String exchangeShortNo = "znwjs";

        YBKAccountEntity entity = this.ybkAccountService.oneStepOpenExchange(userId, exchangeShortNo);

        System.out.println(ToStringBuilder.reflectionToString(entity));

    }

}

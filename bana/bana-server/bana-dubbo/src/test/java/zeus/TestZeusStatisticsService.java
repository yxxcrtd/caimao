/*
*TestInviteReturn.java
*Created on 2015/5/25 16:45
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package zeus;

import com.caimao.bana.api.service.IZeusStatisticsService;
import com.caimao.bana.server.service.risk.RiskServiceImpl;
import com.caimao.bana.server.utils.RedisUtils;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class TestZeusStatisticsService extends BaseTest {

    @Resource
    private IZeusStatisticsService zeusStatisticsService;
    @Resource
    RedisUtils redisUtils;

    @Test
    public void testTotal() throws Exception {
        Long userId = 802184463646721L;
        System.out.println(this.zeusStatisticsService.queryUserDepositTotal(userId));
        System.out.println(this.zeusStatisticsService.queryUserWithdrawTotal(userId));
        System.out.println(this.zeusStatisticsService.queryLastWithdrawSuccess(userId));
    }

    @Test
    public void testUserBalanceDailySave() throws Exception{
        zeusStatisticsService.userBalanceDailySave();
    }

    @Test
    public void testUpdateHomsAccountLoanLog() throws Exception{
        zeusStatisticsService.updateHomsAccountLoanLog();
    }

    @Test
    public void testQueryUserHomsStatistics() throws Exception{
        List<HashMap<String, Object>> userHomsStatistics = zeusStatisticsService.queryUserHomsStatistics(802185872932865L);
        System.out.println(userHomsStatistics);
    }

    @Test
    public void testQueryDailyNewUser() throws Exception{
        List<HashMap> dataList = zeusStatisticsService.queryDailyNewUser("2015-06-07", "2015-06-07", 1, "");
        System.out.println(dataList);
    }

    @Test
    public void testRedis() throws Exception{
        redisUtils.hSet(0, "userLastMsg", "123123", "8888");
        System.out.println(redisUtils.hGet(0, "userLastMsg", "123123"));
        Long lastPushId = Long.parseLong(redisUtils.hGet(0, "userLastMsg", "88888").toString());
        System.out.println(lastPushId);


    }

    @Test
    public void testRemoveContract() throws Exception{
        Long contractNo = 800446008524801L;
        zeusStatisticsService.removeContract(contractNo);
    }
}

package p2p;

import com.caimao.bana.api.entity.p2p.P2PConfigEntity;
import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.entity.res.FP2PParameterRes;
import com.caimao.bana.api.entity.res.FP2PQueryLoanPageInvestWithUserRes;
import com.caimao.bana.api.enums.EP2PLoanStatus;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IP2PService;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.service.p2p.P2PServiceImpl;
import com.caimao.bana.server.utils.AccountManager;
import com.caimao.bana.server.utils.BigMath;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * P2P单元测试
 * Created by WangXu on 2015/6/9.
 */

public class TestP2PService extends BaseTest {
    @Resource
    private IP2PService ip2PService;

    @Autowired
    private P2PServiceImpl p2PService;

    @Autowired
    private TpzAccountDao accountDao;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private IP2PService p2pService;
    @Test
    public void TestBigDecimal() throws Exception {

        String Phone = "123123";
        String phoneStr = "18353665592，13811791759，15954308332";
        if (phoneStr.indexOf(Phone) >= 0) {
            System.out.println("不允许提现");
        }


        // 整形与整形的加减乘除
        int a = 10;
        int b = 5;
        System.out.println("普通加法的");
        System.out.println(String.format("%s + %s = %s", a, b, new BigDecimal(a).add(new BigDecimal(b))));
        System.out.println("普通减法的");
        System.out.println(String.format("%s - %s = %s", a, b, new BigDecimal(a).subtract(new BigDecimal(b))));

        Long c = 1000L;
        double d = 0.05;
        System.out.println("普通除法的");
        System.out.println(String.format("%s / %s = %s", c, d, BigMath.divide(c, d)));
        System.out.println("普通乘法的");
        System.out.println(String.format("%s * %s = %s", c, d, BigMath.multiply(c, d)));

        System.out.println("王旭".substring(0, 1));

        System.out.println(BigMath.compareTo(0.9999, 1));

        Long loanInterest = BigMath.multiply(6000000L, 0.015).longValue();
        loanInterest = BigMath.multiply(loanInterest, BigMath.divide(30, 30).intValue()).longValue();
        System.out.println(loanInterest);

    }
    
    @Test
    public void getParametr() {
        System.out.println("获取p2p配置参数：");
        List<FP2PParameterRes> list = p2pService.getP2PParameter();
        for(FP2PParameterRes res :list){
            System.out.println(res.getParamName());
        }
    }

    @Test
    public void doAddLoan() throws Exception {
        FP2PAddLoanReq req = new FP2PAddLoanReq();
        req.setUserId(799869644046338L);
        req.setTradePwd("wangxu123,");
        req.setCaimaoValue(0L);
        req.setCustomRate(0.015);
        req.setLever(2);
        req.setLoanValue(6000000L);
        req.setMargin(3000000L);
        req.setPeroid(30);
        req.setProdId(3L);

        Long targetId = this.p2PService.doAddLoan(req);
        System.out.println("创建的标的ID " + targetId);
    }

    @Test
    public void getProdSettingByProdId() {
        Long prodId = 4L;
        List<P2PConfigEntity> configEntityList = this.p2pService.getProdSettingByProdId(prodId);

        for (int i = 0; i < configEntityList.size(); i++) {
            System.out.println("第 " + i + " 个");
            System.out.println(ToStringBuilder.reflectionToString(configEntityList.get(i)));
        }
    }

    @Test
    public void TestQueryUserSummaryInfo() throws Exception{
        Map<String, Object> userSummary = ip2PService.queryUserSummaryInfo(802184463646721L);
        System.out.println(userSummary);
    }

    @Test
    public void TestQueryUserPageInvest() throws Exception{
        FP2PQueryUserPageInvestReq fp2PQueryUserPageInvestReq = new FP2PQueryUserPageInvestReq();

        fp2PQueryUserPageInvestReq.setUserId(111L);
        fp2PQueryUserPageInvestReq.setInvestStatus(new Byte("1"));
        fp2PQueryUserPageInvestReq.setStartDate("2015-06-08");
        fp2PQueryUserPageInvestReq.setEndDate("2015-06-10");
        fp2PQueryUserPageInvestReq.setStart(Integer.valueOf("1"));
        fp2PQueryUserPageInvestReq.setLimit(Integer.valueOf("10"));

        fp2PQueryUserPageInvestReq = ip2PService.queryUserPageInvest(fp2PQueryUserPageInvestReq);
        System.out.println(fp2PQueryUserPageInvestReq);
    }

    @Test
    public void TestQueryUserInvestRecord() throws Exception{
        System.out.println(ip2PService.queryUserInvestRecord(111L, 111L));
    }

    @Test
    public void TestQueryUserPageInterest() throws Exception{
        FP2PQueryUserPageInterestReq fp2PQueryUserPageInterestReq = new FP2PQueryUserPageInterestReq();
        fp2PQueryUserPageInterestReq.setUserId(111L);
        fp2PQueryUserPageInterestReq.setInvestId(2222L);

        System.out.println(ip2PService.queryUserPageInterest(fp2PQueryUserPageInterestReq));
    }

    @Test
    public void TestQueryUserPageLoan() throws Exception{
        FP2PQueryUserPageLoanReq fp2PQueryUserPageLoanReq = new FP2PQueryUserPageLoanReq();
        fp2PQueryUserPageLoanReq.setUserId(111L);
        fp2PQueryUserPageLoanReq.setTargetStatus(new Byte("1"));
        fp2PQueryUserPageLoanReq.setStartDate("2015-06-08");
        fp2PQueryUserPageLoanReq.setEndDate("2015-06-10");

        System.out.println(ip2PService.queryUserPageLoan(fp2PQueryUserPageLoanReq));
    }

    @Test
    public void TestQueryUserLoanRecord() throws Exception{
        P2PLoanRecordEntity P2PLoanRecordEntity = ip2PService.queryUserLoanRecord(1L, 2L);

        System.out.println(P2PLoanRecordEntity);
    }

    @Test
    public void TestQueryPageLoan() throws Exception{
        FP2PQueryPageLoanReq fp2PQueryPageLoanReq = new FP2PQueryPageLoanReq();
        fp2PQueryPageLoanReq.setTargetStatus(new Byte("1"));

        System.out.println(ip2PService.queryPageLoan(fp2PQueryPageLoanReq));
    }

    @Test
    public void TestQueryLoanRecord() throws Exception{
        P2PLoanRecordEntity P2PLoanRecordEntity = ip2PService.queryLoanRecord(1L);

        System.out.println(P2PLoanRecordEntity);
    }

    @Test
    public void TestQueryLoanPageInvest() throws Exception{
        FP2PQueryLoanPageInvestReq fp2PQueryLoanPageInvestReq = new FP2PQueryLoanPageInvestReq();
        fp2PQueryLoanPageInvestReq.setTargetId(2L);

        System.out.println(ip2PService.queryLoanPageInvest(fp2PQueryLoanPageInvestReq));
    }

    @Test
    public void queryPageInvestListWithUser() throws Exception {
        FP2PQueryPageInvestListReq req = new FP2PQueryPageInvestListReq();

        req = this.p2pService.queryPageInvestListWithUser(req);

        System.out.println(ToStringBuilder.reflectionToString(req));
        for(FP2PQueryLoanPageInvestWithUserRes res: req.getItems()) {
            System.out.println(ToStringBuilder.reflectionToString(res));
        }
    }

    @Test
    public void TestdoFullCaimaoP2PInvest() throws Exception {
        Long targetId = 52L;
        boolean res = this.p2pService.doFullCaimaoP2PInvest(targetId);
        System.out.println(res);
    }

    @Test
    public void TestQueryLoanCount() throws Exception{
        System.out.println(ip2PService.queryLoanCount(EP2PLoanStatus.CANCEL.getCode()));
    }

    @Test
    public void TestDoFailedTarget() throws Exception{
        p2pService.doFailedTarget(58L, 802184463646721L, EP2PLoanStatus.CANCEL);
    }
}

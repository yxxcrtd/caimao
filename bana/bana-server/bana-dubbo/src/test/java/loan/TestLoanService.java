package loan;

import com.caimao.bana.api.entity.req.loan.FLoanP2PApplyReq;
import com.caimao.bana.api.entity.res.F830216Res;
import com.caimao.bana.api.entity.res.loan.FLoanP2PApplyRes;
import com.caimao.bana.server.service.loan.LoanServiceImpl;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

/**
 * 配资合约测试
 * Created by Administrator on 2015/7/24.
 */
public class TestLoanService extends BaseTest {

    @Autowired
    private LoanServiceImpl loanService;


    @Test
    public void TestQueryLoanP2PApplyList() throws Exception {
        FLoanP2PApplyReq req = new FLoanP2PApplyReq();
        req.setUserId(802184463646721L);

        req = this.loanService.queryLoanP2PApplyList(req);

        System.out.println(ToStringBuilder.reflectionToString(req));
        if (req.getItems() != null) {
            for (FLoanP2PApplyRes res : req.getItems()) {
                System.out.println(ToStringBuilder.reflectionToString(res));
            }
        }
    }

    @Test
    public void testGetTotalLoanAndBill() throws Exception {
        F830216Res res = this.loanService.getTotalLoanAndBill(802184463646721L);
        System.out.println(ToStringBuilder.reflectionToString(res));
    }

}

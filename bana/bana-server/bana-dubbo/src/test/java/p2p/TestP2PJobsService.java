package p2p;

import com.caimao.bana.api.service.IP2PJobService;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

public class TestP2PJobsService extends BaseTest {
    @Resource
    private IP2PJobService P2PJobService;

    @Test
    public void testDoCommitP2PLoanApply() throws Exception{
        P2PJobService.doCommitP2PLoanApply();
    }

    @Test
    public void testDoSetInterestTime() throws Exception{
        P2PJobService.doSetInterestTime();
    }

    @Test
    public void testAutoExtTarget() throws Exception{
        P2PJobService.autoExtTarget();
    }

    @Test
    public void testDoDistributeInterest() throws Exception{
        P2PJobService.doDistributeInterest();
    }

    @Test
    public void testExtTargetFull() throws Exception{
        P2PJobService.extTargetFull();
    }

    @Test
    public void testSearchP2PContractNo() throws Exception{
        P2PJobService.searchP2PContractNo();
    }

    @Test
    public void testUpdateRepaymentTarget() throws Exception{
        P2PJobService.updateRepaymentTarget();
    }

    @Test
    public void testCleanP2PNextSettleInterestDate() throws Exception{
        P2PJobService.cleanP2PNextSettleInterestDate();
    }

    @Test
    public void TestAutoFullTarget() throws Exception {
        this.P2PJobService.autoFullTarget();
    }
}

package btcPledge;

import com.caimao.bana.server.service.btcPledge.BtcPledgeServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

public class TestBtcPledgeService extends BaseTest {

    @Autowired
    private BtcPledgeServiceImpl btcPledgeService;

    @Test
    public void TestSave() throws Exception {
        Long orderNo = btcPledgeService.doSavePledgeOrder(802185872932865L, 1000L, "测试抵押", "2", "bitvc");
        //Long orderNo = btcPledgeService.doSavePledgeOrder(802185872932865L, 1000L, "测试抵押还款", "1", "bitvc");
        System.out.println(orderNo);
    }
}
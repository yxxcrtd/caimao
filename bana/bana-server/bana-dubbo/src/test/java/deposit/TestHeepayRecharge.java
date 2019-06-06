package deposit;

import com.caimao.bana.api.entity.HeepayRequestEntity;
import com.caimao.bana.server.service.deposit.HeepayRechargeImpl;
import com.caimao.bana.server.utils.ChannelUtil;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * Created by WangXu on 2015/4/24.
 */
public class TestHeepayRecharge extends BaseTest {

    @Resource
    private HeepayRechargeImpl heepayRecharge;

    @Autowired
    private MemoryDbidGenerator memoryDbidGenerator;

    @Autowired
    private ChannelUtil channelUtil;

    @Test
    public void testTest() throws Exception {

        String sign = this.channelUtil.MD5(String.format("version=%s&agent_id=%s&agent_bill_id=%s&agent_bill_time=%s&pay_type=%s&pay_amt=%s&notify_url=%s&return_url=%s&user_ip=%s&is_test=%s&key=%s",
                "1",
                "1966362",
                "799874777874434",
                "20150429020643",
                "20",
                "111",
                "https://106.38.234.48/account/charge/heepay/review/async",
                "https://106.38.234.48/account/charge/heepay/review/sync",
                "127_0_0_1",
                "1",
                "B91C8405285E4E458F7E29D8"
        )).toLowerCase();

        System.out.println(sign);
    }

    @Test
    public void testDoCharge() throws Exception {
        HeepayRequestEntity entity = new HeepayRequestEntity();
        entity.setOrderNo(804698999226369L);
        entity.setUserId(802184463646721L);
        entity.setPayResult("03");
        entity.setBankResultCode("001");
        entity.setWorkDate("20150818");
        entity.setOrderAmount(10000L);
        entity.setRemark("测试");

        this.heepayRecharge.doCharge(entity);

    }


    @Test
    public void testCheckHeepayPayResult() throws Exception {
        Long orderNo = 799779114188801L;
        boolean b = this.heepayRecharge.checkHeepayPayResult(orderNo);
        System.out.println(b);
    }



}

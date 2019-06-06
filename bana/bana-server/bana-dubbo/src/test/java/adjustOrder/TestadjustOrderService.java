package adjustOrder;

import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.server.service.adjustOrder.AdjustOrderService;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * Created by WangXu on 2015/4/28.
 */
public class TestadjustOrderService extends BaseTest {

    @Resource
    private AdjustOrderService adjustOrderService;

    @Test
    public void testDoSaveAdjustOrder() throws Exception {
        Long userId = 799436825427971L;
        Long orderAmount = 1L;
        Long orderAmount2 = 2L;

        // 测试蓝补加钱的
        System.out.println("蓝补测试，需要审核的");
        Long res = this.adjustOrderService.doSaveAdjustOrder(userId, orderAmount, "蓝补TEST", ESeqFlag.COME.getCode(), "Test", true);
        System.out.println(res);

        // 测试红冲减钱的
        System.out.println("红冲测试，需要审核的");
        res = this.adjustOrderService.doSaveAdjustOrder(userId, orderAmount, "红冲TEST", ESeqFlag.GO.getCode(), "Test", true);
        System.out.println(res);

        // 测试蓝补加钱的
        System.out.println("蓝补测试，需要审核的");
        res = this.adjustOrderService.doSaveAdjustOrder(userId, orderAmount2, "蓝补TEST-0", ESeqFlag.COME.getCode(), "Test", false);
        System.out.println(res);

        // 测试红冲减钱的
        System.out.println("红冲测试，需要审核的");
        res = this.adjustOrderService.doSaveAdjustOrder(userId, orderAmount2, "红冲TEST-0", ESeqFlag.GO.getCode(), "Test", false);
        System.out.println(res);
    }

}

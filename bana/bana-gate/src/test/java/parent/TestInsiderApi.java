package parent;

import com.caimao.bana.api.entity.AlipayRecordEntity;
import com.caimao.bana.api.service.IAlipayService;
import com.caimao.bana.gate.utils.ValidUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by WangXu on 2015/5/4.
 */
public class TestInsiderApi {
    @Resource
    private IAlipayService alipayService;

    @Test
    public void mapSort() {

        Map<String, Object> signMap = new HashMap<>();
        signMap.put("user_id", "123123");
        signMap.put("order_amount", "1000");
        signMap.put("seq_flag", "out");
        signMap.put("order_abstract", "sldkfjalksd");
        signMap.put("oper_user", "sdfalksd");
        signMap.put("need_audit", "true");

        String signMD5 = ValidUtils.insiderApiSign(signMap);

        System.out.println(signMD5);


        System.out.println(Boolean.valueOf("false"));

    }

    @Test
    public void testAlipay() throws Exception{
        AlipayRecordEntity alipayRecordEntity = new AlipayRecordEntity();
        alipayRecordEntity.setAmount(100L);
        alipayRecordEntity.setRealName("宁豪");
        alipayRecordEntity.setTradeId(new BigDecimal("10909999"));
        alipayRecordEntity.setFinishTime(new Date());
        alipayRecordEntity.setCreateTime(new Date());
        alipayService.saveTradeRecord(alipayRecordEntity);
    }
}

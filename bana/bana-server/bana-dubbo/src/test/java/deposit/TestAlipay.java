package deposit;

import com.caimao.bana.api.entity.AlipayRecordEntity;
import com.caimao.bana.api.service.IAlipayService;
import com.caimao.bana.server.dao.depositDao.AlipayRecordDao;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

public class TestAlipay extends BaseTest {
    @Resource
    private IAlipayService alipayService;
    @Resource
    private AlipayRecordDao alipayRecordDao;

    @Test
    public void testProcessAlipay() throws Exception{
        alipayService.processAlipay();
    }

    @Test
    public void testAlipayToSuccess() throws Exception{
        alipayService.alipayToSuccess(804698999226369L);

    }

    @Test
    public void testSave() throws Exception{
        AlipayRecordEntity alipayRecordEntity = new AlipayRecordEntity();
        alipayRecordEntity.setAmount(1000L);
        alipayRecordEntity.setCreateTime(new Date());
        alipayRecordEntity.setFinishTime(new Date());
        alipayRecordEntity.setTradeId(new BigDecimal("1111111"));
        alipayRecordEntity.setRealName("宁豪");
        alipayRecordDao.saveTradeRecord(alipayRecordEntity);
    }

    @Test
    public void testXX() throws Exception{
        String amount = "30000.00";
        System.out.println(new BigDecimal(amount));
    }
}

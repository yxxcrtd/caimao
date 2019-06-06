package optionalStock;

import com.caimao.bana.api.service.IOptionalStockService;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

public class TestOptionalStock extends BaseTest{
    @Resource
    private IOptionalStockService optionalStockService;

    @Test
    public void testStockInfo() throws Exception{
        System.out.println(optionalStockService.queryStockNameFromSina("000001"));
    }
}

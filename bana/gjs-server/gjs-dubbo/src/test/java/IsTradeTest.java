import com.caimao.gjs.api.service.ITradeService;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * 交易所是否休市
 *
 * Created by yangxinxin@huobi.com on 2015-11-10
 */
public class IsTradeTest extends BaseTest {

    @Resource
    private ITradeService tradeService;

    /**
     * 交易所是否休市
     */
    @Test
    public void isTrade() throws Exception {
        System.out.println(this.tradeService.isTrade("NJS"));
    }

}

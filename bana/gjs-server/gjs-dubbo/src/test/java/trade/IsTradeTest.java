package trade;

import com.caimao.gjs.api.service.ITradeService;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * 贵金属的单元测试
 *
 * Created by yangxinxin@huobi.com on 2015/12/16
 */
public class IsTradeTest extends BaseTest {

    @Resource
    private ITradeService tradeService;

    @Test
    public void isTrade() throws Exception {
        System.out.println(this.tradeService.isTrade("SJS"));
    }

}

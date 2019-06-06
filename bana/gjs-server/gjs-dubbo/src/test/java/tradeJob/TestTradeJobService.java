package tradeJob;

import com.caimao.gjs.api.entity.req.FQueryHistoryTransferReq;
import com.caimao.gjs.api.service.ITradeJobService;
import com.caimao.gjs.api.service.ITradeService;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * 交易脚本服务测试
 */
public class TestTradeJobService extends BaseTest {
    @Resource
    private ITradeJobService tradeJobService;
    @Resource
    private ITradeService tradeService;

    @Test
    public void testTradeGoods() throws Exception{
        tradeJobService.updateGoods();
    }

    @Test
    public void testParseHistory() throws Exception{

//        for (int i = 20151201; i<= 20151231; i++){
//            tradeJobService.parseNJSHistory("wt", String.valueOf(i));
//            tradeJobService.parseNJSHistory("cj", String.valueOf(i));
//            tradeJobService.parseNJSHistory("cr", String.valueOf(i));
//        }
//
//        for (int i = 20160101; i<= 20160106; i++){
//            tradeJobService.parseNJSHistory("wt", String.valueOf(i));
//            tradeJobService.parseNJSHistory("cj", String.valueOf(i));
//            tradeJobService.parseNJSHistory("cr", String.valueOf(i));
//        }

        //南交所
        //tradeJobService.parseNJSHistory("wt", "20151222");
        //tradeJobService.parseNJSHistory("cj", "20151222");
        //tradeJobService.parseNJSHistory("cr", "20151222");

        //上金所
        //tradeJobService.parseSJSHistory("wt");
        //tradeJobService.parseSJSHistory("cj");
        //tradeJobService.parseSJSHistory("cr");
    }

    @Test
    public void testQueryHistory() throws Exception{
        FQueryHistoryTransferReq req = new FQueryHistoryTransferReq();
        req.setExchange("NJS");
        req.setUserId(808330528292865L);
        req.setTraderId("16322888");
        req.setStartDate("20151101");
        req.setEndDate("20151225");
        tradeService.queryHistoryTransferList(req);
    }

    @Test
    public void testRisk() throws Exception{
        tradeJobService.sendRiskUser(1);






    }
}

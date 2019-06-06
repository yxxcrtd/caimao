package demo;

import com.caimao.gjs.api.entity.req.FQueryGoodsListReq;
import com.caimao.gjs.server.dao.GJSProductDao;
import com.caimao.gjs.server.service.TradeJobServiceImpl;
import com.caimao.gjs.server.service.TradeServiceHelper;
import com.caimao.gjs.server.service.TradeServiceImpl;
import com.caimao.gjs.server.utils.RedisUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parent.BaseTest;

import javax.annotation.Resource;

public class TestDemo extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestDemo.class);

    @Resource
    private TradeServiceImpl tradeService;
    @Resource
    private TradeJobServiceImpl tradeJobService;
    @Resource
    private TradeServiceHelper tradeServiceHelper;
    @Resource
    private GJSProductDao GJSProductDao;
    @Resource
    RedisUtils redisUtils;

    @Test
      public void testGoodList() throws Exception{
        FQueryGoodsListReq req = new FQueryGoodsListReq();
        req.setExchange("NJS");
        req.setDataType(0);
        System.out.println(tradeService.queryGoodsList(req));
    }

    @Test
    public void testUserList() throws Exception{
        tradeJobService.updateTraderId();
    }

    @Test
    public void test() throws Exception {
        logger.info("这个是测试的logger");
        tradeJobService.updateGoods();
    }

    @Test
    public void testRedis() throws Exception{
        redisUtils.set(0, "xxxxx", "11111", 10);
        Thread.sleep(11000);
        System.out.println(redisUtils.get(0, "xxxxx"));




    }
}
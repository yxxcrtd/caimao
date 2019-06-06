package ybk;

import com.caimao.bana.api.entity.req.ybk.FYBKQueryArticleIdReq;
import com.caimao.bana.api.entity.req.ybk.FYBKQueryArticleSimpleListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkExchangeQueryListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkMarketReq;
import com.caimao.bana.api.entity.req.ybk.FYbkQueryCollectionRankingReq;
import com.caimao.bana.api.entity.res.ybk.FYBKArticleSimpleRes;
import com.caimao.bana.api.entity.res.ybk.FYBKCollectionRankingRes;
import com.caimao.bana.api.entity.res.ybk.FYBKCompositeIndexRes;
import com.caimao.bana.api.entity.res.ybk.FYBKTimeLineRes;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import com.caimao.bana.api.enums.EYbkExchangeStatus;
import com.caimao.bana.api.service.ybk.IYBKJobService;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.bana.server.service.ybk.YBKJobServiceImpl;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 邮币卡测试
 * Created by Administrator on 2015/9/7.
 */
public class YbkServiceTest extends BaseTest {

    @Resource
    private IYBKService ybkService;

    @Resource
    private IYBKJobService ybkJobService;

    /**
     * 前台API用到的，返回简单的文章列表页面
     * @throws Exception
     */
    @Test
    public void queryArticleSimpleList() throws Exception {
        FYBKQueryArticleSimpleListReq req = new FYBKQueryArticleSimpleListReq();

        req = this.ybkService.queryArticleSimpleList(req);
        for (FYBKArticleSimpleRes res : req.getItems()) {
            System.out.println(ToStringBuilder.reflectionToString(res));
        }
    }

    @Test
    public void queryExchangeListTest() throws Exception {
        FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
        req.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> list = this.ybkService.queryExchangeList(req);
        for (YbkExchangeEntity entity : list) {
            //System.out.println(ToStringBuilder.reflectionToString(entity));
            System.out.println(entity.getName() + "  " + entity.getTradeDayType() + "  " + entity.getOpenOrClose());
        }
    }

    @Test
    public void getExchangeByIdTest() throws Exception {
        YbkExchangeEntity entity = this.ybkService.getExchangeById(16);
        System.out.println(ToStringBuilder.reflectionToString(entity));
    }

    @Test
    public void queryExchangeByBankNo() throws Exception {
        List<YbkExchangeEntity> list = this.ybkService.queryExchangeByBankNo("003");
        if (list != null) {
            for (YbkExchangeEntity entity : list) {
                System.out.println(ToStringBuilder.reflectionToString(entity));
            }
        }

    }

    @Test
    public void testHq() throws Exception {
        String exchangeName = "nfwjs";
        String code = "000001";

        FYbkMarketReq req = new FYbkMarketReq();
        req.setCode("100001");
        req.setExchangeShortName("jswjs");
        req.setLimit(300);
        System.out.println(ToStringBuilder.reflectionToString(ybkService.queryTimeLine(req)));
        //System.out.println(ybkService.queryKLine(exchangeName, code));
        //System.out.println(ybkService.queryMACD(exchangeName, code));
        //System.out.println(ybkService.queryKDJ(exchangeName, code));
        //System.out.println(ybkService.queryRSI(exchangeName, code));
//        FYbkQueryCollectionRankingReq req = new FYbkQueryCollectionRankingReq();
//        req.setExchangeShortName(exchangeName);
//        ybkService.queryCollectionRanking(req);
    }


    @Test
    public void quotationDataTest() throws Exception{
//        ybkJobService.quotationData();
//        ybkJobService.articleData();
        ((YBKJobServiceImpl)ybkJobService).repairKline();
    }

    @Test
    public void goodsSearchTest()throws Exception{
        ybkService.searchGoods("77");
    }

    @Test
    public void queryKLineTest() throws Exception {
        FYbkMarketReq req = new FYbkMarketReq();
        req.setExchangeShortName("nfwjs");
        req.setCode("101001");
        req.setLimit(100);
        ybkService.queryKLine(req);
    }

    /**
     * 查询综合指数
     */
    @Test
    public void queryCompositeIndexTest() throws Exception {
        List<FYBKCompositeIndexRes> list = this.ybkService.queryCompositeIndex();
        for (FYBKCompositeIndexRes res : list) {
            System.out.println(ToStringBuilder.reflectionToString(res));
        }
    }

    /**
     * 查询指定交易所下的藏品商品行情信息列表
     * @throws Exception
     */
    @Test
    public void queryCollectionRankingTest() throws Exception {
        FYbkQueryCollectionRankingReq req = new FYbkQueryCollectionRankingReq();
        req.setOrderColumn("openPrice");
        req.setOrderDir("DESC");
        req.setExchangeShortName("njwjs");
        req = this.ybkService.queryCollectionRanking(req);

        if (req.getItems().size() > 0) {
            for (FYBKCollectionRankingRes res : req.getItems()) {
                System.out.println(ToStringBuilder.reflectionToString(res));
            }
        }
    }

    @Test
    public void getCollectionInfoTest() throws Exception {
        FYBKCollectionRankingRes res = this.ybkService.getCollectionInfo("nfwjs", "101001");
        System.out.println(ToStringBuilder.reflectionToString(res));
    }

    @Test
    public void queryArticleIdTest() throws Exception {
        FYBKQueryArticleIdReq req = new FYBKQueryArticleIdReq();
        req.setCategoryId(3);
        req.setIsShow(0);
        req.setOrder(1);
        req.setThan(1);
//        req.setCreated("2015-09-16 20:05:55");
        System.out.println("上一篇id=" + this.ybkService.queryArticleId(req));
        req.setOrder(-1);
        req.setThan(-1);
        System.out.println("下一篇id="+this.ybkService.queryArticleId(req));
    }
}

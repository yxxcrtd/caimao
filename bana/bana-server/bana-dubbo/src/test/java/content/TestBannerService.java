package content;

import com.caimao.bana.api.entity.content.BanaBannerEntity;
import com.caimao.bana.api.entity.req.content.FQueryBannerListReq;
import com.caimao.bana.api.service.content.IBannerService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.Date;

/**
 * banner服务 单元测试
 * Created by Administrator on 2015/10/14.
 */
public class TestBannerService extends BaseTest {

    @Autowired
    private IBannerService bannerService;

    @Test
    public void selectByIdTest() throws Exception {
        BanaBannerEntity entity = this.bannerService.selectById(1);
        System.out.println(ToStringBuilder.reflectionToString(entity));
    }

    @Test
    public void delByIdTest() throws Exception {
        this.bannerService.delById(1);
    }

    @Test
    public void addBannerTest() throws Exception {
        BanaBannerEntity entity = new BanaBannerEntity();
        entity.setName("测试banner");
        entity.setAppType("gjs");
        entity.setSort(1);
        entity.setIsShow(1);
        entity.setAppJumpUrl("http://app");
        entity.setAppPic("/app/pic");
        entity.setPcJumpUrl("http://pc");
        entity.setPcPic("/pc/pic");
        entity.setCreateTime(new Date());
        this.bannerService.addBanner(entity);
    }

    @Test
    public void updatgeBannerTest() throws Exception {
        BanaBannerEntity entity = new BanaBannerEntity();
        entity.setId(2);
        entity.setName("测试banner");
        entity.setAppType("gjs");
        entity.setSort(2);
        entity.setIsShow(3);
        entity.setAppJumpUrl("http://app");
        entity.setAppPic("/app/pic");
        entity.setPcJumpUrl("http://pc");
        entity.setPcPic("/pc/pic");
        entity.setCreateTime(new Date());
        this.bannerService.updatgeBanner(entity);
    }

    @Test
    public void queryBannerListTest() throws Exception {
        FQueryBannerListReq req = new FQueryBannerListReq();
        req.setAppType("gjs");
        req.setIsShow(3);
        req = this.bannerService.queryBannerList(req);
        System.out.println("返回条数：" + req.getTotalCount());
        for (BanaBannerEntity entity: req.getItems()) {
            System.out.println(ToStringBuilder.reflectionToString(entity));
        }
    }

}

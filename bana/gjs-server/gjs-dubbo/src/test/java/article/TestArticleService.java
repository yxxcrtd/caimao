package article;

import com.caimao.gjs.api.entity.article.GjsArticleEntity;
import com.caimao.gjs.api.entity.article.GjsArticleJin10Entity;
import com.caimao.gjs.api.entity.req.FQueryArticleReq;
import com.caimao.gjs.api.entity.req.FQueryGjsArticleJin10Req;
import com.caimao.gjs.api.service.IArticleJobService;
import com.caimao.gjs.api.service.IArticleService;
import com.caimao.gjs.api.utils.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * 贵金属文章单元测试
 * Created by Administrator on 2015/10/12.
 */
public class TestArticleService extends BaseTest {

    @Resource
    private IArticleService articleService;

    @Resource
    private IArticleJobService articleJobService;

    /**
     * 添加文章的接口
     */
    @Test
    public void addArticleInfoTest() throws Exception {
        GjsArticleEntity entity = new GjsArticleEntity();
        entity.setCategoryId(1);
        entity.setContent("1234567");
        entity.setCreated("2012-12-12 12:12:12");
        entity.setIsHot(1);
        entity.setIsShow(0);
        entity.setSort(0);
        entity.setSourceName("快讯通");
        entity.setSourceUrl("http://kxt.com");
        entity.setTitle("123123123123");
        entity.setSummary("这是摘要");
        entity.setViewCount(0);
        this.articleService.addArticleInfo(entity);
    }


    /**
     * 删除文章的接口
     */
    @Test
    public void delArticleTest() throws Exception {
        Long id = 6L;
        this.articleService.delArticle(id);
    }

    /**
     * 更新文章的接口
     */
    @Test
    public void updateArticleInfoTest() throws Exception {
        GjsArticleEntity entity = new GjsArticleEntity();
        entity.setId(7L);
        entity.setTitle("aaaa");
        this.articleService.updateArticleInfo(entity);
    }

    /**
     * 查询指定文章内容
     */
    @Test
    public void getArticleByIdTest() throws Exception {
        GjsArticleEntity entity = this.articleService.getArticleById(7L);
        System.out.println(ToStringBuilder.reflectionToString(entity));
    }

    /**
     * 查询文章列表
     */
    @Test
    public void queryArticleListTest() throws Exception {
        FQueryArticleReq req = new FQueryArticleReq();
        req.setCategoryId(2);
        req.setIsShow(0);
//        req.setIsHot(0);
        req = this.articleService.queryArticleList(req);
        System.out.println("获取的查询文章列表条数：" + req.getItems().size());
//        if (req.getItems() != null) {
//            for (GjsArticleEntity entity : req.getItems()) {
//                System.out.println(ToStringBuilder.reflectionToString(entity));
//            }
//        }
    }

    /**
     * 抓取快讯通新闻（http://www.kxt.com/news）
     *
     * @throws Exception
     */
    @Test
    public void catchArticles() throws Exception {
//        this.articleJobService.catchArticle();
    }

    /**
     * 抓取黄金头条的资讯（http://www.goldtoutiao.com/news/list?cid=1）
     *
     * @throws Exception
     */
    @Test
    public void catchGoldTouTiaoNews() throws Exception {
        this.articleJobService.catchGoldTouTiaoNews();
    }

    /**
     * 抓取金十首页资讯（http://www.jin10.com）
     *
     * @throws Exception
     */
    @Test
    public void catchJin10Index() throws Exception {
        this.articleJobService.catchJin10();
    }

    /**
     * 查询金十数据
     *
     * @throws Exception
     */
    @Test
    public void queryGjsArticleJin10() throws Exception {
        FQueryGjsArticleJin10Req req = new FQueryGjsArticleJin10Req();
        req.setLimit(10);
        req.setStart(0);
        req = this.articleService.queryGjsArticleJin10List(req);
        if (null != req.getItems()) {
            for (GjsArticleJin10Entity entity : req.getItems()) {
                System.out.println("返回的对象：" + ToStringBuilder.reflectionToString(entity));
            }
        }
    }

    /**
     * 根据 ArticleId 查询之后的最新数据
     *
     * @throws Exception
     */
    @Test
    public void queryNewestArticleById() throws Exception {
        GjsArticleEntity entity = this.articleService.getArticleById(1177L);
        FQueryArticleReq req = new FQueryArticleReq();
        if (null != entity) {
            req.setDateStart(DateUtil.addDateSecond(entity.getCreated(), 1));
            req = this.articleService.queryArticleList(req);
        }
        System.out.println("=========根据 ArticleId 查询之后的最新数据============：" + req.getItems().size());
    }

    /**
     * 根据 ArticleId 查询之后的最新的 Jin10 数据
     *
     * @throws Exception
     */
    @Test
    public void queryNewestArticleJin10ById() throws Exception {
        GjsArticleJin10Entity entity = this.articleService.getArticleJin10ById(3881L);
        FQueryGjsArticleJin10Req req = new FQueryGjsArticleJin10Req();
        if (null != entity) {
            req.setTimeId(entity.getTimeId());
            req = this.articleService.queryGjsArticleJin10List(req);
        }
        System.out.println("=========根据 ArticleId 查询 Jin10 之后的最新数据============：" + req.getItems().size());
    }

}

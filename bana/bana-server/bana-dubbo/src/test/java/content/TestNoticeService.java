package content;

import com.caimao.bana.api.entity.content.BanaNoticeEntity;
import com.caimao.bana.api.entity.req.content.FNoticeQueryListReq;
import com.caimao.bana.server.service.content.NoticeServiceImpl;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.Date;

/**
 * 系统公告单元测试
 * Created by WangXu on 2015/6/18.
 */
public class TestNoticeService extends BaseTest {

    @Autowired
    private NoticeServiceImpl noticeService;

    @Test
    public void save() throws Exception {
        BanaNoticeEntity entity = new BanaNoticeEntity();
        entity.setId(1L);
        entity.setTitle("测试标题");
        entity.setContent("测试内容");
        entity.setSource("财猫");
        entity.setSourceHref("https://www.caimao.com");
        entity.setCreated(new Date());
        entity.setListShow(1);
        entity.setTopShow(1);
        Long id = this.noticeService.save(entity);
        System.out.println("添加系统公告成功 ID : " + id);
    }

    @Test
    public void queryList() throws Exception {
        FNoticeQueryListReq req = new FNoticeQueryListReq();
        req.setOrderColumn("created");
        req.setOrderDir("DESC");
        req = this.noticeService.queryList(req);

        System.out.println("查询公告列表，返回值:");
        System.out.println(ToStringBuilder.reflectionToString(req.getItems().get(0)));
    }

    @Test
    public void update() throws Exception {
        Long id = 1L;
        BanaNoticeEntity entity = new BanaNoticeEntity();
        entity.setId(id);
        entity.setTitle("修改后的标题");
        entity.setContent("内容不能为空");
        this.noticeService.update(entity);

        System.out.println("修改成功");
    }

    @Test
    public void queryById() throws Exception {
        Long id = 1L;
        BanaNoticeEntity entity = this.noticeService.queryById(id);

        System.out.println("查询指定公告的信息：");
        System.out.println(ToStringBuilder.reflectionToString(entity));
    }

    @Test
    public void delete() throws Exception {
        Long id = 1L;
        this.noticeService.delete(id);

        System.out.println("删除指定的公告信息 " + id);
    }

}

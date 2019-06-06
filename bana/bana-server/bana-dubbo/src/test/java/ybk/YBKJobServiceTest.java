package ybk;

import com.caimao.bana.api.service.ybk.IYBKJobService;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * 邮币卡定时任务测试
 * Created by Administrator on 2015/9/25.
 */
public class YBKJobServiceTest extends BaseTest {

    @Resource
    private IYBKJobService ybkJobService;


    /**
     * 行情数据抓取
     */
    @Test
    public void quotationDataTest() throws Exception {
        this.ybkJobService.quotationData();
    }

    @Test
    public void articleDataTest() throws Exception {
        this.ybkJobService.articleData();
    }
}

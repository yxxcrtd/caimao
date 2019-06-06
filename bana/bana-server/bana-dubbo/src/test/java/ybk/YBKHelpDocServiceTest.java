package ybk;

import com.caimao.bana.api.entity.ybk.YbkHelpDocEntity;
import com.caimao.bana.api.service.ybk.IYbkHelpDocService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.Date;

/**
 * 邮币卡帮助文档单元测试
 * Created by Administrator on 2015/11/16.
 */
public class YBKHelpDocServiceTest extends BaseTest {

    @Autowired
    private IYbkHelpDocService ybkHelpDocService;


    @Test
    public void insertTest() throws Exception {
        YbkHelpDocEntity entity = new YbkHelpDocEntity();
        entity.setTitle("test");
        entity.setContent("content");
        entity.setCategoryId(1);
        entity.setCreated(new Date());

        this.ybkHelpDocService.insert(entity);
    }

}

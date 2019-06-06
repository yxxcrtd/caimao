package operation;

import com.caimao.bana.api.service.IOperationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

/**
 * 运营相关的东东
 * Created by Administrator on 2015/8/19.
 */
public class TestOperationService extends BaseTest {

    @Autowired
    private IOperationService operationService;

    @Test
    public void testAddAlarmTask() throws Exception {
        String key = "homs_error";
        String subject = "测试报警标题";
        String content = "测试报警内容";
        this.operationService.addAlarmTask(key, subject, content);
    }
}

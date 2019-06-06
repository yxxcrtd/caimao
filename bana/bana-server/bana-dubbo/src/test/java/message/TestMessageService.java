package message;

import com.caimao.bana.api.service.content.IMessageService;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * xiaoxiceshi
 * Created by Administrator on 2015/12/29.
 */
public class TestMessageService extends BaseTest {

    @Resource
    private IMessageService messageService;

    @Test
    public void cleanAllTest() throws Exception {
        this.messageService.cleanAll(809250154610689L);
    }

    @Test
    public void delTest() throws Exception {
        this.messageService.del(809250154610689L, 810606424752129L);
    }
}

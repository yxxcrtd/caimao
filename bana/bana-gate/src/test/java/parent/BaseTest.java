package parent;


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: xiaofeng.ma
 * Date: 14-3-6
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
        "classpath:/spring/application.xml",
        "classpath:/spring/application-dubbo.xml",
        "classpath:/spring/application-redis.xml",
        "classpath:/spring/application-freemarker.xml"
})
public abstract class BaseTest {

}

package parent;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: xiaofeng.ma
 * Date: 14-3-6
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
@ContextConfiguration({
        "classpath*:META-INF/spring/application-dubbo.xml","classpath*:META-INF/spring/application.xml","classpath*:META-INF/spring/application-mybatis.xml","classpath*:META-INF/spring/application-redis.xml","classpath*:META-INF/spring/application-email.xml",
})
public abstract class BaseTest {

}

package parent;/*
*Created on 2015/5/8 16:53
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Administrator
 * @version 1.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
@ContextConfiguration({
        "classpath*:/spring/application-dubbo.xml","classpath*:/spring/application-web.xml","classpath*:/spring/applicationContext.xml"
})
public abstract class BaseTest {

}

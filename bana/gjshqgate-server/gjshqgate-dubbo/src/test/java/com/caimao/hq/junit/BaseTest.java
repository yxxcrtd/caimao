package com.caimao.hq.junit;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
@ContextConfiguration({
        "classpath*:META-INF/spring/application.xml",
        "classpath*:META-INF/spring/application-mybatis.xml",
        "classpath*:META-INF/spring/application-redis.xml",
        "classpath*:META-INF/spring/application-email.xml",
        "classpath*:META-INF/spring/application-dubbo.xml"
})



public abstract class BaseTest {

}

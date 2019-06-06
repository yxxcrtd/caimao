/*
*Charge.java
*Created on 2015/5/8 10:35
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server;

import com.caimao.bana.api.service.IHeepayRecharge;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class Charge {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/META-INF/spring/application.xml",
                "classpath:/META-INF/spring/application-mybatis.xml",
                "classpath:/META-INF/spring/application-redis.xml");
        IHeepayRecharge heepayRecharge = (IHeepayRecharge) ac.getBean("heepayRecharge");
        for (String arg : args) {
            Long orderNo = Long.parseLong(arg);
            try {
                //  充值ls
                heepayRecharge.checkHeepayPayResult(orderNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

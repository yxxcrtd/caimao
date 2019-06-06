/*
*TestGene.java
*Created on 2015/4/24 15:22
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package email;

import com.caimao.bana.api.service.IEmailService;
import com.caimao.bana.server.utils.EmailUtils;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class TestEmail extends BaseTest {
    @Resource
    private EmailUtils emailUtils;
    @Resource
    private IEmailService emailService;

    @Test
    public void testSendEmail() throws Exception{
        emailUtils.sendMail("测试发送邮件标题", "测试发送邮件内容", "ninghao@huobi.com");
    }

    @Test
    public void testSendEmailTask() throws Exception{
        emailService.sendEmailTask();
    }
}

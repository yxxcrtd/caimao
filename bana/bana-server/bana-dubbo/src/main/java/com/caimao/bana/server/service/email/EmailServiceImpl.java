package com.caimao.bana.server.service.email;

import com.caimao.bana.api.entity.EmailSendEntity;
import com.caimao.bana.api.service.IEmailService;
import com.caimao.bana.server.dao.email.BanaEmailSendDao;
import com.caimao.bana.server.utils.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("emailService")
public class EmailServiceImpl implements IEmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private EmailUtils emailUtils;
    @Resource
    private BanaEmailSendDao emailSendDao;

    // 发送邮件
    @Override
    public void doSendEmail(String subject, String content, String to) throws Exception {
        try{
            emailUtils.sendMail(subject, content, to);
        }catch(Exception e){
            throw new Exception("发送邮件失败");
        }
    }

    // 发送邮件任务
    @Override
    public void sendEmailTask() throws Exception {
        List<EmailSendEntity> emailSendList = emailSendDao.queryEmailSendList();
        if(emailSendList != null){
            for (EmailSendEntity emailSendEntity:emailSendList){
                try{
                    emailUtils.sendMail(emailSendEntity.getEmailSubject(), emailSendEntity.getEmailContent(), emailSendEntity.getEmailTo());
                    emailSendDao.sendSuccess(emailSendEntity.getId(), 1);
                    System.out.println("发送成功");
                    Thread.sleep(1000);
                }catch(Exception e){
                    logger.error("邮件发送任务失败了，失败ID：" + emailSendEntity.getId() + ",失败原因{}", e);
                }
            }
        }
    }

}

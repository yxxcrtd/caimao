package com.caimao.bana.server.service;

import com.caimao.bana.api.entity.EmailSendEntity;
import com.caimao.bana.api.entity.TpzSmsOutEntity;
import com.caimao.bana.api.entity.zeus.ZeusAlarmPeopleEntity;
import com.caimao.bana.api.enums.ESmsBizType;
import com.caimao.bana.api.enums.ESmsStatus;
import com.caimao.bana.api.service.IOperationService;
import com.caimao.bana.server.dao.email.BanaEmailSendDao;
import com.caimao.bana.server.dao.sms.TpzSmsOutDao;
import com.caimao.bana.server.dao.zeus.ZeusAlarmPeopleDao;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 网站运营相关接口服务
 * Created by Administrator on 2015/8/19.
 */
@Service("operationService")
public class OperationServiceImpl implements IOperationService {

    @Autowired
    private ZeusAlarmPeopleDao alarmPeopleDao;

    @Autowired
    private BanaEmailSendDao emailSendDao;

    @Autowired
    private TpzSmsOutDao smsOutDAO;

    // 保存报警通知联系人信息
    @Override
    public void saveAlarmPeople(ZeusAlarmPeopleEntity entity) throws Exception {
        this.alarmPeopleDao.save(entity);
    }

    // 删除报警通知联系人信息
    @Override
    public void delAlarmPeople(String key) throws Exception {
        this.alarmPeopleDao.del(key);
    }

    // 更新报警通知联系人信息
    @Override
    public void updateAlarmPeople(ZeusAlarmPeopleEntity entity) throws Exception {
        this.alarmPeopleDao.update(entity);
    }

    // 获取所有报警通知联系人列表
    @Override
    public List<ZeusAlarmPeopleEntity> listAlarmPeople() throws Exception {
        return this.alarmPeopleDao.list();
    }

    // 添加报警通知的任务
    @Override
    public void addAlarmTask(String key, String subject, String content) throws Exception {
        ZeusAlarmPeopleEntity alarmPeopleEntity = this.alarmPeopleDao.getByKey(key);
        if (alarmPeopleEntity == null) return;

        // 邮件的东东
        if (alarmPeopleEntity.getEmails() != null && !alarmPeopleEntity.getEmails().equals("")) {
            String[] emails = alarmPeopleEntity.getEmails().split(",");
            for (String e: emails) {
                try {
                    EmailSendEntity emailSendEntity = new EmailSendEntity();
                    emailSendEntity.setEmailTo(e);
                    emailSendEntity.setEmailSubject(subject);
                    emailSendEntity.setEmailContent(content);
                    emailSendEntity.setSendStatus(0);
                    emailSendEntity.setCreated(new Date());
                    this.emailSendDao.insertEmailSend(emailSendEntity);
                } catch (Exception exc) {
                    //throw exc;
                }
            }
        }
        // 短信的东东
        if (alarmPeopleEntity.getSmss() != null && !alarmPeopleEntity.getSmss().equals("")) {
            String[] smss = alarmPeopleEntity.getSmss().split(",");
            for (String s: smss) {
                try {
                    TpzSmsOutEntity smsOut = new TpzSmsOutEntity();
                    smsOut.setMobile(s);
                    smsOut.setSmsContent(subject);
                    smsOut.setCreateDatetime(new Date());
                    smsOut.setSmsStatus(ESmsStatus.SEND.getCode());
                    smsOut.setSmsStatus(ESmsBizType.ALARM.getCode());
                    this.smsOutDAO.save(smsOut);
                } catch (Exception e) {
                    //throw e;
                }
            }
        }

    }
}

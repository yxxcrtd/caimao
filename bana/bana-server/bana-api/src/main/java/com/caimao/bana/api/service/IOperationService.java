package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.zeus.ZeusAlarmPeopleEntity;

import java.util.List;

/**
 * 网站运营相关的服务接口
 * Created by Administrator on 2015/8/19.
 */
public interface IOperationService {

    /**
     * 保存报警通知联系人信息
     * @param entity
     * @throws Exception
     */
    public void saveAlarmPeople(ZeusAlarmPeopleEntity entity) throws Exception;

    /**
     * 删除报警通知联系人信息
     * @param key
     * @throws Exception
     */
    public void delAlarmPeople(String key) throws Exception;

    /**
     * 更新报警通知联系人信息
     * @param entity
     * @throws Exception
     */
    public void updateAlarmPeople(ZeusAlarmPeopleEntity entity) throws Exception;

    /**
     * 获取所有报警通知联系人列表
     * @return
     * @throws Exception
     */
    public List<ZeusAlarmPeopleEntity> listAlarmPeople() throws Exception;

    /**
     * 添加报警通知的任务
     * @param key
     * @param subject
     * @param content
     * @throws Exception
     */
    public void addAlarmTask(String key, String subject, String content) throws Exception;
}

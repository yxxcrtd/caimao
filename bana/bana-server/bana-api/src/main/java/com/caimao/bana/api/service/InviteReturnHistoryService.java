/*
*InviteReturnHistoryService.java
*Created on 2015/5/25 9:11
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.InviteReturnHistoryEntity;
import com.caimao.bana.api.entity.req.FInviteReturnPageReq;

import java.util.List;

/**
 * 返佣历史记录服务
 */
public interface InviteReturnHistoryService {

    public List<InviteReturnHistoryEntity> queryInviteReturnHistoryEntityList(InviteReturnHistoryEntity inviteReturnHistoryEntity);

    /**
     * 保存返佣历史记录
     *
     * @param inviteReturnHistoryEntity 返佣历史实体类
     * @return
     * @throws Exception
     */
    public int saveInviteReturnHistoryEntity(InviteReturnHistoryEntity inviteReturnHistoryEntity) throws Exception;

    /**
     * 根据用户userId获取返佣历史记录
     * @param returnPageReq
     * @return
     * @throws Exception
     */
    public FInviteReturnPageReq getInviteReturnHistoryListByUserId(FInviteReturnPageReq returnPageReq) throws Exception;

    /**
     * 根据邀请人和获奖类型获取记录
     *
     * @param inviteUserId 邀请人userId
     * @param returnType   获奖类型
     * @return
     * @throws Exception
     */
    public List<InviteReturnHistoryEntity> getInviteReturnHistoryByInviteUserIdAndReturnType(Long inviteUserId, Integer returnType) throws Exception;

    /**
     * 根据订单获取返佣历史记录
     *
     * @param billOrderNo
     * @return
     * @throws Exception
     */
    public List<InviteReturnHistoryEntity> getInviteReturnHistoryByBillOrderNoAndReturnType(Long billOrderNo, Integer returnType) throws Exception;

    /**
     * 根据类型获取列表
     * @param type
     * @return
     * @throws Exception
     */
    public List<InviteReturnHistoryEntity> getInviteReturnHistoryByType(Long type) throws Exception;
}

/*
*InviteReturnHistoryServiceImpl.java
*Created on 2015/5/25 9:34
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server.service.inviteReturn;

import com.caimao.bana.api.entity.InviteReturnHistoryEntity;
import com.caimao.bana.api.entity.req.FInviteReturnPageReq;
import com.caimao.bana.api.service.InviteReturnHistoryService;
import com.caimao.bana.server.dao.inviteReturn.InviteReturnHistoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0.1
 */
@Service("inviteReturnHistoryService")
public class InviteReturnHistoryServiceImpl implements InviteReturnHistoryService {

    @Resource
    private InviteReturnHistoryDao inviteReturnHistoryDao;

    @Override
    public List<InviteReturnHistoryEntity> queryInviteReturnHistoryEntityList(InviteReturnHistoryEntity inviteReturnHistoryEntity) {
        return null;
    }

    @Override
    public int saveInviteReturnHistoryEntity(InviteReturnHistoryEntity inviteReturnHistoryEntity) throws Exception {
        return inviteReturnHistoryDao.save(inviteReturnHistoryEntity);
    }
    
    @Override
    public FInviteReturnPageReq getInviteReturnHistoryListByUserId(FInviteReturnPageReq returnPageReq) throws Exception{
        returnPageReq.setItems(inviteReturnHistoryDao.getInviteReturnHistoryListByUserIdWithPage(returnPageReq));
        return returnPageReq;
    }

    @Override
    public List<InviteReturnHistoryEntity> getInviteReturnHistoryByInviteUserIdAndReturnType(Long inviteUserId, Integer returnType) throws Exception {
        return inviteReturnHistoryDao.getInviteReturnHistoryByReturnTypeAndInviteUserId(inviteUserId, returnType);
    }

    @Override
    public List<InviteReturnHistoryEntity> getInviteReturnHistoryByBillOrderNoAndReturnType(Long billOrderNo, Integer returnType) throws Exception {
        return inviteReturnHistoryDao.getInviteReturnHistoryByBillOrderNoAndReturnType(billOrderNo, returnType);
    }

    @Override
    public List<InviteReturnHistoryEntity> getInviteReturnHistoryByType(Long type) throws Exception {
        return inviteReturnHistoryDao.getInviteReturnHistoryByType(type);
    }
}

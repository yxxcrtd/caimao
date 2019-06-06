/*
*InviteReturnHistoryDao.java
*Created on 2015/5/25 16:20
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server.dao.inviteReturn;

import com.caimao.bana.api.entity.InviteReturnHistoryEntity;
import com.caimao.bana.api.entity.req.FInviteReturnPageReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0.1
 */
@Repository
public class InviteReturnHistoryDao extends SqlSessionDaoSupport {

    public int save(InviteReturnHistoryEntity inviteReturnHistoryEntity) {
        return getSqlSession().insert("InviteReturnHistory.save", inviteReturnHistoryEntity);
    }

    public List<InviteReturnHistoryEntity> getInviteReturnHistoryListByUserIdWithPage(FInviteReturnPageReq returnPageReq) {
        return getSqlSession().selectList("InviteReturnHistory.getInviteReturnHistoryListByUserIdWithPage", returnPageReq);
    }

    public List<InviteReturnHistoryEntity> getInviteReturnHistoryByReturnTypeAndInviteUserId(Long inviteUserId, Integer returnType) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("inviteUserId", inviteUserId);
        paramMap.put("returnType", returnType);
        return getSqlSession().selectList("InviteReturnHistory.getInviteReturnHistoryByReturnTypeAndInviteUserId", paramMap);
    }

    public List<InviteReturnHistoryEntity> getInviteReturnHistoryByBillOrderNoAndReturnType(Long orderNo, Integer returnType) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderNo", orderNo);
        paramMap.put("returnType", returnType);
        return getSqlSession().selectList("InviteReturnHistory.getInviteReturnHistoryByBillOrderNoAndReturnType", paramMap);
    }

    public List<InviteReturnHistoryEntity> getInviteReturnHistoryByType(Long type) throws Exception {
        return getSqlSession().selectList("InviteReturnHistory.getInviteReturnHistoryByType", type);
    }
}

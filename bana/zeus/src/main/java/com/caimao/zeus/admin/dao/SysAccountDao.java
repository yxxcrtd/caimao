/*
*AdminDao.java
*Created on 2015/5/8 16:51
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.zeus.admin.dao;

import com.caimao.zeus.admin.entity.SysAccount;
import com.caimao.zeus.admin.entity.SysAccountJour;
import com.caimao.zeus.admin.entity.req.ChangeBalanceJourReq;
import com.caimao.zeus.admin.entity.res.ChangeBalanceJourRes;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统账户列表
 */
@Repository
public class SysAccountDao extends SqlSessionDaoSupport {
    public List<SysAccount> getSysAccountList() throws Exception{
        return getSqlSession().selectList("SysAccount.getSysAccountList");
    }

    public SysAccount getSysAccountById(Long sysAccountId) throws Exception{
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", sysAccountId);
        return getSqlSession().selectOne("SysAccount.getSysAccountById", paramMap);
    }

    public SysAccount getSysAccountByIdForUpdate(Long sysAccountId) throws Exception{
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", sysAccountId);
        return getSqlSession().selectOne("SysAccount.getSysAccountByIdForUpdate", paramMap);
    }

    public Integer updateSysAccount(Long id, Map<String, Object> updateInfo) throws Exception{
        Map<String, Object> paramMap = new HashMap<>(updateInfo);
        paramMap.put("id", id);
        return getSqlSession().update("SysAccount.updateSysAccount", paramMap);
    }

    public Integer updateSysAccountBalance(Long id, Long amount) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("availableAmount", amount);
        return getSqlSession().update("SysAccount.updateSysAccountBalance", paramMap);
    }

    public Integer insertSysAccount(SysAccount sysAccount) throws Exception{
        return getSqlSession().insert("SysAccount.insertSysAccount", sysAccount);
    }

    public Integer insertSysAccountJour(SysAccountJour sysAccountJour) throws Exception{
        return getSqlSession().insert("SysAccount.insertSysAccountJour", sysAccountJour);
    }

    public List<ChangeBalanceJourRes> queryChangeBalanceJourResWithPage(ChangeBalanceJourReq req) throws Exception{
        return getSqlSession().selectList("SysAccount.queryChangeBalanceJourResWithPage", req);
    }
}

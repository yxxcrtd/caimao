/*
*AccountServiceImpl.java
*Created on 2015/5/8 17:40
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.zeus.admin.service.impl;

import com.caimao.zeus.admin.dao.SysAccountDao;
import com.caimao.zeus.admin.entity.SysAccount;
import com.caimao.zeus.admin.entity.SysAccountJour;
import com.caimao.zeus.admin.entity.req.ChangeBalanceJourReq;
import com.caimao.zeus.admin.entity.res.ChangeBalanceJourRes;
import com.caimao.zeus.admin.enums.TransType;
import com.caimao.zeus.admin.service.SysAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员实现类
 */
@Service("sysAccountService")
public class SysAccountServiceImpl implements SysAccountService {
    @Resource
    private SysAccountDao sysAccountDao;

    @Override
    public List<SysAccount> getSysAccountList() throws Exception {
        return sysAccountDao.getSysAccountList();
    }

    @Override
    public SysAccount getSysAccountById(Long sysAccountId) throws Exception {
        return sysAccountDao.getSysAccountById(sysAccountId);
    }

    @Override
    public Integer saveSysAccount(String aliasName, String memo, Long sysAccountId) throws Exception {
        if(sysAccountId !=null && sysAccountId > 0){
            Map<String, Object> updateInfo = new HashMap<>();
            updateInfo.put("aliasName", aliasName);
            updateInfo.put("memo", memo);
            return sysAccountDao.updateSysAccount(sysAccountId, updateInfo);
        }else{
            SysAccount sysAccount = new SysAccount();
            sysAccount.setAliasName(aliasName);
            sysAccount.setMemo(memo);
            return sysAccountDao.insertSysAccount(sysAccount);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void doChangeBalance(Long id, Long amount, TransType transType, Long extId, String remark) throws Exception {
        //处理数量
        if(amount == 0) throw new Exception("数量不能为0");
        amount = amount * 100;

        //锁定资产
        SysAccount sysAccount = sysAccountDao.getSysAccountByIdForUpdate(id);

        //计算资产
        Long postAmount = sysAccount.getAvailableAmount() + amount;
        if(postAmount < 0) throw new Exception("余额不足");

        //变更资产
        Integer affectRow = sysAccountDao.updateSysAccountBalance(id, amount);
        if(affectRow != 1) throw new Exception("变更记录失败");

        //插入变动记录
        SysAccountJour sysAccountJour = new SysAccountJour();
        sysAccountJour.setSysAccountId(id);
        sysAccountJour.setExtId(extId);
        sysAccountJour.setTransType(transType.getCode());
        sysAccountJour.setTransAmount(amount);
        sysAccountJour.setPreAmount(sysAccount.getAvailableAmount());
        sysAccountJour.setPostAmount(postAmount);
        sysAccountJour.setRemark(remark);
        sysAccountDao.insertSysAccountJour(sysAccountJour);

        if(sysAccountJour.getId() == null || sysAccountJour.getId() == 0){
            throw new Exception("变更记录失败02");
        }
    }

    @Override
    public ChangeBalanceJourReq queryChangeBalanceJourResWithPage(ChangeBalanceJourReq req) throws Exception {
        List<ChangeBalanceJourRes> list = sysAccountDao.queryChangeBalanceJourResWithPage(req);
        req.setItems(list);
        return req;
    }
}
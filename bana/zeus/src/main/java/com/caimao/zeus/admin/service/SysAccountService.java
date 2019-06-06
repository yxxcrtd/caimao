/*
*IAccountService.java
*Created on 2015/5/8 17:38
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.zeus.admin.service;

import com.caimao.zeus.admin.entity.SysAccount;
import com.caimao.zeus.admin.entity.req.ChangeBalanceJourReq;
import com.caimao.zeus.admin.enums.TransType;

import java.util.List;

/**
 * 管理员服务
 */
public interface SysAccountService {
    public List<SysAccount> getSysAccountList() throws Exception;

    public SysAccount getSysAccountById(Long sysAccountId) throws Exception;

    public Integer saveSysAccount(String aliasName, String memo, Long sysAccountId) throws Exception;

    public void doChangeBalance(Long id, Long amount, TransType transType, Long extId, String remark) throws Exception;

    public ChangeBalanceJourReq queryChangeBalanceJourResWithPage(ChangeBalanceJourReq req) throws Exception;
}

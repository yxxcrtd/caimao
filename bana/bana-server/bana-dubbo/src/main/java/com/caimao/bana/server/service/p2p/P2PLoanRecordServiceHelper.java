/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.service.p2p;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.enums.EP2PConfigKey;
import com.caimao.bana.api.service.IP2PService;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.p2p.P2PInvestRecordDao;
import com.caimao.bana.server.dao.p2p.P2PLoanRecordDao;
import com.caimao.bana.server.dao.sysParameter.SysParameterDao;
import com.caimao.bana.server.utils.AccountManager;

/**
 * 提交融资申请，并回写contract_no至p2p_loan_record表
 * @author yanjg
 * 2015年6月8日
 */
@Component("p2pLoanRecordServiceHelper")
public class P2PLoanRecordServiceHelper {
    @Autowired
    private P2PLoanRecordDao p2pLoanRecordDao;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private TpzAccountDao accountDao;
    @Autowired
    private P2PInvestRecordDao p2pInvestRecordDao;
    @Autowired
    private SysParameterDao sysParameterDao;
    @Autowired
    private IP2PService p2pService;

    /**
     * 获取满标的借贷列表
     * @param i
     * @param listSize
     * @return
     */
    public List<P2PLoanRecordEntity> getLoanRecordList(int start,int listSize,int targetStatus) {
        Map<String,Object> paraMap=new HashMap<String,Object>();
        paraMap.put("start", start);
        paraMap.put("length", listSize);
        paraMap.put("targetStatus", targetStatus);
        return p2pLoanRecordDao.getLoanRecordList(paraMap);
    }

    /**
     * 筛选出过期的，为满标的标的
     * @return
     */
    public List<P2PLoanRecordEntity> getFailedLoanRecordList() {
        //查询p2p全局配置
        Map<String,Object> config = p2pService.getP2PGlobalConfig();
        //验证要购买金额是否允许
        String liftDays = (String) config.get(EP2PConfigKey.借贷有效周期天数.getKey());
        if(StringUtils.isBlank(liftDays)){
            liftDays = "5";
        }
        return p2pLoanRecordDao.getFailedLoanRecordList(liftDays);
    }

    /**
     * 获取已经满标，并审核通过
     * @return
     */
    public List<P2PLoanRecordEntity> getFullCheckedLoanRecordList() {
        return p2pLoanRecordDao.getFullCheckedLoanRecordList();
    }

    /**
     * 根据合约号码查询对应的P2P借贷标的
     * @param contractNo
     * @return
     * @throws Exception
     */
    public P2PLoanRecordEntity getP2PLoanByContractNo(Long contractNo) throws Exception {
        return this.p2pLoanRecordDao.getByContractNo(contractNo);
    }
}

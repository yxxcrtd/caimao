package com.caimao.bana.server.service.btcPledge;

import com.caimao.bana.api.entity.BanaBtcPledgeRecordEntity;
import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IBtcPledgeService;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.btcPledge.BtcPledgeRecordDao;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.utils.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 比特币抵押接口
 */
@Service("btcPledgeService")
public class BtcPledgeServiceImpl implements IBtcPledgeService {
    @Autowired
    private TpzUserDao tpzUserDao;
    @Autowired
    private TpzAccountDao tpzAccountDao;
    @Autowired
    private BtcPledgeRecordDao btcPledgeRecordDao;
    @Autowired
    private AccountManager accountManager;

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Long doSavePledgeOrder(Long userId, Long orderAmount, String orderAbstract, String seqFlag, String operUser) throws Exception {
        System.out.println(operUser);
        if(operUser == null || (!operUser.equals("bitvc") && !operUser.equals("huobi"))){
            throw new CustomerException("操作员错误", 888888);
        }
        // 锁定用户资金账户
        TpzAccountEntity tpzAccountEntity = this.tpzAccountDao.getAccountByUserId(userId);
        if (tpzAccountEntity == null) {
            throw new CustomerException("该用户还没有资金账号", 888888);
        }
        // 锁定用户账户
        TpzUserEntity tpzUserEntity = this.tpzUserDao.getUserById(userId);
        if (tpzUserEntity == null) {
            throw new CustomerException("该用户不存在", 888888);
        }
        if (seqFlag.equals(ESeqFlag.GO.getCode())) {
            if (tpzAccountEntity.getAvalaibleAmount() < orderAmount) {
                throw new CustomerException("申请金额大于账户可用金额", 888888);
            }
        }
        //定义资产变更类型
        String bizType = "";
        if (seqFlag.equals(ESeqFlag.COME.getCode())) {
            if(operUser.equals("bitvc")){
                bizType = EAccountBizType.MORTGAGE_IN_BITVC.getCode();
            }else if(operUser.equals("huobi")){
                bizType = EAccountBizType.MORTGAGE_IN_HUOBI.getCode();
            }else{
                bizType = EAccountBizType.MORTGAGE_IN.getCode();
            }
        } else if (seqFlag.equals(ESeqFlag.GO.getCode())) {
            if(operUser.equals("bitvc")){
                bizType = EAccountBizType.MORTGAGE_OUT_BITVC.getCode();
            }else if(operUser.equals("huobi")){
                bizType = EAccountBizType.MORTGAGE_OUT_HUOBI.getCode();
            }else{
                bizType = EAccountBizType.MORTGAGE_OUT.getCode();
            }
        } else {
            throw new CustomerException("doSavePledgeOrder方法入参不合法", 888888);
        }

        BanaBtcPledgeRecordEntity banaBtcPledgeRecordEntity = new BanaBtcPledgeRecordEntity();
        banaBtcPledgeRecordEntity.setPzAccountId(tpzAccountEntity.getPzAccountId());
        banaBtcPledgeRecordEntity.setUserId(userId);
        banaBtcPledgeRecordEntity.setUserRealName(tpzUserEntity.getUserRealName());
        banaBtcPledgeRecordEntity.setOrderAmount(orderAmount);
        banaBtcPledgeRecordEntity.setOrderAbstract(orderAbstract);
        banaBtcPledgeRecordEntity.setOperUser(operUser);
        banaBtcPledgeRecordEntity.setCreated(new Date());
        banaBtcPledgeRecordEntity.setAccountBizType(bizType);
        banaBtcPledgeRecordEntity.setSeqFlag(seqFlag);
        btcPledgeRecordDao.save(banaBtcPledgeRecordEntity);

        //变更资产
        this.accountManager.doUpdateAvaiAmount(
                banaBtcPledgeRecordEntity.getId().toString(),
                banaBtcPledgeRecordEntity.getPzAccountId(),
                banaBtcPledgeRecordEntity.getOrderAmount(), bizType, seqFlag);

        return banaBtcPledgeRecordEntity.getId();
    }
}

package com.caimao.bana.server.service.adjustOrder;

import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.TpzAdjustOrderEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.api.enums.EVerifyStatus;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IAdjustOrderService;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.adjustOrderDao.TpzAdjustOrderDao;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.utils.AccountManager;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 红冲蓝补的方法接口
 * Created by WangXu on 2015/4/28.
 */
@Service
public class AdjustOrderService implements IAdjustOrderService {

    @Autowired
    private TpzUserDao tpzUserDao;
    @Autowired
    private TpzAccountDao tpzAccountDao;
    @Autowired
    private TpzAdjustOrderDao tpzAdjustOrderDao;
    @Autowired
    private MemoryDbidGenerator memoryDbidGenerator;
    @Autowired
    private AccountManager accountManager;

    /**
     * 红冲蓝补的接口方法
     * 锁定用户资金账户，在锁定用户信息（所有的资金操作，都先锁定资金账户吧）
     * @param userId    用户ID
     * @param orderAmount   变更数量（*100）
     * @param orderAbstract 变更说明
     * @param seqFlag   变更类型
     * @param operUser  操作用户
     * @param needAudit 是否需要审核
     * @throws CustomerException
     * @throws Exception
     * @return Long  返回订单号
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Long doSaveAdjustOrder(Long userId, Long orderAmount, String orderAbstract, String seqFlag, String operUser, boolean needAudit) throws Exception {
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
        String bizType = "";
        if (seqFlag.equals(ESeqFlag.COME.getCode())) {
            bizType = EAccountBizType.OTHER.getCode();
        } else if (seqFlag.equals(ESeqFlag.GO.getCode())) {
            bizType = EAccountBizType.OTHER.getCode();
        } else {
            throw new CustomerException("doSaveAdjustOrder方法入参不合法", 888888);
        }
        Date date = new Date();
        TpzAdjustOrderEntity tpzAdjustOrderEntity = new TpzAdjustOrderEntity();
        tpzAdjustOrderEntity.setOrderNo(this.memoryDbidGenerator.getNextId());
        tpzAdjustOrderEntity.setPzAccountId(tpzAccountEntity.getPzAccountId());
        tpzAdjustOrderEntity.setUserId(userId);
        tpzAdjustOrderEntity.setUserRealName(tpzUserEntity.getUserRealName());
        tpzAdjustOrderEntity.setOrderAmount(orderAmount);
        tpzAdjustOrderEntity.setOrderAbstract(orderAbstract);
        tpzAdjustOrderEntity.setOperUser(operUser);
        tpzAdjustOrderEntity.setVerifyStatus(EVerifyStatus.WAIT_APPROVAL.getCode());
        tpzAdjustOrderEntity.setCreateDatetime(date);
        tpzAdjustOrderEntity.setUpdateDatetime(date);
        tpzAdjustOrderEntity.setSeqFlag(seqFlag);
        this.tpzAdjustOrderDao.save(tpzAdjustOrderEntity);

        if ( ! needAudit) {
            // 不需要审核，直接变更资产
            tpzAdjustOrderEntity.setVerifyUser(operUser);
            tpzAdjustOrderEntity.setVerifyDatetime(date);
            tpzAdjustOrderEntity.setVerifyStatus(EVerifyStatus.PASS.getCode());
            this.tpzAdjustOrderDao.update(tpzAdjustOrderEntity);
            this.accountManager.doUpdateAvaiAmount(
                    tpzAdjustOrderEntity.getOrderNo().toString(),
                    tpzAdjustOrderEntity.getPzAccountId(),
                    tpzAdjustOrderEntity.getOrderAmount(), bizType, seqFlag);
        } else if (seqFlag.equals(ESeqFlag.GO.getCode())) {
            // 需要审核，并且是出款的，则冻结用户的资产
            this.accountManager.dofreezeAmount(
                    tpzAdjustOrderEntity.getOrderNo().toString(),
                    tpzAccountEntity.getPzAccountId(), orderAmount, ESeqFlag.COME.getCode(), bizType);
        }
        return tpzAdjustOrderEntity.getOrderNo();
    }

}

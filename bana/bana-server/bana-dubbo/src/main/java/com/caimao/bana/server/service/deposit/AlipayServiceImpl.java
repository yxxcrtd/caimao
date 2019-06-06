package com.caimao.bana.server.service.deposit;

import com.caimao.bana.api.entity.AlipayRecordEntity;
import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.TpzChargeOrderEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.EOrderStatus;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.api.service.IAlipayService;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.depositDao.AlipayRecordDao;
import com.caimao.bana.server.dao.depositDao.TpzChargeOrderDao;
import com.caimao.bana.server.utils.AccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 支付宝相关
 */
@Service("alipayService")
public class AlipayServiceImpl implements IAlipayService {
    private static final Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);

    @Resource
    private AlipayRecordDao alipayRecordDao;
    @Resource
    private TpzChargeOrderDao tpzChargeOrderDao;
    @Resource
    private TpzAccountDao tpzAccountDao;
    @Resource
    private AccountManager accountManager;

    @Override
    public void saveTradeRecord(AlipayRecordEntity alipayRecordEntity) throws Exception {
        System.out.println("进入了");
        alipayRecordDao.saveTradeRecord(alipayRecordEntity);
    }

    @Override
    public void processAlipay() throws Exception {
        //获取5天内未成功的支付宝订单
        Date dateStart = new Date(System.currentTimeMillis() - 86400 * 5 * 1000);
        List<TpzChargeOrderEntity> orderList = tpzChargeOrderDao.queryAlipayNoSuccess(dateStart);
        if(orderList != null){
            for (TpzChargeOrderEntity tpzChargeOrderEntity:orderList){
                try{
                    this.alipayToSuccess(tpzChargeOrderEntity.getOrderNo());
                }catch(Exception e){
                    logger.error("支付宝订单错误了:{}", e);
                }
            }
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void alipayToSuccess(Long orderNo) throws Exception {
        //锁定订单
        TpzChargeOrderEntity tpzChargeOrderEntity = tpzChargeOrderDao.getChargeOrderByOrderNo(orderNo);
        if(tpzChargeOrderEntity == null) throw new Exception("订单不存在");
        if(tpzChargeOrderEntity.getChannelId() != -1) throw new Exception("不是支付宝的");
        if(!tpzChargeOrderEntity.getPayType().equals("-1")) throw new Exception("不是支付宝的");
        if(!tpzChargeOrderEntity.getOrderStatus().equals(EOrderStatus.WAIT_SURE.getCode())) throw new Exception("状态不正确");
        if(tpzChargeOrderEntity.getBankCardName() == null || tpzChargeOrderEntity.getBankCardName().equals("")) throw new Exception("交易号不存在");
        // 锁定用户资产记录
        TpzAccountEntity tpzAccountEntity = this.tpzAccountDao.getAccountByUserId(tpzChargeOrderEntity.getUserId());
        if (tpzAccountEntity == null) {
            logger.error("充值的用户 {} 不存在", tpzChargeOrderEntity.getUserId());
            throw new Exception("用户不存在!");
        }

        //查找相应的支付宝交易信息
        AlipayRecordEntity alipayRecordEntity = alipayRecordDao.queryTradeRecordForUpdate(new BigDecimal(tpzChargeOrderEntity.getBankCardName()));
        if(alipayRecordEntity != null){
            if(alipayRecordEntity.getAmount().longValue() != tpzChargeOrderEntity.getOrderAmount().longValue()) throw new Exception("金额不一致");
            if(alipayRecordEntity.getRelNo() != 0) throw new Exception("关联id不为空");
            //变更交易记录
            alipayRecordDao.updateTradeRecord(alipayRecordEntity.getTradeId(), tpzChargeOrderEntity.getOrderNo());
            //变更充值记录
            TpzChargeOrderEntity updateInfo = new TpzChargeOrderEntity();
            updateInfo.setOrderNo(tpzChargeOrderEntity.getOrderNo());
            updateInfo.setRemark(alipayRecordEntity.getTradeId().toString());
            updateInfo.setOrderStatus(EOrderStatus.SUCCESS.getCode());
            updateInfo.setVerifyUser("system");
            updateInfo.setVerifyDatetime(new Date());
            updateInfo.setUpdateDatetime(new Date());
            tpzChargeOrderDao.updateAlipay(updateInfo);
            //变更资产
            logger.info("给用户 {} 加钱 {}", tpzChargeOrderEntity.getUserId(), tpzChargeOrderEntity.getOrderAmount());
            this.accountManager.doUpdateAvaiAmount(tpzChargeOrderEntity.getOrderNo().toString(), tpzChargeOrderEntity.getPzAccountId(), tpzChargeOrderEntity.getOrderAmount(), EAccountBizType.CHARGE.getCode(), ESeqFlag.COME.getCode());
        }
    }
}

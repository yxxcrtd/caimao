package com.caimao.bana.server.service.account;

import com.caimao.bana.api.entity.*;
import com.caimao.bana.api.entity.activity.BanaActivityRecordEntity;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.entity.req.account.FAccountChangeReq;
import com.caimao.bana.api.entity.res.FAccountQueryAccountFrozenJourRes;
import com.caimao.bana.api.entity.res.FAccountQueryAccountJourRes;
import com.caimao.bana.api.entity.res.FAccountQueryChargeOrderRes;
import com.caimao.bana.api.entity.res.FAccountQueryWithdrawOrderRes;
import com.caimao.bana.api.enums.*;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IAccountService;
import com.caimao.bana.api.service.IActivityService;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.accountDao.TpzAccountFreezeJourDao;
import com.caimao.bana.server.dao.accountDao.TpzAccountJourDao;
import com.caimao.bana.server.dao.depositDao.TpzChargeOrderDao;
import com.caimao.bana.server.dao.depositDao.TpzPayChannelDao;
import com.caimao.bana.server.dao.userBankCardDao.TpzUserBankCardDao;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.dao.userDao.TpzUserExtDao;
import com.caimao.bana.server.dao.withdrawDao.TpzWithdrawOrderDao;
import com.caimao.bana.server.utils.*;
import com.hsnet.pz.core.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户账户相关接口服务
 * Created by WangXu on 2015/5/26.
 */
@Service("accountService")
public class AccountServiceImpl implements IAccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private TpzUserDao userDao;
    @Resource
    private TpzUserDao tpzUserDao;
    @Resource
    private TpzUserExtDao userExtDao;
    @Autowired
    private TpzAccountDao accountDao;
    @Autowired
    private TpzWithdrawOrderDao withdrawOrderDao;
    @Autowired
    private TpzAccountJourDao accountJourDao;
    @Autowired
    private TpzAccountFreezeJourDao accountFreezeJourDao;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private TpzPayChannelDao payChannelDao;
    @Autowired
    private TpzChargeOrderDao chargeOrderDao;
    @Autowired
    private ChannelUtil channelUtil;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TpzUserBankCardDao userBankCardDao;
    @Autowired
    private MemoryDbidGenerator dbidGenerator;
    @Autowired
    private IActivityService activityService;

    // 冻结用户可用的人民币
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Long frozenAvailableAmount(FAccountChangeReq req) throws Exception {
        return this.accountManager.dofreezeAmount(req.getRefSerialNo(), req.getPzAccountId(), req.getAmount(), req.getSeqFlag(), req.getAccountBizType());
    }

    // 充值功能
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Map<String, Object> doPreCharge(FAccountPreChargeReq accountChargeReq) throws Exception {
        TpzUserEntity tpzUserEntity = this.userDao.getUserById(accountChargeReq.getUserId());
        if (tpzUserEntity == null) {
            this.logger.error("无法获取 {} 用户的信息", accountChargeReq.getUserId());
            throw new CustomerException("无法获取用户信息", 830408);
        }
        if ((tpzUserEntity.getIsTrust() != null) && (!tpzUserEntity.getIsTrust().equals(EAuthStatus.YES.getCode()))) {
            this.logger.warn("用户 {} 未进行实名认证", accountChargeReq.getUserId());
            throw new CustomerException("请先进行实名认证", 830408);
        }
        TpzPayChannelEntity payChannelEntity = this.payChannelDao.getPayById(accountChargeReq.getPayCompanyNo());

        TpzAccountEntity accountEntity = this.accountDao.getAccountByUserId(accountChargeReq.getUserId());
        Date curDate = new Date();
        // 这里要写订单表了
        TpzChargeOrderEntity tpzChargeOrderEntity = new TpzChargeOrderEntity();
        tpzChargeOrderEntity.setBankNo(accountChargeReq.getBankNo()); // 设置银行代码
        tpzChargeOrderEntity.setBankCardName(accountChargeReq.getBackCardName()); // 设置银行代码
        tpzChargeOrderEntity.setBankCode(accountChargeReq.getBankNo()); // 设置银行代码
        tpzChargeOrderEntity.setBankCardNo(""); // 设置充值的银行卡号码
        tpzChargeOrderEntity.setPzAccountId(accountEntity.getPzAccountId());    // 设置融资账户的id
        tpzChargeOrderEntity.setOrderAmount(accountChargeReq.getChargeAmount());  // 设置充值的数量
        tpzChargeOrderEntity.setOrderName(accountChargeReq.getOrderName());  // 设置订单名称
        tpzChargeOrderEntity.setOrderAbstract(accountChargeReq.getOrderAbstract());  // 设置订单描述
        tpzChargeOrderEntity.setUserId(accountChargeReq.getUserId()); // 用户ID
        tpzChargeOrderEntity.setCurrencyType(ECurrency.CNY.getCode());  // 设置货币类型
        if (accountChargeReq.getPayCompanyNo().longValue() < 0L) {
            tpzChargeOrderEntity.setOrderStatus(EOrderStatus.WAIT_SURE.getCode());
            tpzChargeOrderEntity.setCheckStatus(ECheckStatus.NO_CHECK.getCode());
        } else {
            tpzChargeOrderEntity.setOrderStatus(EOrderStatus.ING.getCode());
            tpzChargeOrderEntity.setCheckStatus(ECheckStatus.UNCHECK.getCode());
        }
        tpzChargeOrderEntity.setCreateDatetime(curDate);
        tpzChargeOrderEntity.setUpdateDatetime(curDate);
        tpzChargeOrderEntity.setChannelId(accountChargeReq.getPayCompanyNo());   // 设置渠道ID
        tpzChargeOrderEntity.setPayType(String.valueOf(accountChargeReq.getPayType()));   // 设置支付方式
        tpzChargeOrderEntity.setPaySubmitDatetime(curDate);
        tpzChargeOrderEntity.setChannelServiceCharge(0L);   // 这个是充值手续费
        this.logger.info("预充值，写入数据记录 {}", ToStringBuilder.reflectionToString(tpzChargeOrderEntity));
        this.chargeOrderDao.save(tpzChargeOrderEntity);  // 进行保存

        // 需要充值的金额 = 充值金额 + 服务费用
        tpzChargeOrderEntity.setOrderAmount(tpzChargeOrderEntity.getOrderAmount() + tpzChargeOrderEntity.getChannelServiceCharge());
        if (1 == accountChargeReq.getPayCompanyNo()) {
            // 通联支付，目前不是用
            throw new CustomerException("支付通道暂时关闭", 888888);
        } else if (2 == accountChargeReq.getPayCompanyNo()) {
            // 易宝支付，目前不用
            throw new CustomerException("支付通道暂时关闭", 888888);
        } else if (3 == accountChargeReq.getPayCompanyNo()) {
            // 汇付宝支付
            return this.channelUtil.convertHeepaySubmitEntity(tpzChargeOrderEntity, payChannelEntity, accountChargeReq);
        } else if (-1 == accountChargeReq.getPayCompanyNo()) {
            // 支付宝转账？
            Map<String, Object> alipayReturnRep = new TreeMap<>();
            alipayReturnRep.put("result", true);
            alipayReturnRep.put("orderNo", tpzChargeOrderEntity.getOrderNo());
            alipayReturnRep.put("msg", "");
            return alipayReturnRep;
        } else if (-2 == accountChargeReq.getPayCompanyNo()) {
            // 银行汇款 ？
            Map<String, Object> bankReturnRep = new TreeMap<>();
            bankReturnRep.put("result", true);
            bankReturnRep.put("orderNo", tpzChargeOrderEntity.getOrderNo());
            bankReturnRep.put("msg", "");
            return bankReturnRep;
        }
        throw new CustomerException("未定义的支付方式", 888888);
    }

    // 申请提现
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Long doApplyWithdraw(FAccountApplyWithdrawReq accountApplyWithdrawReq) throws Exception {
        Long userId = accountApplyWithdrawReq.getUserId();
        String userTradePwd = accountApplyWithdrawReq.getUserTradePwd();
        Long orderAmount = accountApplyWithdrawReq.getOrderAmount();
        String orderName = accountApplyWithdrawReq.getOrderName();
        String orderAbstract = accountApplyWithdrawReq.getOrderAbstract();

        // 查询用户信息，验证交易账户是否正确
        TpzUserEntity userEntity = this.userDao.getUserById(userId);
        if (userEntity == null) {
            throw new CustomerException("无法获取用户信息", 830405);
        }

        // 获取用户的扩展信息，看是否能够进行提现
        TpzUserExtEntity userExtEntity = this.userExtDao.getById(userId);
        if (userExtEntity.getProhiWithdraw().equals(1)) {
            throw new CustomerException("该账户不允许进行提现", 830405);
        }

        this.userManager.validateUserTradePwd(userId, userTradePwd);
        // 查詢用戶的提现银行卡
        List<TpzUserBankCardEntity> userBankCardEntityList = this.userBankCardDao.queryUserBankCardList(userId, EBankCardStatus.YES.getCode());
        if ((userBankCardEntityList == null) || (userBankCardEntityList.size() == 0)) {
            throw new CustomerException("您还没有通过银行卡验证,无法进行取现申请,请先验证银行卡或者联系客服", 830411);
        }
        // 查询他活动获取的钱有没有
        Long actAmount = 0L;
        List<BanaActivityRecordEntity> recordEntityList = this.activityService.getUserActivityRecord(userId, EActId.EWM100.getCode());
        if (recordEntityList != null) {
            for (BanaActivityRecordEntity entity: recordEntityList) {
                actAmount += entity.getAmount();
            }
        }
        TpzAccountEntity accountEntity = this.accountDao.getAccountByUserId(userId);
        if (accountEntity.getAvalaibleAmount() - accountEntity.getFreezeAmount() - actAmount < orderAmount) {
            if (actAmount > 0) {
                throw new CustomerException("参与活动赠送的资金不允许提现", 830411);
            }
            throw new CustomerException("取现金额大于账户可用余额,您可以将合约盈利的部分划转到账户中申请取现", 830411);
        }


        TpzWithdrawOrderEntity withdrawOrderEntity = new TpzWithdrawOrderEntity();
        Date date = new Date();
        // 设置银行卡信息
        withdrawOrderEntity.setBankCardName((userBankCardEntityList.get(0)).getBankCardName());
        withdrawOrderEntity.setBankCardNo((userBankCardEntityList.get(0)).getBankCardNo());
        withdrawOrderEntity.setBankCode((userBankCardEntityList.get(0)).getBankCode());
        withdrawOrderEntity.setBankNo((userBankCardEntityList.get(0)).getBankNo());
        withdrawOrderEntity.setBankAddress((userBankCardEntityList.get(0)).getBankAddress());
        withdrawOrderEntity.setProvince(userBankCardEntityList.get(0).getProvince());
        withdrawOrderEntity.setCity(userBankCardEntityList.get(0).getCity());
        withdrawOrderEntity.setOpenBank(userBankCardEntityList.get(0).getOpenBank());

        withdrawOrderEntity.setCheckStatus(ECheckStatus.UNCHECK.getCode());
        withdrawOrderEntity.setCreateDatetime(date);
        withdrawOrderEntity.setUpdateDatetime(date);
        withdrawOrderEntity.setCurrencyType(ECurrency.CNY.getCode());
        withdrawOrderEntity.setOrderAbstract(orderAbstract);
        withdrawOrderEntity.setOrderAmount(orderAmount);
        withdrawOrderEntity.setOrderName(orderName);

        withdrawOrderEntity.setOrderStatus(EOrderStatus.ING.getCode());
        withdrawOrderEntity.setPzAccountId(accountEntity.getPzAccountId());

        withdrawOrderEntity.setUserId(userId);
        withdrawOrderEntity.setOrderNo(this.dbidGenerator.getNextId());
        // 看是否需要进行审核
        if (Constants.WITHDRAW_CHECK) {
            withdrawOrderEntity.setVerifyStatus(EVerifyStatus.WAIT_APPROVAL.getCode());
        } else {
            withdrawOrderEntity.setVerifyStatus(EVerifyStatus.PASS.getCode());
            withdrawOrderEntity.setVerifyDatetime(date);
        }
        // 看是否需要进行冻结
        if (Constants.WITHDRAW_FROZEN) {
            this.accountManager.dofreezeAmount(String.valueOf(withdrawOrderEntity.getOrderNo()), accountEntity.getPzAccountId(), withdrawOrderEntity.getOrderAmount(), ESeqFlag.COME.getCode(), EAccountBizType.WITHDRAW.getCode());
        }

        this.withdrawOrderDao.save(withdrawOrderEntity);
        return withdrawOrderEntity.getOrderNo();
    }

    // 取消提现功能
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void doCancelWithdraw(FAccountCancelWithdrawReq accountCancelWithdrawReq) throws Exception {
        Long userId = accountCancelWithdrawReq.getUserId();
        Long orderId = accountCancelWithdrawReq.getOrderId();
        String remark = accountCancelWithdrawReq.getRemark();

        TpzWithdrawOrderEntity orderEntity = this.withdrawOrderDao.getWithdrawOrderByOrderNo(orderId);
        if (orderEntity == null) {
            throw new CustomerException("无此订单。", 831419);
        }
        if (!userId.equals(orderEntity.getUserId())) {
            throw new CustomerException("无此订单。", 831419);
        }
        // 和程序原来的正好相仿，之前后台不审核不让撤销，前台是审核了不让撤销
        if (EVerifyStatus.PASS.getCode().equals(orderEntity.getVerifyStatus())) {
            throw new CustomerException("该笔订单审核通过,不能进行撤销。", 831419);
        }
        if (EOrderStatus.SUCCESS.getCode().equals(orderEntity.getOrderStatus())) {
            throw new CustomerException("该笔订单已支付成功，不能撤销。", 831419);
        }
        if (EOrderStatus.WAIT_SURE.getCode().equals(orderEntity.getOrderStatus())) {
            throw new CustomerException("该笔订单等待确认,在获取最终状态前,暂不能撤销。", 831419);
        }
        if (EOrderStatus.CACAL.getCode().equals(orderEntity.getOrderStatus())) {
            throw new CustomerException("该笔订单已被撤销,不能再次撤销。", 831419);
        }
        orderEntity.setRemark(remark);
        orderEntity.setOrderStatus(EOrderStatus.CACAL.getCode());
        this.withdrawOrderDao.update(orderEntity);
        if (Constants.WITHDRAW_FROZEN)
            this.accountManager.dofreezeAmount(String.valueOf(orderEntity.getOrderNo()), orderEntity.getPzAccountId(), orderEntity.getOrderAmount(), ESeqFlag.GO.getCode(), EAccountBizType.WITHDRAW.getCode());
    }

    // 查询账户资金流水记录
    @Override
    public FAccountQueryAccountJourReq queryAccountJour(FAccountQueryAccountJourReq accountQueryAccountJourReq) throws Exception {
        if (accountQueryAccountJourReq.getOrderColumn() == null) {
            accountQueryAccountJourReq.setOrderColumn("trans_datetime");
        }
        if (accountQueryAccountJourReq.getOrderDir() == null) {
            accountQueryAccountJourReq.setOrderDir("DESC");
        }
        List<FAccountQueryAccountJourRes> list = this.accountJourDao.queryAccountJourWithPage(accountQueryAccountJourReq);
        accountQueryAccountJourReq.setItems(list);
        return accountQueryAccountJourReq;
    }

    // 获取用户资金冻结流水
    @Override
    public FAccountQueryAccountFrozenJourReq queryAccountFrozenJour(FAccountQueryAccountFrozenJourReq req) throws Exception {
        if (req.getOrderColumn() == null) {
            req.setOrderColumn("trans_datetime");
        }
        if (req.getOrderDir() == null) {
            req.setOrderDir("DESC");
        }
        List<FAccountQueryAccountFrozenJourRes> list = this.accountFreezeJourDao.queryAccountFrozenJourWithPage(req);
        req.setItems(list);
        return req;
    }

    // 查询账户提现的订单记录
    @Override
    public FAccountQueryWithdrawOrderReq queryWithdrawOrder(FAccountQueryWithdrawOrderReq accountQueryWithdrawOrderReq) throws Exception {
        if (accountQueryWithdrawOrderReq.getOrderColumn() == null) {
            accountQueryWithdrawOrderReq.setOrderColumn("create_datetime");
        }
        if (accountQueryWithdrawOrderReq.getOrderDir() == null) {
            accountQueryWithdrawOrderReq.setOrderDir("DESC");
        }
        List<TpzWithdrawOrderEntity> list = this.withdrawOrderDao.queryWithdrawOrdersWithPage(accountQueryWithdrawOrderReq);
        List<FAccountQueryWithdrawOrderRes> newList = new ArrayList<>();
        accountQueryWithdrawOrderReq.setItems(newList);
        if ((list == null) || (list.size() == 0)) {
            return accountQueryWithdrawOrderReq;
        }
        for (TpzWithdrawOrderEntity ch : list) {
            FAccountQueryWithdrawOrderRes withdrawOrderRes = new FAccountQueryWithdrawOrderRes();
            withdrawOrderRes.setBankCardName(ch.getBankCardName());
            withdrawOrderRes.setBankCardNo(ch.getBankCardNo());
            withdrawOrderRes.setBankCode(ch.getBankCode());
            withdrawOrderRes.setBankNo(ch.getBankNo());
            withdrawOrderRes.setCurrencyType(ch.getCurrencyType());
            withdrawOrderRes.setOrderAbstract(ch.getOrderAbstract());
            withdrawOrderRes.setOrderAmount(ch.getOrderAmount());
            withdrawOrderRes.setOrderNo(ch.getOrderNo());
            withdrawOrderRes.setOrderStatus(ch.getOrderStatus());
            withdrawOrderRes.setPaySubmitDatetime(DateUtil.convertDateToString(ch.getPaySubmitDatetime()));
            withdrawOrderRes.setPzAccountId(ch.getPzAccountId());
            withdrawOrderRes.setCreateDatetime(DateUtil.convertDateToString(ch.getCreateDatetime()));
            withdrawOrderRes.setVerifyDatetime(DateUtil.convertDateToString(ch.getVerifyDatetime()));
            newList.add(withdrawOrderRes);
        }
        return accountQueryWithdrawOrderReq;
    }

    // 查询充值订单历史
    @Override
    public FAccountQueryChargeOrderReq queryChargeOrder(FAccountQueryChargeOrderReq req) throws Exception {
        // 定制查询对象
        TpzChargeOrderEntity chargeOrderEntity = new TpzChargeOrderEntity();
        chargeOrderEntity.setUserId(req.getUserId());
        chargeOrderEntity.setPzAccountId(req.getPzAccountId());
        if (!req.getStartDate().isEmpty()) {
            System.out.println("==================================");
            System.out.println(req.getStartDate());
            chargeOrderEntity.setCreateDatetimeBegin(DateUtil.convertStringToDate("yyyy-MM-dd", req.getStartDate()));
        }
        if (!req.getEndDate().isEmpty()) {
            chargeOrderEntity.setCreateDatetimeEnd(DateUtil.convertStringToDate("yyyy-MM-dd", req.getEndDate()));
        }
        chargeOrderEntity.setOrderStatus(req.getOrderStatus());
        chargeOrderEntity.setChannelId(req.getChannelId());
        chargeOrderEntity.setLimit(req.getLimit());
        chargeOrderEntity.setPages(req.getPages());
        chargeOrderEntity.setStart(req.getStart());
        if (StringUtils.isBlank(req.getOrderColumn())) {
            req.setOrderColumn("create_datetime");
            req.setOrderDir("DESC");
        }
        chargeOrderEntity.setOrderColumn(req.getOrderColumn());
        chargeOrderEntity.setOrderDir(req.getOrderDir());
        // 查询订单
        List<TpzChargeOrderEntity> chargeOrderEntityList = this.chargeOrderDao.queryChargeOrderWithPage(chargeOrderEntity);

        req.setTotalCount(chargeOrderEntity.getTotalCount());
        List list = new ArrayList();
        req.setItems(list);
        if (chargeOrderEntityList == null || chargeOrderEntityList.size() == 0) {
            return req;
        }
        for (TpzChargeOrderEntity coe : chargeOrderEntityList) {
            FAccountQueryChargeOrderRes aqco = new FAccountQueryChargeOrderRes();
            aqco.setBankCardName(coe.getBankCardName());
            aqco.setBankCardNo(coe.getBankCardNo());
            aqco.setBankCode(coe.getBankCode());
            aqco.setBankNo(coe.getBankNo());
            aqco.setChannelServiceCharge(coe.getChannelServiceCharge());
            aqco.setCurrencyType(coe.getCurrencyType());
            aqco.setOrderAbstract(coe.getOrderAbstract());
            aqco.setOrderAmount(coe.getOrderAmount());
            aqco.setOrderNo(coe.getOrderNo());
            aqco.setOrderStatus(coe.getOrderStatus());
            aqco.setPaySubmitDatetime(DateUtil.convertDateToString(coe.getPaySubmitDatetime()));
            aqco.setPzAccountId(coe.getPzAccountId());
            aqco.setChannelId(coe.getChannelId());
            aqco.setCreateDatetime(DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", coe.getCreateDatetime()));
            list.add(aqco);
        }
        return req;
    }

    // 获取用户的资金账户
    @Override
    public TpzAccountEntity getAccount(Long userId) throws Exception {
        return this.accountDao.getAccountByUserId(userId);
    }

    //  查询用户资产列表
    @Override
    public FZeusUserBalanceReq queryUserBalanceList(FZeusUserBalanceReq req) throws Exception {
        if(req.getMobile() != null && !req.getMobile().equals("") && (req.getUserId() == null || req.getUserId() == 0)){
            TpzUserEntity tpzUserEntity = tpzUserDao.queryUserByPhone(req.getMobile());
            if(tpzUserEntity != null) req.setUserId(tpzUserEntity.getUserId());
        }
        req.setItems(this.accountDao.queryUserBalanceListWithPage(req));
        return req;
    }

    //冻结用户资产账户
    @Override
    public void lockingAccount(Long pzAccountId) throws Exception {
        accountDao.lockingAccount(pzAccountId);
    }
}

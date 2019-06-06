package com.caimao.bana.server.service.p2p;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.TpzLoanContractEntity;
import com.caimao.bana.api.entity.TpzProdEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.p2p.P2PConfigEntity;
import com.caimao.bana.api.entity.p2p.P2PInterestRecordEntity;
import com.caimao.bana.api.entity.p2p.P2PInvestRecordEntity;
import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.entity.res.*;
import com.caimao.bana.api.entity.res.product.FProductDetailRes;
import com.caimao.bana.api.enums.*;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IP2PService;
import com.caimao.bana.server.dao.LoanContractDao;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.accruedInterest.TpzAccruedInterestBillDao;
import com.caimao.bana.server.dao.p2p.P2PConfigDao;
import com.caimao.bana.server.dao.p2p.P2PInterestRecordDao;
import com.caimao.bana.server.dao.p2p.P2PInvestRecordDao;
import com.caimao.bana.server.dao.p2p.P2PLoanRecordDao;
import com.caimao.bana.server.dao.prod.TpzProdDao;
import com.caimao.bana.server.dao.prod.TpzProdDetailDao;
import com.caimao.bana.server.dao.sysParameter.SysParameterDao;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.utils.*;
import com.hsnet.pz.core.exception.BizServiceException;
import com.huobi.commons.utils.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资借贷的接口实现类 Created by WangXu on 2015/6/8.
 */
@Service("P2PService")
public class P2PServiceImpl implements IP2PService {

    private static final Logger logger = LoggerFactory.getLogger(P2PServiceImpl.class);

    @Resource
    private P2PInterestRecordDao P2PInterestRecordDao;
    @Resource
    private P2PInvestRecordDao P2PInvestRecordDao;
    @Resource
    private P2PLoanRecordDao P2PLoanRecordDao;
    @Resource
    private SysParameterDao sysParameterDao;
    @Resource
    private P2PConfigDao p2pConfigDao;
    @Autowired
    private TpzAccountDao accountDao;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private TpzUserDao userDao;
    @Autowired
    private TpzProdDao prodDao;
    @Autowired
    private TpzProdDetailDao prodDetailDao;
    @Autowired
    private UserManager userManager;
    @Resource
    private TpzAccruedInterestBillDao tpzAccruedInterestBillDao;
    @Resource
    private LoanContractDao loanContractDao;

    @Override
    public List<FP2PParameterRes> getP2PParameter() {
        return sysParameterDao.getP2PParameter();
    }
    
    @Override
    public Map<String,Object> getP2PGlobalConfig() {
        Map<String,Object> map = new HashMap<String,Object>();
        List<FP2PParameterRes> parameterResList = getP2PParameter();
        for(FP2PParameterRes parameterRes: parameterResList) {
            map.put(parameterRes.getParamCode(), parameterRes.getParamValue());
        }
        return map;
    }

    @Override
    public P2PConfigEntity getProdSetting(Long prodId, Integer prodLever) {
        return p2pConfigDao.getProdSetting(prodId, prodLever);
    }

    @Override
    public List<P2PConfigEntity> getProdSettingByProdId(Long prodId) {
        return p2pConfigDao.getProdSetting(prodId);
    }

    @Override
    public void saveProdSetting(P2PConfigEntity config) {
        if (config == null) {
            return;
        }
        p2pConfigDao.save(config);

    }

    @Override
    public void updateProdSetting(P2PConfigEntity config) {
        if (config == null) {
            return;
        }
        p2pConfigDao.update(config);

    }

    // 检查这个这个产品是否是P2P借贷的产品
    @Override
    public void checkIsP2PLoan(Long productId, Integer lever) throws Exception {
        P2PConfigEntity configEntity = this.getProdSetting(productId, lever);
        if (configEntity != null) {
            if (configEntity.getIsAvailable().equals(true)) {
                // 这个产品是P2P的产品，跑出个异常告诉他
                throw new CustomerException("不能直接申请借贷融资的产品", 888888);
            }
        }
    }

    // 添加融资抵押
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Long doAddLoan(FP2PAddLoanReq req) throws Exception {
        // 查询用户的资产记录，并锁住它
        TpzAccountEntity accountEntity = this.accountDao.getAccountByUserId(req.getUserId());
        if (accountEntity == null)
            throw new CustomerException("未完成实名认证", 888888);
        // 验证用户的安全密码
        this.userManager.validateUserTradePwd(req.getUserId(), req.getTradePwd());

        // 标的融资的产品是配置的
        Long targetProdId = Constants.P2P_LOAN_PZ_PRODUCT_ID;

        // 获取这个产品的配置    这个是页面展示的产品ID
        P2PConfigEntity configEntity = this.p2pConfigDao.getProdSetting(req.getProdId(), req.getLever());
        if (configEntity == null) {
            throw new CustomerException("标的融资产品借贷配置未找到", 888888);
        }
        if (configEntity.getIsAvailable().equals(false)) {
            throw new CustomerException("产品不支持借贷融资", 888888);
        }
        Map<String, Object> globalConfig = this.getP2PGlobalConfig();

        // 查询产品的配置  // 这个查询的是标的的产品信息
        TpzProdEntity prodEntity = this.prodDao.getProd(targetProdId);
        if (prodEntity == null)
            throw new CustomerException("标的融资产品配置未找到", 888888);

        // 确认产品倍数，借贷金额，保证金，财猫出资 数值正确
        // 产品中是否有这个倍数
        List<FProductDetailRes> prodDetailEntityList = this.prodDetailDao.queryProdDetailList(targetProdId);
        FProductDetailRes productDetailRes = null;
        for (FProductDetailRes detailRes : prodDetailEntityList) {
            if (detailRes.getLoanRatioFrom() == Double.valueOf(req.getLever())) {
                if (detailRes.getLoanAmountFrom() <= req.getLoanValue() && req.getLoanValue() <= detailRes.getLoanAmountTo()) {
                    productDetailRes = detailRes;
                    break;
                }
            }
        }
        if (productDetailRes == null)
            throw new CustomerException("不允许的杠杆倍数", 888888);
        // 确认融资金额是否允许
        if (BigMath.compareTo(req.getLoanValue(), productDetailRes.getLoanAmountFrom()) == -1) {
            logger.error("用户 {} 申请阶段融资 {}, 小于系统最小融资 {}", req.getUserId(), req.getLoanValue(), productDetailRes.getLoanAmountFrom());
            throw new CustomerException("小于最小融资金额", 888888);
        }
        if (BigMath.compareTo(req.getLoanValue(), productDetailRes.getLoanAmountTo()) == 1) {
            logger.error("用户 {} 申请阶段融资 {}, 大于系统最小融资 {}", req.getUserId(), req.getLoanValue(), productDetailRes.getLoanAmountTo());
            throw new CustomerException("大于最大融资金额", 888888);
        }
        // 保证金是否正确
        int realLever = BigMath.divide(req.getLoanValue(), req.getMargin()).intValue();
        if (BigMath.compareTo(realLever, req.getLever()) != 0) {
            logger.error("用户 {} 申请的杠杆倍数 {}, 不等于计算的保证金 {}", req.getUserId(), req.getLever(), realLever);
            throw new CustomerException("保证金错误", 888888);
        }
        // 财猫出资的是否正确    (借贷融资数 * 财猫出资比例） 对比投资最小值，向上取整
        Long cmRealValue = 0L;
        // 配置的财猫比例
        if (BigMath.compareTo(req.getCustomRate(), configEntity.getCaimaoRate()) >= 0) {
            Long minInvestValue = BigMath.multiply(globalConfig.get(EP2PConfigKey.理财人最小投资.getKey()) , 100).longValue();
            logger.info("投资人最小投资数量：" + minInvestValue);
            cmRealValue = BigMath.multiply(configEntity.getChuziRate(), req.getLoanValue()).longValue();
            logger.info("先计算的财猫出资：" + cmRealValue);
            Long diff = cmRealValue % minInvestValue;
            logger.info("需要补充的差值: " + diff);
            if (!diff.equals(0L)) {
                cmRealValue = BigMath.add(cmRealValue, BigMath.subtract(minInvestValue, diff)).longValue();
                logger.info("最后财猫出资的数量" + cmRealValue);
            }


            if (BigMath.compareTo(cmRealValue, configEntity.getChuziMin()) == -1)
                cmRealValue = configEntity.getChuziMin();
            if (BigMath.compareTo(cmRealValue, configEntity.getChuziMax()) == 1)
                cmRealValue = configEntity.getChuziMax();
        }
        if (BigMath.compareTo(cmRealValue, req.getCaimaoValue()) != 0) {
            logger.error(" > {} 财猫最小出资数量 {} 最大出资数量 {}", configEntity.getCaimaoRate(), configEntity.getChuziMin(), configEntity.getChuziMax());
            logger.error("用户 {} 出资比例 {} 申请数量 {} 请求的财猫出资数量 {}, 计算出财猫出资数量 {}", req.getUserId(), configEntity.getChuziRate(), req.getLoanValue(), req.getCaimaoValue(), cmRealValue);
            throw new CustomerException("财猫出资数量错误", 888888);
        }

        // 确认用户的手续费定义正确
        if (BigMath.compareTo(req.getCustomRate(), globalConfig.get(EP2PConfigKey.月费率最大值.getKey())) == 1) {
            logger.error("用户自定义费率 {} 大于系统定义的最大值 {}", req.getCustomRate(), globalConfig.get(EP2PConfigKey.月费率最大值.getKey()));
            throw new CustomerException("月费率大于系统设置的最大值", 888888);
        }
        // 确认用户的的自定义手续费是否小于产品的最小利息
        //if (BigMath.compareTo(req.getCustomRate(), productDetailRes.getInterestRate()) == -1) {
        //    logger.error("用户自定义费率 {} 小于产品定义的最小值 {}", req.getCustomRate(), productDetailRes.getInterestRate());
        //    throw new CustomerException("月费率小于产品的最小值", 888888);
        //}
        // 计算用户的借贷的年利率是多少
        // 用户自定义利率 * 12 = 借贷年利率
        double yearRate = BigMath.multiply(req.getCustomRate(), 12).doubleValue();
        // 计算用户应付的借贷利息是多少
        // 计算用户一个月的利息，在乘以他的周期
        Long loanInterest = BigMath.multiply(req.getLoanValue(), req.getCustomRate()).longValue();
        loanInterest = BigMath.multiply(loanInterest, BigMath.divide(req.getPeroid(), 30).intValue()).longValue();

        // 计算用户的融资利息
        Long pzInterest = BigMath.multiply(req.getLoanValue(), productDetailRes.getInterestRate()).longValue();

        // 计算用户总共需要收取的管理费
        Long totalManageFee = 0L;
        if (configEntity.getManageFee() != null) {
            totalManageFee = configEntity.getManageFee();
        }
        // 系统收取的管理费年利率
        double manageRate = BigMath.multiply(configEntity.getManageRate(), 12).doubleValue();
        // 收取的管理费比例，就已经包含在用户的借贷利率里了
//        if (configEntity.getManageRate() != null) {
//            totalManageFee = BigMath.add(totalManageFee, BigMath.multiply(req.getLoanValue(), configEntity.getManageRate())).longValue();
//        }

        // 记录写日志
        logger.info("用户 {} 进行借贷融资", req.getUserId());
        logger.info("请求对象数据 {}", ToStringBuilder.reflectionToString(req));
        logger.info("产品的杠杆 {}", req.getLever());
        logger.info("这个产品的配置 {}", ToStringBuilder.reflectionToString(configEntity));
        logger.info("这个产品的详情 {}", ToStringBuilder.reflectionToString(productDetailRes));
        logger.info("产品融资利息：{}", pzInterest);
        logger.info(" ------------- ");
        logger.info("计算财猫出资数量 {}", cmRealValue);
        logger.info("计算的借贷月利息 {}", req.getCustomRate());
        logger.info("计算的借贷年利息 {}", yearRate);
        logger.info("计算的管理年利息 {}", manageRate);
        logger.info("计算的应付利息 {}", loanInterest);
        logger.info("收取管理费 {}", totalManageFee);



        TpzUserEntity userEntity = this.userDao.getById(req.getUserId());
        //TpzUserExtEntity userExtEntity = this.userExtDao.getById(req.getUserId());
        //生成融资借贷记录，并写入表
        Date curDate = new Date();
        //String gender = userExtEntity.getGender() == null ? "先生" : (userExtEntity.getGender().equals("2") ? "女士" : "先生");
        String userName = ChannelUtil.sirOrLady(userEntity.getUserRealName(), userEntity.getIdcard());
        String loanName = userName + "续约借款";

        P2PLoanRecordEntity loanRecordEntity = new P2PLoanRecordEntity();
        loanRecordEntity.setTargetUserId(req.getUserId());
        loanRecordEntity.setTargetName(loanName);
        loanRecordEntity.setTargetProdId(targetProdId);
        loanRecordEntity.setTargetProdLever(req.getLever().byteValue());
        loanRecordEntity.setTargetProdRate(new BigDecimal(productDetailRes.getInterestRate()));
        loanRecordEntity.setLiftTime(req.getPeroid());
        loanRecordEntity.setTargetAmount(req.getLoanValue());
        loanRecordEntity.setCaimaoValue(req.getCaimaoValue());
        loanRecordEntity.setYearRate(new BigDecimal(yearRate));
        loanRecordEntity.setManageRate(new BigDecimal(manageRate));
        loanRecordEntity.setPayMargin(req.getMargin());
        loanRecordEntity.setPayPzInterest(0L);
        loanRecordEntity.setPayTargetInterest(loanInterest);
        loanRecordEntity.setPayManageFee(totalManageFee);
        loanRecordEntity.setCreated(curDate);
        loanRecordEntity.setLastUpdate(curDate);
        loanRecordEntity.setTargetStatus(EP2PLoanStatus.INIT.getCode().byteValue());

        this.P2PLoanRecordDao.save(loanRecordEntity);

        Long targetId = loanRecordEntity.getTargetId();

        // 冻结用户的保证金
        this.accountManager.dofreezeAmount(targetId.toString(), accountEntity.getPzAccountId(), req.getMargin(), ESeqFlag.COME.getCode(), EAccountBizType.LOAN.getCode());

        // 冻结用户的借贷利息
        this.accountManager.dofreezeAmount(targetId.toString(), accountEntity.getPzAccountId(), loanInterest, ESeqFlag.COME.getCode(), EAccountBizType.LOAN_INTEREST.getCode());

        // 冻结用户的管理费用，如果有的话
        if (totalManageFee > 0) {
            this.accountManager.dofreezeAmount(targetId.toString(), accountEntity.getPzAccountId(), totalManageFee, ESeqFlag.COME.getCode(), EAccountBizType.LOAN_MANAGE_FEE.getCode());
        }
        return targetId;
    }

    // 购买理财（进行投资标的）
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Long doAddInvest(FP2PAddinvestReq req) throws Exception {
        //查询p2p全局配置
        Map<String,Object> config = getP2PGlobalConfig();
        //验证要购买金额是否允许
        Long max = BigMath.multiply(config.get(EP2PConfigKey.理财人最大投资.getKey()), 100L).longValue();
        Long min = BigMath.multiply(config.get(EP2PConfigKey.理财人最小投资.getKey()), 100L).longValue();

        String liftDays = (String) config.get(EP2PConfigKey.借贷有效周期天数.getKey());
        if(StringUtils.isBlank(liftDays)){
            liftDays = "5";
        }
        if(max != null && max.intValue() > 0){
            if(BigMath.compareTo(req.getInvestValue(), max) == 1){
                throw new CustomerException("购买金额不能超过"+max, 888888);
            }
        }
        if(min != null && min.intValue() > 0){
            if(BigMath.compareTo(req.getInvestValue(), min) == -1){
                throw new CustomerException("购买金额不能小于"+min, 888888);
            }
        }
        // 查询用户的资产记录，并锁住它
        TpzAccountEntity accountEntity = this.accountDao.getAccountByUserId(req.getUserId());
        if (accountEntity == null){
            throw new CustomerException("未完成实名认证", 888888);
        }
        //验证用户账户金额是否足够
        if(BigMath.compareTo(req.getInvestValue(), BigMath.subtract(accountEntity.getAvalaibleAmount(), accountEntity.getFreezeAmount())) == 1){
            throw new CustomerException("账户资金不足", 888888);
        }
        // 验证用户的安全密码
        this.userManager.validateUserTradePwd(req.getUserId(), req.getTradePwd());
        // 获取标的,并锁住它
        P2PLoanRecordEntity loan = P2PLoanRecordDao.getLoanRecordAndLock(req.getTargetId());
        if(loan==null){
            throw new CustomerException("标的不存在", 888888);
        }

        // 验证标的是否过期
        if(System.currentTimeMillis() > (loan.getCreated().getTime() + Long.parseLong(liftDays) * 86400 * 1000)) {
            throw new CustomerException("标的已经过期", 888888);
        }
        // 验证标的状态
        if(EP2PLoanStatus.INIT.getCode().byteValue() != loan.getTargetStatus()){
            throw new CustomerException("标的状态不允许购买", 888888);
        }
        // 自己不能投自己的标的
        if(loan.getTargetUserId().compareTo(req.getUserId()) == 0){
            throw new CustomerException("不能投自己的标的", 888888);
        }
        // 确认购买金额是否允许
        Long surplus  = loan.getTargetAmount() - loan.getActualValue() - loan.getCaimaoValue();//标的剩余金额
        int status = BigMath.compareTo(req.getInvestValue(), surplus);//比较购买金额与标的金额
        boolean isFull = false;//是否满标
        if ( status == 1) {
            logger.error("用户 {} 申请购买 {}, 大于标的{}剩余金额", req.getUserId(), req.getInvestValue(), surplus);
            throw new CustomerException("购买金额大于标的剩余金额", 888888);
        } else if(status == 0) {
            isFull = true;
        }
        // 计算用户的年利率，用户的投资标的的利息  减去 管理费年利率  ，就等于新的投资人的年利率
        BigDecimal investYearRate = BigMath.subtract(loan.getYearRate(), loan.getManageRate());

        //插入购买记录
        P2PInvestRecordEntity invest = new P2PInvestRecordEntity();
        // invest.setExpirationDatetime(expirationDatetime);//到期时间
        //invest.setInterestDatetime(interestDatetime);//计息开始时间
        invest.setInterestPeriod(loan.getInterestSettleDays());//支付利息周期
        invest.setInvestCreated(new Date(System.currentTimeMillis()));
        invest.setInvestStatus(EP2PInvestStatus.INIT.getCode().byteValue());
        invest.setInvestUserId(req.getUserId());
        invest.setInvestValue(req.getInvestValue());
        invest.setLiftTime(loan.getLiftTime());//使用期限
        invest.setYearRate(investYearRate);//年利率
        invest.setYearValue(BigMath.multiply(req.getInvestValue(), investYearRate).longValue());//年息
        invest.setTargetId(loan.getTargetId());
        invest.setTargetUserId(loan.getTargetUserId());
        invest.setTargetProdId(loan.getTargetProdId());
        invest.setPayInterest(0L);
        invest.setTargetName(loan.getTargetName());
        P2PInvestRecordDao.save(invest);
        //更新标的购买人数、金额
        loan.setActualValue(BigMath.add(req.getInvestValue(), loan.getActualValue()).longValue());
        if(isFull){
            loan.setTargetFullDatetime(new Date(System.currentTimeMillis()));
            loan.setTargetStatus(EP2PLoanStatus.FULL.getCode().byteValue());
        }
        P2PLoanRecordDao.updateActualAndUserNum(loan);
        //如果标的满标，更新所有购买此标的人的投资记录为满标
        if(isFull){
            P2PInvestRecordDao.updateStatusByTargetId(loan.getTargetId(),EP2PInvestStatus.FULL.getCode().byteValue());
        }
        //扣除用户账户金额
        return accountManager.doUpdateAvaiAmount(invest.getInvestId().toString(), accountEntity.getPzAccountId(), req.getInvestValue(), EAccountBizType.INVS_OUT.getCode(), ESeqFlag.GO.getCode());
    }

    @Override
    public Map<String, Object> queryUserSummaryInfo(Long userId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Long userTotalInvestment = P2PInvestRecordDao.queryUserTotalInvestment(userId);
        Long userTotalIncome = P2PInvestRecordDao.queryUserTotalIncome(userId);
        Long userTotalMarginReceived = P2PInvestRecordDao.queryUserTotalMarginReceived(userId);
        Long userTotalMarginClosed = P2PInvestRecordDao.queryUserTotalMarginClosed(userId);
        Long userTotalInterestReceived = P2PInvestRecordDao.queryUserTotalInterestReceived(userId);
        Long userTotalInterestClosed = P2PInvestRecordDao.queryUserTotalInterestClosed(userId);
        //累计投资
        result.put("userTotalInvestment", userTotalInvestment == null ? 0 : userTotalInvestment);
        //累计收益
        result.put("userTotalIncome", userTotalIncome == null ? 0 : userTotalIncome);
        //已收本金
        result.put("userTotalMarginReceived", userTotalMarginReceived == null ? 0 : userTotalMarginReceived);
        //待收本金
        result.put("userTotalMarginClosed", userTotalMarginClosed == null ? 0 : userTotalMarginClosed);
        //已收利息
        result.put("userTotalInterestReceived", userTotalInterestReceived == null ? 0 : userTotalInterestReceived);
        //待收利息
        result.put("userTotalInterestClosed", userTotalInterestClosed == null ? 0 : userTotalInterestClosed);
        return result;
    }

    @Override
    public FP2PQueryUserPageInvestReq queryUserPageInvest(FP2PQueryUserPageInvestReq req) throws Exception {
        List<FP2PQueryUserPageInvestRes> list = P2PInvestRecordDao.queryFP2PQueryUserPageInvestResWithPage(req);
        req.setItems(list);
        return req;
    }

    @Override
    public P2PInvestRecordEntity queryUserInvestRecord(Long userId, Long investId) throws Exception {
        return P2PInvestRecordDao.queryUserInvestRecord(userId, investId);
    }

    @Override
    public FP2PQueryUserPageInterestReq queryUserPageInterest(FP2PQueryUserPageInterestReq req) throws Exception {
        List<FP2PQueryUserPageInterestRes> list = P2PInterestRecordDao.queryFP2PQueryUserPageInterestResWithPage(req);
        req.setItems(list);
        return req;
    }

    @Override
    public FP2PQueryUserPageLoanReq queryUserPageLoan(FP2PQueryUserPageLoanReq req) throws Exception {
        List<FP2PQueryUserPageLoanRes> list = P2PLoanRecordDao.queryFP2PQueryUserPageLoanResWithPage(req);
        if(list != null){
            //查询p2p全局配置
            Map<String,Object> config = getP2PGlobalConfig();
            //验证要购买金额是否允许
            String liftDays = (String) config.get(EP2PConfigKey.借贷有效周期天数.getKey());
            if(StringUtils.isBlank(liftDays)){
                liftDays = "5";
            }
            for(FP2PQueryUserPageLoanRes fp2PQueryUserPageLoanRes:list){
                fp2PQueryUserPageLoanRes.setTargetLiftTime(fp2PQueryUserPageLoanRes.getCreated().getTime() + Long.parseLong(liftDays) * 86400 * 1000 - System.currentTimeMillis());
            }
        }
        req.setItems(list);
        return req;
    }

    @Override
    public P2PLoanRecordEntity queryUserLoanRecord(Long userId, Long loanId) throws Exception {
        P2PLoanRecordEntity P2PLoanRecordEntity = P2PLoanRecordDao.queryUserLoanRecord(userId, loanId);
        //查询p2p全局配置
        Map<String,Object> config = getP2PGlobalConfig();
        //验证要购买金额是否允许
        String liftDays = (String) config.get(EP2PConfigKey.借贷有效周期天数.getKey());
        if(StringUtils.isBlank(liftDays)){
            liftDays = "5";
        }
        if(P2PLoanRecordEntity != null){
            P2PLoanRecordEntity.setTargetLiftTime((P2PLoanRecordEntity.getCreated().getTime() + Long.parseLong(liftDays) * 86400 * 1000 - System.currentTimeMillis()) / 1000);
            TpzUserEntity tpzUserEntity = userDao.getById(P2PLoanRecordEntity.getTargetUserId());
            P2PLoanRecordEntity.setIsTrust(tpzUserEntity.getIsTrust());
            P2PLoanRecordEntity.setMobile(ChannelUtil.hide(tpzUserEntity.getMobile(), 3, 3));
            P2PLoanRecordEntity.setLoanUserName(ChannelUtil.sirOrLady(tpzUserEntity.getUserRealName(), tpzUserEntity.getIdcard()));
        }
        return P2PLoanRecordEntity;
    }

    @Override
    public FP2PQueryPageLoanReq queryPageLoan(FP2PQueryPageLoanReq req) throws Exception {
        List<FP2PQueryPageLoanRes> list = P2PLoanRecordDao.queryFP2PQueryPageLoanResWithPage(req);
        if(list != null){
            //查询p2p全局配置
            Map<String,Object> config = getP2PGlobalConfig();
            //验证要购买金额是否允许
            String liftDays = (String) config.get(EP2PConfigKey.借贷有效周期天数.getKey());
            if(StringUtils.isBlank(liftDays)){
                liftDays = "5";
            }
            for(FP2PQueryPageLoanRes fp2PQueryPageLoanRes:list){
                // 设置新的年利率, 投资年利率 减去 管理年利率
                fp2PQueryPageLoanRes.setYearRate(BigMath.subtract(fp2PQueryPageLoanRes.getYearRate(), fp2PQueryPageLoanRes.getManageRate()));

                fp2PQueryPageLoanRes.setTargetUserId(null);
                fp2PQueryPageLoanRes.setTargetLiftTime(fp2PQueryPageLoanRes.getCreated().getTime() + Long.parseLong(liftDays) * 86400 * 1000 - System.currentTimeMillis());
            }
        }
        req.setItems(list);
        return req;
    }

    @Override
    public FP2PQueryPageLoanReq queryPageFullLoan(FP2PQueryPageLoanReq req) throws Exception {
        List<FP2PQueryPageLoanRes> list = P2PLoanRecordDao.queryFP2PQueryPageLoanFullResWithPage(req);
        if(list != null){
            //查询p2p全局配置
            Map<String,Object> config = getP2PGlobalConfig();
            //验证要购买金额是否允许
            String liftDays = (String) config.get(EP2PConfigKey.借贷有效周期天数.getKey());
            if(StringUtils.isBlank(liftDays)){
                liftDays = "5";
            }
            for(FP2PQueryPageLoanRes fp2PQueryPageLoanRes:list){
                // 设置新的年利率, 投资年利率 减去 管理年利率
                fp2PQueryPageLoanRes.setYearRate(BigMath.subtract(fp2PQueryPageLoanRes.getYearRate(), fp2PQueryPageLoanRes.getManageRate()));

                fp2PQueryPageLoanRes.setTargetUserId(null);
                fp2PQueryPageLoanRes.setTargetLiftTime(fp2PQueryPageLoanRes.getCreated().getTime() + Long.parseLong(liftDays) * 86400 * 1000 - System.currentTimeMillis());
            }
        }
        req.setItems(list);
        return req;
    }

    @Override
    public P2PLoanRecordEntity queryLoanRecord(Long loanId) throws Exception {
        P2PLoanRecordEntity P2PLoanRecordEntity = P2PLoanRecordDao.queryLoanRecord(loanId);
        //查询p2p全局配置
        Map<String,Object> config = getP2PGlobalConfig();
        //验证要购买金额是否允许
        String liftDays = (String) config.get(EP2PConfigKey.借贷有效周期天数.getKey());
        if(StringUtils.isBlank(liftDays)){
            liftDays = "5";
        }
        if(P2PLoanRecordEntity != null){
            if (P2PLoanRecordEntity.getTargetStatus().equals(EP2PLoanStatus.INIT.getCode().byteValue())) {
                P2PLoanRecordEntity.setTargetLiftTime((P2PLoanRecordEntity.getCreated().getTime() + Long.parseLong(liftDays) * 86400 * 1000 - System.currentTimeMillis()) / 1000);
            } else {
                P2PLoanRecordEntity.setTargetLiftTime(0L);
            }
            TpzUserEntity tpzUserEntity = userDao.getById(P2PLoanRecordEntity.getTargetUserId());
            P2PLoanRecordEntity.setIsTrust(tpzUserEntity.getIsTrust());
            P2PLoanRecordEntity.setMobile(ChannelUtil.hide(tpzUserEntity.getMobile(), 3, 3));
            P2PLoanRecordEntity.setLoanUserName(ChannelUtil.sirOrLady(tpzUserEntity.getUserRealName(), tpzUserEntity.getIdcard()));
            // 从新计算进度
            //P2PLoanRecordEntity.setTargetRate(BigMath.divide(BigMath.add(P2PLoanRecordEntity.getActualValue(), P2PLoanRecordEntity.getCaimaoValue()), P2PLoanRecordEntity.getTargetAmount()).toString());
            // 设置新的年利率, 投资年利率 减去 管理年利率
            P2PLoanRecordEntity.setYearRate(BigMath.subtract(P2PLoanRecordEntity.getYearRate(), P2PLoanRecordEntity.getManageRate()));
        }
        return P2PLoanRecordEntity;
    }

    @Override
    public FP2PQueryLoanPageInvestReq queryLoanPageInvest(FP2PQueryLoanPageInvestReq req) throws Exception {
        List<FP2PQueryLoanPageInvestRes> list = P2PInvestRecordDao.queryFP2PQueryLoanPageInvestResWithPage(req);
        req.setItems(list);
        return req;
    }
    
    @Override
    public FP2PQueryLoanPageInvestWithUserReq queryLoanPageInvestWithUser(FP2PQueryLoanPageInvestWithUserReq req) throws Exception {
        List<FP2PQueryLoanPageInvestWithUserRes> list = P2PInvestRecordDao.queryFP2PQueryLoanAndUserPageInvestResWithPage(req);
        req.setItems(list);
        return req;
    }

    @Override
    public FP2PQueryPageLoanAndUserReq queryPageLoanWithUser(FP2PQueryPageLoanAndUserReq req) throws Exception {
        List<FP2PQueryPageLoanAndUserRes> list = P2PLoanRecordDao.queryFP2PQueryPageLoanAndUserResWithPage(req);
        req.setItems(list);
        return req;
    }
    @Override
    public FP2PQueryStatisticsByUserReq queryStatisticsByUserWithPage(FP2PQueryStatisticsByUserReq req) throws Exception {
        List<FP2PQueryStatisticsByUserRes> list = P2PInvestRecordDao.queryStatisticsByUserWithPage(req);
        req.setItems(list);
        return req;
    }

    // 后台  查询投资人的投资列表
    @Override
    public FP2PQueryPageInvestListReq queryPageInvestListWithUser(FP2PQueryPageInvestListReq req) throws Exception {
        List<FP2PQueryLoanPageInvestWithUserRes> list = this.P2PInvestRecordDao.queryInvestPageWithPage(req);
        req.setItems(list);
        return req;
    }

    /**
     * 流特定的一个标
     * @param targetId 标的ID
     * @param targetUserId 用户id
     * @param p2pLoanStatus 标的状态
     * @throws Exception
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void doFailedTarget(Long targetId,Long targetUserId, EP2PLoanStatus p2pLoanStatus) throws Exception {
        P2PLoanRecordEntity p2pLoanRecord = P2PLoanRecordDao.getLoanRecordAndLock(targetId);
        if(p2pLoanRecord==null){
            throw new CustomerException("该标的不存在："+targetId, 400100);
        }else if(p2pLoanRecord.getTargetUserId().longValue()!=targetUserId.longValue()){
            throw new CustomerException("非法操作", 400101);
        }
        if(p2pLoanStatus.getCode().intValue()==EP2PLoanStatus.CANCEL.getCode().intValue()){//取消,需要查看该标的的投资情况
            if(((int)p2pLoanRecord.getIsExt()) == 1) throw new CustomerException("该标的为自动展期，无法取消", 400102);
            List<P2PInvestRecordEntity> investList=P2PInvestRecordDao.getInvestUsers(p2pLoanRecord, EP2PInvestStatus.INIT);
            if(investList!=null && investList.size()>0){
                throw new CustomerException("该标的已有投资者，无法取消", 400102);
            }
        }
        //1.流借贷用户,解除借贷用户的冻结（支付的保证金，支付的融资利息，支付的标的利息P2P，支付的管理费）
        Long toUnfreezeAmount=p2pLoanRecord.getPayMargin()+
                p2pLoanRecord.getPayPzInterest()+p2pLoanRecord.getPayTargetInterest()
                +p2pLoanRecord.getPayManageFee();
        if(((int)p2pLoanRecord.getIsExt()) == 0){
            accountManager.dofreezeAmount(p2pLoanRecord.getTargetId().toString(),
                    accountDao.getAccountByUserId(p2pLoanRecord.getTargetUserId()).getPzAccountId(),
                    toUnfreezeAmount, ESeqFlag.GO.getCode(),EAccountBizType.FAILED_TARGET.getCode() );
        }

        //2.流出借用户
        List<P2PInvestRecordEntity> investList=P2PInvestRecordDao.getInvestUsers(p2pLoanRecord,EP2PInvestStatus.INIT);
        if(investList!=null && investList.size()>0){
            for(P2PInvestRecordEntity p2pInvestRecord: investList){
                accountManager.doUpdateAvaiAmount("FT_"+System.currentTimeMillis(), 
                        accountDao.getAccountByUserId(p2pInvestRecord.getInvestUserId()).getPzAccountId(),
                        p2pInvestRecord.getInvestValue(),EAccountBizType.FAILED_TARGET.getCode(),ESeqFlag.COME.getCode());
                
            }
        }
        //3.修改p2p_loan_record的状态
        P2PLoanRecordDao.updateStatus(p2pLoanRecord.getTargetId(), p2pLoanStatus);
        if(p2pLoanStatus.getCode().intValue()==EP2PLoanStatus.CANCEL.getCode().intValue()){//取消
            P2PInvestRecordDao.updateStatusByTargetId(p2pLoanRecord.getTargetId(),EP2PInvestStatus.CANCEL.getCode().byteValue());
        }else if(p2pLoanStatus.getCode().intValue()==EP2PLoanStatus.FAIL.getCode().intValue()){//流标
            P2PInvestRecordDao.updateStatusByTargetId(p2pLoanRecord.getTargetId(),EP2PInvestStatus.FAIL.getCode().byteValue());
        }
    }

    @Override
    public Integer queryLoanCount(Integer targetStatus) throws Exception {
        EP2PLoanStatus ep2PLoanStatus = EP2PLoanStatus.findByCode(targetStatus);
        if(ep2PLoanStatus == null){
            throw new BizServiceException("10422", "状态不正确");
        }else{
            return P2PLoanRecordDao.queryLoanCount(ep2PLoanStatus);
        }
    }

    @Override
    public Integer queryLoanFullCount() throws Exception {
        return P2PLoanRecordDao.queryLoanFullCount();
    }

    // 自动补满P2P投资的剩余数量
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public boolean doFullCaimaoP2PInvest(Long targetId) throws Exception {
        // 查询投资标的的信息
        P2PLoanRecordEntity p2PLoanRecordEntity = P2PLoanRecordDao.getLoanRecordAndLock(targetId);
        if (p2PLoanRecordEntity == null) throw new CustomerException("标的不存在", 888888);
        if ( ! p2PLoanRecordEntity.getTargetStatus().equals(EP2PLoanStatus.INIT.getCode().byteValue())) {
            throw new CustomerException("标的状态错误", 888888);
        }
        // 看看还差多少
        Long surplusAmount = p2PLoanRecordEntity.getTargetAmount() - p2PLoanRecordEntity.getCaimaoValue() - p2PLoanRecordEntity.getActualValue();
        if (surplusAmount <= 0) {
            throw new CustomerException("标的剩余数量不大于0", 888888);
        }
        // 更新
        p2PLoanRecordEntity.setCaimaoValue(p2PLoanRecordEntity.getCaimaoValue() + surplusAmount);
        p2PLoanRecordEntity.setTargetFullDatetime(new Date());
        p2PLoanRecordEntity.setTargetStatus(EP2PLoanStatus.FULL.getCode().byteValue());
        this.P2PLoanRecordDao.updateCaimaoValue(p2PLoanRecordEntity);
        return true;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void doAutoExtTarget(Long targetId) throws Exception{
        //查询并锁定记录
        P2PLoanRecordEntity P2PLoanRecord = P2PLoanRecordDao.getLoanRecordAndLock(targetId);
        if(P2PLoanRecord == null) throw new CustomerException("该标的不存在：" + targetId, 500100);
        //是否已还款
        if(((int)P2PLoanRecord.getIsRepayment()) != 0) throw new CustomerException("不符合自动展期要求：" + targetId + ", 原因：已还款", 500200);
        //是否关联合约
        if(P2PLoanRecord.getContractNo() <= 0) throw new CustomerException("不符合自动展期要求：" + targetId + ", 原因：未关联合约", 500200);
        //合约是否还款
        TpzLoanContractEntity loanContractEntity = loanContractDao.getById(P2PLoanRecord.getContractNo());
        if(loanContractEntity == null) throw new CustomerException("不符合自动展期要求：" + targetId + ", 原因：已还款", 500200);
        //合约结息次数
        Integer billCnt = tpzAccruedInterestBillDao.queryBillReceivedCnt(loanContractEntity.getContractNo());
        if(billCnt == null) throw new CustomerException("不符合自动展期要求：" + targetId + ", 原因：结息记录没有找到", 500200);
        //排除初始结息
        billCnt -= 1;
        //增加对旧展期方式的兼容
        if(P2PLoanRecord.getExtTargetId() > 0) billCnt += 1;
        //该标的展期次数
        Integer extCnt = P2PLoanRecordDao.queryExtCnt(P2PLoanRecord.getTargetId());
        if(extCnt == null) extCnt = 0;

        logger.info("extCnt {} billCny {}", extCnt, billCnt);
        //如果发现
        if(extCnt < billCnt){
            // 获取产品配置
            // 获取这个产品的配置    这个是页面展示的产品ID
            P2PConfigEntity configEntity = this.p2pConfigDao.getProdSetting(3L, P2PLoanRecord.getTargetProdLever().intValue());
            if (configEntity == null) {
                throw new CustomerException("标的融资产品借贷配置未找到", 888888);
            }
            BigDecimal manageFee = BigMath.multiply(configEntity.getManageRate(), 12);
            BigDecimal minRate = new BigDecimal(0.12);
            BigDecimal maxRate = new BigDecimal(0.15);
            // 如果之后的利率比最小的小；年利率减最小利率
            if (BigMath.compareTo(BigMath.subtract(P2PLoanRecord.getYearRate(), manageFee), minRate) == -1) {
                manageFee = BigMath.subtract(P2PLoanRecord.getYearRate(), minRate);
            }
            // 如果之后的利率比最大的大，最大利率减年利率
            if (BigMath.compareTo(BigMath.subtract(P2PLoanRecord.getYearRate(), manageFee), maxRate) == 1) {
                manageFee = BigMath.subtract(P2PLoanRecord.getYearRate(), maxRate);
            }
            if (BigMath.compareTo(manageFee, 0) == -1) {
                manageFee = new BigDecimal(0);
            }
            //开始自动展期吧
            P2PLoanRecordEntity P2PLoanRecordFiled = new P2PLoanRecordEntity();
            P2PLoanRecordFiled.setTargetUserId(P2PLoanRecord.getTargetUserId());
            P2PLoanRecordFiled.setTargetName(P2PLoanRecord.getTargetName());
            P2PLoanRecordFiled.setTargetProdId(P2PLoanRecord.getTargetProdId());
            P2PLoanRecordFiled.setTargetProdLever(P2PLoanRecord.getTargetProdLever());
            P2PLoanRecordFiled.setTargetProdRate(P2PLoanRecord.getTargetProdRate());
            P2PLoanRecordFiled.setLiftTime(30);
            P2PLoanRecordFiled.setTargetAmount(P2PLoanRecord.getTargetAmount());
            P2PLoanRecordFiled.setCaimaoValue(P2PLoanRecord.getCaimaoValue());
            P2PLoanRecordFiled.setYearRate(P2PLoanRecord.getYearRate());
            P2PLoanRecordFiled.setManageRate(manageFee);    // 管理年利率使用新的配置中的年利率
            P2PLoanRecordFiled.setPayMargin(P2PLoanRecord.getPayMargin());
            P2PLoanRecordFiled.setPayPzInterest(0L);
            P2PLoanRecordFiled.setPayTargetInterest(P2PLoanRecord.getPayTargetInterest());
            P2PLoanRecordFiled.setPayManageFee(P2PLoanRecord.getPayManageFee());
            P2PLoanRecordFiled.setCreated(new Date());
            P2PLoanRecordFiled.setLastUpdate(new Date());
            P2PLoanRecordFiled.setTargetStatus(EP2PLoanStatus.INIT.getCode().byteValue());
            P2PLoanRecordFiled.setIsExt(new Byte("1"));
            P2PLoanRecordFiled.setOwnerTargetId(P2PLoanRecord.getTargetId());
            this.P2PLoanRecordDao.save(P2PLoanRecordFiled);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void doAutoExtTargetFull(Long targetId) throws Exception{
        //查询并锁定记录
        P2PLoanRecordEntity P2PLoanRecord = P2PLoanRecordDao.getLoanRecordAndLock(targetId);
        if(P2PLoanRecord == null) throw new CustomerException("该标的不存在：" + targetId, 500100);
        if(((int)P2PLoanRecord.getIsExt()) != 1) throw new CustomerException("该标的不是自动展期的：" + targetId, 500101);
        if(((int)P2PLoanRecord.getTargetStatus()) != EP2PLoanStatus.FULL.getCode()) throw new CustomerException("该标的状态不为满标：" + targetId, 500102);

        //变更投资表
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("expirationDatetime", DateUtil.addDays(P2PLoanRecord.getTargetFullDatetime(), P2PLoanRecord.getLiftTime()));
        paraMap.put("interestDatetime", P2PLoanRecord.getTargetFullDatetime());
        paraMap.put("investStatus", EP2PInvestStatus.INCOME.getCode().byteValue());
        paraMap.put("lastUpdate", P2PLoanRecord.getTargetFullDatetime());
        paraMap.put("fullDatetime", P2PLoanRecord.getTargetFullDatetime());
        P2PInvestRecordDao.updateExtByTargetId(P2PLoanRecord.getTargetId(), paraMap);

        //变更标的表
        P2PLoanRecordDao.updateStatus(P2PLoanRecord.getTargetId(), EP2PLoanStatus.REPAYMENT);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void updateIsRepayment(Long targetId) throws Exception{
        P2PLoanRecordEntity P2PLoanRecord = P2PLoanRecordDao.getLoanRecordAndLock(targetId);
        if(P2PLoanRecord == null) throw new CustomerException("该标的不存在：" + targetId, 500100);
        P2PLoanRecordDao.updateIsRepayment(targetId);
        //查找是否有相关的展期标如果有也置为已还款
        if(P2PLoanRecord.getExtTargetId() > 0){
            P2PLoanRecordDao.updateIsRepayment(P2PLoanRecord.getExtTargetId());
        }
    }

    /**
     * 添加自定义的P2P借贷记录
     * @param entity
     * @return
     * @throws Exception
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    @Override
    public Long addCustomLoanRecord(P2PLoanRecordEntity entity) throws Exception {
        entity.setTargetUserId(1L);
        entity.setTargetProdLever(new Byte("5"));   // 设置为5倍杠杆
        entity.setTargetProdId(Long.valueOf("3"));
        entity.setTargetProdRate(new BigDecimal(0));
        entity.setInterestSettleDays(30);
        entity.setManageRate(new BigDecimal(0));
        // 支付的保证金、配资利息、管理费都为0
        entity.setPayMargin(0L);
        entity.setPayManageFee(0L);
        entity.setPayPzInterest(0L);
        // 支付的标的利息
        entity.setPayTargetInterest(BigMath.multiply(BigMath.divide(BigMath.multiply(entity.getYearRate(), entity.getTargetAmount()), 12), BigMath.divide(entity.getLiftTime(), 30)).longValue());

        entity.setTargetStatus(EP2PLoanStatus.INIT.getCode().byteValue());
        entity.setCreated(new Date());
        entity.setLastUpdate(new Date());
        entity.setP2pType(1);
        this.P2PLoanRecordDao.save(entity);
        return entity.getTargetId();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void doPrepayment(P2PInvestRecordEntity p2pInvestRecord) throws Exception{
        Date today = new Date();

        //计算天数
        long timeDiff = today.getTime() - p2pInvestRecord.getLastUpdate().getTime();
        double days = Math.ceil(((double) timeDiff) / 86400 / 1000);
        int dayInt = (int) days;
        //计算金额 year_value / 12 /30 * (当前时间 - 最后更新时间)
        Long repaymentAmount = p2pInvestRecord.getYearValue() / 12 * dayInt / 30;

        System.out.println(p2pInvestRecord.getInvestId() + " 计息天数：" + dayInt + "，返息：" + (double)(repaymentAmount / 100));
        //执行利息返还

        // 检查是否已经进行过发息操作
        Integer interestExist = P2PInterestRecordDao.queryInvestInterestExist(p2pInvestRecord.getInvestId(), p2pInvestRecord.getInterestTimes() + 1);
        if (interestExist <= 0) {
            //变更投资记录
            p2pInvestRecord.setPayInterest(p2pInvestRecord.getPayInterest() + repaymentAmount);
            p2pInvestRecord.setLastUpdate(new Date());
            p2pInvestRecord.setInterestTimes(p2pInvestRecord.getInterestTimes() + 1);
            P2PInvestRecordDao.update(p2pInvestRecord);

            //给用户添加资产
            accountManager.doUpdateAvaiAmount(
                    "DI_" + System.currentTimeMillis(),
                    accountDao.getAccountByUserId(p2pInvestRecord.getInvestUserId()).getPzAccountId(),
                    repaymentAmount,
                    EAccountBizType.P2PSETTLE.getCode(),
                    ESeqFlag.COME.getCode());

            //在添加结息记录
            P2PInterestRecordEntity p2pInterestRecord = new P2PInterestRecordEntity();
            p2pInterestRecord.setInvestId(p2pInvestRecord.getInvestId());
            p2pInterestRecord.setInvestUserId(p2pInvestRecord.getInvestUserId());
            p2pInterestRecord.setTargetId(p2pInvestRecord.getTargetId());
            p2pInterestRecord.setTargetUserId(p2pInvestRecord.getTargetUserId());
            p2pInterestRecord.setInterestDate(new Date(1443493800L * 1000L));
            p2pInterestRecord.setInterestValue(repaymentAmount);
            p2pInterestRecord.setCreateTime(new Date());
            p2pInterestRecord.setInterestTimes(p2pInvestRecord.getInterestTimes());
            P2PInterestRecordDao.save(p2pInterestRecord);
        }

        //给投资人返本金
        accountManager.doUpdateAvaiAmount(
                "DI_"+System.currentTimeMillis(),
                accountDao.getAccountByUserId(p2pInvestRecord.getInvestUserId()).getPzAccountId(),
                p2pInvestRecord.getInvestValue(),
                EAccountBizType.P2P_PRINCIPAL.getCode(),
                ESeqFlag.COME.getCode());
        P2PInvestRecordDao.updateStatusByInvestId(p2pInvestRecord.getInvestId(), EP2PInvestStatus.END);
        //设置p2p_loan_apply的状态为已还款
        P2PLoanRecordDao.updateStatus(p2pInvestRecord.getTargetId(), EP2PLoanStatus.END);
    }
}

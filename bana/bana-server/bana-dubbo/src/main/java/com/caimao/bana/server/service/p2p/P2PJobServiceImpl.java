package com.caimao.bana.server.service.p2p;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.bana.api.entity.TpzLoanApplyEntity;
import com.caimao.bana.api.entity.TsysParameterEntity;
import com.caimao.bana.api.entity.req.FP2PQueryPageLoanReq;
import com.caimao.bana.api.entity.res.FP2PQueryPageLoanRes;
import com.caimao.bana.api.enums.EP2PConfigKey;
import com.caimao.bana.server.dao.sysParameter.SysParameterDao;
import com.caimao.bana.server.service.loan.LoanServiceImpl;
import com.caimao.bana.server.utils.BigMath;
import com.caimao.bana.server.utils.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.caimao.bana.api.entity.p2p.P2PInvestRecordEntity;
import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.enums.EP2PInvestStatus;
import com.caimao.bana.api.enums.EP2PLoanStatus;
import com.caimao.bana.api.service.IP2PJobService;
import com.caimao.bana.server.dao.p2p.P2PInvestRecordDao;
import com.caimao.bana.server.dao.p2p.P2PLoanRecordDao;
import com.caimao.bana.server.service.loan.LoanApplyServiceHelper;

/**
 * P2P融资job的接口实现类 Created by yanjg on 2015/6/8.
 */
@Service("p2pJobService")
public class P2PJobServiceImpl implements IP2PJobService {
    private static final int LIST_SIZE = 100;
    private static final Logger logger = LoggerFactory.getLogger(P2PJobServiceImpl.class);
    @Autowired
    private P2PLoanRecordServiceHelper p2pLoanRecordServiceHelper;
    @Autowired
    private LoanApplyServiceHelper loanApplyServiceHelper;
    @Autowired
    private P2PInvestRecordServiceHelper p2pInvestRecordServiceHelper;
    @Autowired
    private P2PLoanRecordDao p2pLoanRecordDao;
    @Autowired
    private P2PInvestRecordDao p2pInvestRecordDao;
    @Autowired
    private P2PServiceImpl p2pService;
    @Autowired
    private LoanServiceImpl loanService;
    @Autowired
    private SysParameterDao sysParameterDao;

    /*
     * 生成p2p借贷 满标后，自动提交借贷申请，只插入借贷申请，不涉及到资产操作
     *  每10分钟更新一次。
     */
    @Override
    public void doCommitP2PLoanApply() throws Exception {
        try {
            List<P2PLoanRecordEntity> p2pLoanRecordList = new ArrayList<P2PLoanRecordEntity>();
            int start = 0;
            do {
                p2pLoanRecordList = p2pLoanRecordServiceHelper.getLoanRecordList(start, LIST_SIZE, EP2PLoanStatus.FULL.getCode());
                if (p2pLoanRecordList != null && p2pLoanRecordList.size() > 0) {
                    for (P2PLoanRecordEntity p2pLoanRecord : p2pLoanRecordList) {
                        // 根据标的的类型，如果是自定义合约的话，就不需要提交配资申请订单了
                        if (p2pLoanRecord.getP2pType() == 1) {
                            //设置投资表的计息时间
                            p2pInvestRecordServiceHelper.setInterestTime(p2pLoanRecord);
                            //设置p2p_invest_record中的状态为还款中
                            p2pInvestRecordDao.updateStatusByTargetId(p2pLoanRecord.getTargetId(),EP2PInvestStatus.INCOME.getCode().byteValue());
                            //设置p2p_loan_record表的状态为还款中
                            p2pLoanRecordDao.updateStatus(p2pLoanRecord.getTargetId(), EP2PLoanStatus.REPAYMENT);
                        } else {
                            loanApplyServiceHelper.commitLoanApply(p2pLoanRecord);
                        }
                    }
                }
                start = start + LIST_SIZE;
            } while (p2pLoanRecordList.size() == LIST_SIZE);
            logger.info("满标提交融资任务运行结束 - -");
        } catch (Exception e) {
            logger.error("满标提交融资任务运行失败{}", e);
        }
    }
    
    /*
     * 当P2P借贷满标并审核通过时，设置p2p_invest_record的计息时间job
     * 将状态修改为收益中
     * 2分钟运行一次
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void doSetInterestTime() throws Exception {
        List<P2PLoanRecordEntity> p2pLoanRecordList = new ArrayList<P2PLoanRecordEntity>();
        //获取已经发出融资申请，并且融资审核通过
        p2pLoanRecordList = p2pLoanRecordServiceHelper.getFullCheckedLoanRecordList();
        if (p2pLoanRecordList != null && p2pLoanRecordList.size() > 0) {
            for (P2PLoanRecordEntity p2pLoanRecord : p2pLoanRecordList) {
                //设置投资表的计息时间
                p2pInvestRecordServiceHelper.setInterestTime(p2pLoanRecord);
                //设置p2p_invest_record中的状态为还款中
                p2pInvestRecordDao.updateStatusByTargetId(p2pLoanRecord.getTargetId(),EP2PInvestStatus.INCOME.getCode().byteValue());
                //设置p2p_loan_record表的状态为还款中
                p2pLoanRecordDao.updateStatus(p2pLoanRecord.getTargetId(), EP2PLoanStatus.REPAYMENT);
            }
        }
        logger.info("设置计息时间任务运行结束 - -");
    }

    /*
     * 扫描p2p_invest_record过期日期小于昨天的记录，给用户发息。如果到期的话，同时返还本金，修改状态为4已还款
     * 每天更新一次。早上8:00
     */
    @Override
    public void doDistributeInterest() throws Exception {
        List<P2PInvestRecordEntity> p2PInvestRecordList = new ArrayList<P2PInvestRecordEntity>();
        int start = 0;
        do {
            p2PInvestRecordList = p2pInvestRecordServiceHelper.getInvestRecordList(start, LIST_SIZE, EP2PInvestStatus.INCOME.getCode());
            if (p2PInvestRecordList != null && p2PInvestRecordList.size() > 0) {
                for (P2PInvestRecordEntity p2pInvestRecord : p2PInvestRecordList) {
                    // 如果出错了，记录错误，在重试几次就好了
                    try {
                        p2pInvestRecordServiceHelper.commitDistributeInterest(p2pInvestRecord);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }
            start = start + LIST_SIZE;
        } while (p2PInvestRecordList.size() == LIST_SIZE);
        logger.info("运行结息任务结束 - -");
    }

    /*
     * 流标脚本job
     * 每1分钟更新一次。
     */
    @Override
    public void doFailedTarget() throws Exception {
        List<P2PLoanRecordEntity> p2pLoanRecordList = new ArrayList<P2PLoanRecordEntity>();
        p2pLoanRecordList = p2pLoanRecordServiceHelper.getFailedLoanRecordList();
        if (p2pLoanRecordList != null && p2pLoanRecordList.size() > 0) {
            for (P2PLoanRecordEntity p2pLoanRecord : p2pLoanRecordList) {
                p2pService.doFailedTarget(p2pLoanRecord.getTargetId(),p2pLoanRecord.getTargetUserId(),EP2PLoanStatus.FAIL);
            }
        }
        logger.info("运行流标任务结束 - -");
    }

    /**
     * 自动进行满标的任务
     * 对符合条件的标的合约，进行满标的操作
     * @throws Exception
     */
    public void autoFullTarget() throws Exception {
        // 获取各个参数
        Integer sysFullOnOff = null;    // 开关 0 开 1 关
        Integer sysCountDownHours = null;   // 倒计时差几小时
        Integer sysSurplusPer = null;    // 还差多少百分比
        Integer sysPeriodDay = null;    // 借贷标的的有效天数
        TsysParameterEntity sysEntity = this.sysParameterDao.getById("p2p_full_on_off");
        if (sysEntity == null) return;
        sysFullOnOff = Integer.valueOf(sysEntity.getParamValue());
        sysEntity = this.sysParameterDao.getById("p2p_full_count_down_hours");
        if (sysEntity == null) return;
        sysCountDownHours = Integer.valueOf(sysEntity.getParamValue());
        sysEntity = this.sysParameterDao.getById("p2p_full_surplus_per");
        if (sysEntity == null) return;
        sysSurplusPer = Integer.valueOf(sysEntity.getParamValue());
        sysEntity = this.sysParameterDao.getById("period_day");
        if (sysEntity == null) return;
        sysPeriodDay = Integer.valueOf(sysEntity.getParamValue());

        logger.info("开关 {}  倒计时设置 {}  差百分比 {} 有效天数 {}", sysFullOnOff, sysCountDownHours, sysSurplusPer, sysPeriodDay);

        // 获取是否进行自动满标的开关
        if (sysFullOnOff.equals(0)) return;
        // 获取当前所有的标的信息
        FP2PQueryPageLoanReq req = new FP2PQueryPageLoanReq();
        req.setTargetStatus(EP2PLoanStatus.INIT.getCode().byteValue());
        req.setLimit(1000);
        List<FP2PQueryPageLoanRes> loanList = this.p2pLoanRecordDao.queryFP2PQueryPageLoanResWithPage(req);
        if (loanList == null) return;
        for (FP2PQueryPageLoanRes l : loanList) {
            // 获取倒计时多长时间、满标差百分比
            // 还有多少小时 创建时间 + 倒计时时间 - 当前时间 / 3600秒
            Integer diffHours = BigMath.divide(DateUtil.addDays(l.getCreated(), sysPeriodDay).getTime() / 1000 - new Date().getTime() / 1000, 3600).intValue();
            // 还差多少百分比  100 - (总金额 / (财猫出资 + 当前投资) * 100)
            Integer diffPer = BigMath.subtract(100, BigMath.multiply((BigMath.divide((l.getCaimaoValue() + l.getActualValue()), l.getTargetAmount())), 100)).intValue();
            logger.info("标的 {} 还差 {} 小时 还差 {} 百分比", l.getTargetId(), diffHours, diffPer);
            if (sysCountDownHours > diffHours || sysSurplusPer > diffPer) {
                // 进行满标的操作
                logger.info("执行满标的操作");
                this.p2pService.doFullCaimaoP2PInvest(l.getTargetId());
            }
        }
    }

    /*
     * 自动展期
     * 每天更新一次。早上10:00
     */
    @Override
    public void autoExtTarget() throws Exception {
        //获取所有需要展期的记录
        List<P2PLoanRecordEntity> P2PLoanRecordList = p2pLoanRecordDao.getNeedExtTargetList();
        if(P2PLoanRecordList != null && P2PLoanRecordList.size() > 0){
            for (P2PLoanRecordEntity p2pLoanRecord : P2PLoanRecordList) {
                try{
                    p2pService.doAutoExtTarget(p2pLoanRecord.getTargetId());
                }catch(Exception e){
                    logger.error("P2P " + p2pLoanRecord.getTargetId() + ",自动展期失败, e", e);
                }
            }
        }
        logger.info("自动展期任务结束 0.0");
    }

    /*
     * 自动展期满标
     * 每1分钟更新一次
     */
    @Override
    public void extTargetFull() throws Exception {
        //获取所有满标的展期
        List<P2PLoanRecordEntity> P2PLoanRecordList = p2pLoanRecordDao.getExtTargetFullList();
        if(P2PLoanRecordList != null && P2PLoanRecordList.size() > 0){
            for (P2PLoanRecordEntity p2pLoanRecord : P2PLoanRecordList) {
                try{
                    p2pService.doAutoExtTargetFull(p2pLoanRecord.getTargetId());
                }catch(Exception e){
                    logger.error("P2P " + p2pLoanRecord.getTargetId() + ",满标后操作投资失败");
                }
            }
        }
        logger.info("自动展期满标任务结束 0.0");
    }

    /*
     * 查找所有没有合约编号的
     * 每1分钟更新一次
     */
    @Override
    public void searchP2PContractNo() throws Exception {
        //查找所有没有合约编号的
        List<TpzLoanApplyEntity> tpzLoanApplyList = loanService.getP2PContractNoNullList();
        if(tpzLoanApplyList != null && tpzLoanApplyList.size() > 0){
            for (TpzLoanApplyEntity tpzLoanApplyEntity:tpzLoanApplyList){
                try{
                    loanService.doSearchP2PContractNo(tpzLoanApplyEntity);
                }catch(Exception e){
                    logger.error("P2P apply " + tpzLoanApplyEntity.getOrderNo() + ",查找合约编号失败 e:", e);
                }
            }
        }
        logger.info("查找合约编号结束 0.0");
    }

    /*
     * 更新提前还款的标的
     * 每1分钟更新一次
     */
    @Override
    public void updateRepaymentTarget() throws Exception{
        //查找所有合约已经还款的
        List<P2PLoanRecordEntity> P2PLoanRecordList = p2pLoanRecordDao.getRepayContractList();
        if(P2PLoanRecordList != null && P2PLoanRecordList.size() > 0){
            for (P2PLoanRecordEntity p2PLoanRecordEntity:P2PLoanRecordList){
                try{
                    p2pService.updateIsRepayment(p2PLoanRecordEntity.getTargetId());
                }catch(Exception e){
                    logger.error("P2P is_repayment " + p2PLoanRecordEntity.getTargetId() + ",变更是否还款失败 e:", e);
                }
            }
        }
        logger.info("更新提前还款的标的结束 0.0");
    }

    /*
     * 清除合约下一次结息日
     * 每1分钟更新一次
     */
    @Override
    public void cleanP2PNextSettleInterestDate() throws Exception{
        try{
            loanService.cleanNextSettleInterestDate();
        }catch(Exception e){
            logger.error("清除合约下一次结息日失败 e:", e);
        }
        logger.info("清除合约下一次结息日失败结束 0.0");
    }
}

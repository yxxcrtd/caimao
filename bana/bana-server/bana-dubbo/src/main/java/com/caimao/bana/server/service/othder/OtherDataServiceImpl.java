package com.caimao.bana.server.service.othder;

import com.caimao.bana.api.entity.TpzHomsAccountLoanEntity;
import com.caimao.bana.api.entity.TpzLoanContractEntity;
import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.entity.res.FIndexPZRankingRes;
import com.caimao.bana.api.entity.res.FIndexRealtimePZRes;
import com.caimao.bana.api.service.IOtherDataService;
import com.caimao.bana.server.dao.CmOther.CmOtherActivityDao;
import com.caimao.bana.server.dao.homs.TpzHomsAccountLoanDao;
import com.caimao.bana.server.dao.other.OtherDataDao;
import com.caimao.bana.server.dao.p2p.P2PLoanRecordDao;
import com.caimao.bana.server.service.OperationServiceImpl;
import com.caimao.bana.server.service.p2p.P2PLoanRecordServiceHelper;
import com.caimao.bana.server.utils.ChannelUtil;
import com.caimao.bana.server.utils.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 其他需要的数据接口服务，乱七八糟的东西，无系统的东西 MD
 * Created by WangXu on 2015/5/28.
 */
@Service("otherDataService")
public class OtherDataServiceImpl implements IOtherDataService {

    private Logger logger = LoggerFactory.getLogger(OtherDataServiceImpl.class);

    @Autowired
    private OtherDataDao otherDataDao;
    @Resource
    private P2PLoanRecordDao P2PLoanRecordDao;
    @Autowired
    private CmOtherActivityDao cmOtherActivityDao;
    @Autowired
    private TpzHomsAccountLoanDao homsAccountLoanDao;
    @Autowired
    private P2PLoanRecordServiceHelper p2PLoanRecordServiceHelper;
    @Resource
    private OperationServiceImpl operationService;

    @Override
    public List<FIndexRealtimePZRes> indexRealtimePZList(int limit) throws Exception {
        List<FIndexRealtimePZRes> fIndexRealtimePZReses = this.otherDataDao.queryRealtimePZList(limit);
        for (int i = 0; i < fIndexRealtimePZReses.size(); i++) {
            // 隐藏手机号码
            fIndexRealtimePZReses.get(i).setMobile(ChannelUtil.hide(fIndexRealtimePZReses.get(i).getMobile(), 3, 3));
            fIndexRealtimePZReses.get(i).setApplyDatetime(DateUtil.formateDateStr(fIndexRealtimePZReses.get(i).getApplyDatetime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
        }
        return fIndexRealtimePZReses;
    }

    //首页融资排行数据
    @Override
    public List<FIndexPZRankingRes> indexPzRankingList(int limit) throws Exception {
        List<FIndexPZRankingRes> fIndexPZRankingReses = this.otherDataDao.queryPZRankingList(limit);
        for (int i = 0; i < fIndexPZRankingReses.size(); i++) {
            // 隐藏手机号码
            fIndexPZRankingReses.get(i).setMobile(ChannelUtil.hide(fIndexPZRankingReses.get(i).getMobile(), 3, 3));
        }
        return fIndexPZRankingReses;
    }

    // 首页首页显示的运营数据
    @Override
    public Map<String, Object> indexWebOperationInfo(Float ratio) throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 获取总的注册人数
        Integer totalRegCount = this.otherDataDao.getTotalRegisterUserCount();

        map.put("totalRegCount", totalRegCount);
        // 获取总的融资数量
        Long totalPzSum = this.otherDataDao.getTotalPzSum();

        map.put("totalPzSum", totalPzSum);
        // 获取总的投资数量
        Long totalInviestSum = this.otherDataDao.getTotalInviestSum();

        map.put("totalInviestSum", totalInviestSum);
        // 获取总的赚取收益
        Long totalInterestSum = this.otherDataDao.getTotalInterestSum();

        map.put("totalInterestSum", totalInterestSum);

        return map;
    }

    // 临时为了解决月配的不收利息的问题
    @Override
    public void prodLoanInterestFix() throws Exception {
        // 今天的日期
        Date jintianDate = new Date();
        // 明天的日期
        Date mingtianDate = DateUtil.addDays(jintianDate, 1);

        List<TpzLoanContractEntity> list = this.cmOtherActivityDao.queryLoanInterestList();
        this.logger.info("共查找有问题的融资合约数：{}", list.size());
        ConcurrentLinkedDeque<TpzLoanContractEntity> strQueue = new ConcurrentLinkedDeque<>();
        strQueue.addAll(list);
        while ( ! strQueue.isEmpty()) {
            TpzLoanContractEntity entity = strQueue.poll();

            // 跳过西藏信托的合约，不进行他们的结息
            TpzHomsAccountLoanEntity homsAccountLoanEntity = this.homsAccountLoanDao.getAccountByContractNo(entity.getContractNo());
            if (homsAccountLoanEntity != null) {
                if (homsAccountLoanEntity.getHomsFundAccount().equals("22550002")) {
                    continue;
                }
            } else {
                continue;
            }

//            // 计算这个合约与当前相差的天数
//            long times = new Date().getTime() - entity.getCreateDatetime().getTime();
//            Integer prodTerm = (int)(times / 60L / 60L / 1000L / 24L) + 1;
//            // 过滤掉 借贷融资的产品用户
//            if (entity.getProdId().equals(802234593968142L)) {
//                continue;
//            }
//            // 过滤掉指定的用户 李明剑
//            if (entity.getUserId().equals(800205188366337L)) {
//                continue;
//            }
            if (entity.getInterestSettleDays() != 30) {
                // 如果是日合约，设置为明天
                entity.setNextSettleInterestDate(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, mingtianDate));
                entity.setContractStopDatetime(mingtianDate);
                entity.setContractEndDate(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, mingtianDate));
                cmOtherActivityDao.updateLoanInterestMonth(entity);
                continue;
            }
            // 确认合约是否是P2P
            P2PLoanRecordEntity p2PLoanRecordEntity = this.p2PLoanRecordServiceHelper.getP2PLoanByContractNo(entity.getContractNo());
            // 获取展期次数
            Integer extCnt = 0;
            if (p2PLoanRecordEntity != null) extCnt = P2PLoanRecordDao.queryExtCnt(p2PLoanRecordEntity.getTargetId());
            if (extCnt == null) extCnt = 0;
            // 最后一次的结息日期
            if (Integer.parseInt(entity.getLastSettleInterestDate()) > Integer.parseInt(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, DateUtil.addDays(jintianDate, -30)))) {
                // 如果他是空的，进行补全   补的下次结息时间为 ： 合约的开始时间 + 结息周期，知道下次结息时间大于今天的日期
                Date nextSettleDate = DateUtil.convertStringToDate(DateUtil.DATE_FORMAT_STRING, entity.getContractBeginDate());
                if (nextSettleDate != null) {
                    while (nextSettleDate.getTime() < jintianDate.getTime()) {
                        nextSettleDate = DateUtil.addDays(nextSettleDate, entity.getInterestSettleDays());
                    }
                }
                // 如果是P2P的合约，并且没有进行展期的执行以下操作
                if (p2PLoanRecordEntity != null && extCnt == 0) {
                    this.logger.info("P2P 合约信息 " + ToStringBuilder.reflectionToString(p2PLoanRecordEntity));
                    // 用户借款几个月，就延期几个月进行下次结息
                    nextSettleDate = DateUtil.convertStringToDate(DateUtil.DATE_FORMAT_STRING, entity.getContractBeginDate());
                    Integer loopMonth = p2PLoanRecordEntity.getLiftTime() / 30;
                    this.logger.info("循环 " + loopMonth);
                    for (Integer i = 1; i <= loopMonth; i++) {
                        nextSettleDate = DateUtil.addDays(nextSettleDate, entity.getInterestSettleDays());
                    }
                }
                entity.setNextSettleInterestDate(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, nextSettleDate));
                this.cmOtherActivityDao.updateLoanInterestMonth(entity);
                this.logger.info("补全信息 -- 合约 {} ({}) 最后结息时间 {}, 更新下次结息时间为 {}",
                        entity.getContractNo(), entity.getUserRealName(), entity.getLastSettleInterestDate(), entity.getNextSettleInterestDate());
                continue;
            }

            this.logger.info("开始修复合约 {} {}", entity.getContractNo(), entity.getUserRealName());
            this.logger.info("最后一次计息的时间 {}", entity.getLastSettleInterestDate());
            this.logger.info("合约的结束时间 {}", entity.getContractEndDate());
            // 让那些没有计息的，计息一次，其他会自动进行修正

            // 如果是P2P配资的合约，并且他进行过展期了
            if (p2PLoanRecordEntity != null && extCnt != 0) {
                // 第一步，将这个时间更正为正确的时间
                // 如果他是空的，进行补全   补的下次结息时间为 ： 合约的开始时间 + 结息周期，直到下次结息时间大于今天的日期
                Date nextSettleDate = DateUtil.convertStringToDate(DateUtil.DATE_FORMAT_STRING, entity.getContractBeginDate());
                if (nextSettleDate != null) {
                    while (nextSettleDate.getTime() < jintianDate.getTime()) {
                        nextSettleDate = DateUtil.addDays(nextSettleDate, entity.getInterestSettleDays());
                    }
                }
                entity.setNextSettleInterestDate(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, nextSettleDate));
            } else {
                // 第一步，将 next_settle_interest_date 日期修改为 明天 ，格式：20150624
                entity.setNextSettleInterestDate(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, mingtianDate));
            }

            // 第二步，将 contract_stop_datetime 日期修改为 后天，格式 2015-06-24 12:00:00
            entity.setContractStopDatetime(mingtianDate);
            // 第三部，将 contract_end_date 日期修改为 后天，格式 20150723
            entity.setContractEndDate(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, mingtianDate));
            // 打LOG
            this.logger.info("合约ID {} ({}) 修改下次结息时间 {} 合约到期时间 {} ({})",
                    entity.getContractNo(), entity.getUserRealName(), entity.getNextSettleInterestDate(), entity.getContractEndDate(), entity.getContractStopDatetime());
            // 第四步，更新记录
            cmOtherActivityDao.updateLoanInterestMonth(entity);
        }
        this.logger.info("执行完毕");
    }

    // 结息配资利息的报警通知
    @Override
    public void settlePZInterestNotice() throws Exception {
        // 今天的日期
        Date jintianDate = new Date();
        String jintianStr = DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, jintianDate);
        List<TpzLoanContractEntity> loanContractEntityList = this.cmOtherActivityDao.queryNextSettleLoanList(jintianStr);
        if (loanContractEntityList.size() > 0) {
            String alarmKey = "settle_error";
            String subject = jintianStr + "有用户未进行结息操作";
            String content = subject + ", 共 " + loanContractEntityList.size() + " 笔记录";
            try {
                this.operationService.addAlarmTask(alarmKey, subject, content);
            } catch (Exception ignored) {}
        }
    }
}

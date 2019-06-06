/*
*PromotionPointsUtils.java
*Created on 2015/6/15 14:33
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.jobs.utils;

/**
 * @author Administrator
 * @version 1.0.1
 */

import com.caimao.bana.api.entity.InviteInfoEntity;
import com.caimao.bana.api.entity.InviteReturnHistoryEntity;
import com.caimao.bana.api.entity.TpzAccruedInterestBillEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.enums.AddScoreType;
import com.caimao.bana.api.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class PromotionPointsUtils {

    private static final Logger logger = LoggerFactory.getLogger(PromotionPointsUtils.class);

    private static final String SCORE_BETWEEN_LOAN_REGIST = "score_between_loan_regist";

    private static final SimpleDateFormat LOGGER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat WORK_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    @Resource
    private IUserService userService;

    @Resource
    private TpzAccruedInterestBillService tpzAccruedInterestBillService;

    @Resource
    private SysParameterService sysParameterService;

    @Resource
    private InviteInfoService inviteInfoService;

    @Resource
    private InviteReturnHistoryService inviteReturnHistoryService;

    public void addScore(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            String workDate = WORK_DATE_FORMAT.format(calendar.getTime());
            logger.info("脚本运行扣息日期:workdate={}", workDate);
            int workDateToDayOfYears = calendar.get(Calendar.DAY_OF_YEAR);

            // #{workDate}结息记录
            List<TpzAccruedInterestBillEntity> tpzAccruedInterestBillEntityList = tpzAccruedInterestBillService.queryBillListByWorkDate(workDate);
            logger.info("日期:{}共查询到结息记录{}条", workDate, tpzAccruedInterestBillEntityList.size());

            LinkedBlockingQueue<TpzAccruedInterestBillEntity> tpzAccruedInterestBillEntityQueue = new LinkedBlockingQueue<>(tpzAccruedInterestBillEntityList);

            logger.info("返佣增加积分开始, 时间:{}", LOGGER_DATE_FORMAT.format(new Date()));
            Map<Long, String> totalPzAmountMap = addAccruedInterestScore(tpzAccruedInterestBillEntityQueue, workDateToDayOfYears);
            logger.info("返佣增加积分完成, 时间:{}", LOGGER_DATE_FORMAT.format(new Date()));

            logger.info("加30积分开始, 共{}条记录", totalPzAmountMap.size());
            addInvite30Points(totalPzAmountMap);
            logger.info("增加30积分完成, 时间:{}", LOGGER_DATE_FORMAT.format(new Date()));
        } catch (Exception e) {
            logger.error("返佣脚本运行出现错误, 请检查. 错误信息:{}", e);
        }
    }

    private Map<Long, String> addAccruedInterestScore(LinkedBlockingQueue<TpzAccruedInterestBillEntity> tpzAccruedInterestBillEntityQueue, Integer workDateToDayOfYears) {
        // 融资加积分间隔注册时最大值
        String max_between = sysParameterService.getSysparameterById(SCORE_BETWEEN_LOAN_REGIST).getParamValue();
        logger.info("参数score_between_loan_regist={}", max_between);

        TpzUserEntity userEntity = null; // 融资用户
        InviteInfoEntity inviteInfoEntity = null; // 邀请人返积分信息
        BigDecimal score = BigDecimal.ZERO; // 邀请用户要增加的积分
        InviteReturnHistoryEntity inviteReturnHistoryEntity = null; // 返佣记录
        TpzAccruedInterestBillEntity tpzAccruedInterestBillEntity = null; // 扣息记录

        Map<Long, String> totalPzAmountMap = new HashMap<>();
        while ((tpzAccruedInterestBillEntity = tpzAccruedInterestBillEntityQueue.poll()) != null && tpzAccruedInterestBillEntity.getAddScoreTimes() < 4) {
            // 计算积分
            try {
                userEntity = userService.getById(tpzAccruedInterestBillEntity.getUserId());
                if (userEntity.getRefUserId() == null || userEntity.getRefUserId() == 0) {
                    logger.warn("结息记录{}对应的融资用户无邀请用户, continue", tpzAccruedInterestBillEntity.getOrderNo());
                    continue;
                }

                // 查看该计息用户是否存在增加 AddScoreType.INVITE_RETURN 积分的记录
                List<InviteReturnHistoryEntity> inviteReturnHistoryEntities
                        = inviteReturnHistoryService.getInviteReturnHistoryByInviteUserIdAndReturnType(tpzAccruedInterestBillEntity.getUserId(), AddScoreType.INVITE_RETURN.getValue());
                if (CollectionUtils.isEmpty(inviteReturnHistoryEntities) && !totalPzAmountMap.containsKey(tpzAccruedInterestBillEntity.getUserId())) {
                    //获取融资金额
                    Long pzAmount = inviteInfoService.getUserPzAmount(tpzAccruedInterestBillEntity.getUserId());
                    if (pzAmount >= 1000000) {
                        totalPzAmountMap.put(tpzAccruedInterestBillEntity.getUserId(), userEntity.getRefUserId().toString());
                    }
                }

                // 根据结息记录和积分类型确认该结息记录是否已经进行增加积分的流程
                inviteReturnHistoryEntities
                        = inviteReturnHistoryService.getInviteReturnHistoryByBillOrderNoAndReturnType(tpzAccruedInterestBillEntity.getOrderNo(), AddScoreType.COMMISSION_RETURN.getValue());
                if (CollectionUtils.isNotEmpty(inviteReturnHistoryEntities)) {
                    logger.warn("结息记录{}已经为邀请用户增加积分, continue", tpzAccruedInterestBillEntity.getOrderNo());
                    continue;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(userEntity.getRegisterDatetime());
                int registerDays = calendar.get(Calendar.DAY_OF_YEAR);
                if (workDateToDayOfYears - registerDays > Integer.valueOf(max_between)) {
                    logger.warn("邀请用户已过{}天, 不再加积分", max_between);
                    continue;
                }

                inviteInfoEntity = inviteInfoService.getInviteInfo(userEntity.getRefUserId());
                // 积分=扣息(00) / 100 * 返佣比率;
                score = new BigDecimal(tpzAccruedInterestBillEntity.getOrderAmount().longValue())
                        .divide(new BigDecimal("100"), 0, BigDecimal.ROUND_DOWN)
                        .multiply(inviteInfoEntity.getCommissionRate());
                if (score.compareTo(BigDecimal.ZERO) > 0) {
                    inviteReturnHistoryEntity = new InviteReturnHistoryEntity();
                    inviteReturnHistoryEntity.setUserId(userEntity.getRefUserId());
                    inviteReturnHistoryEntity.setInviteUserId(userEntity.getUserId());
                    inviteReturnHistoryEntity.setReturnType(AddScoreType.COMMISSION_RETURN.getValue());
                    inviteReturnHistoryEntity.setOrderNo(tpzAccruedInterestBillEntity.getOrderNo());
                    inviteReturnHistoryEntity.setOrderAmount(tpzAccruedInterestBillEntity.getOrderAmount());
                    inviteReturnHistoryEntity.setReturnRate(inviteInfoEntity.getCommissionRate());
                    inviteReturnHistoryEntity.setReturnAmount(score);

                    userService.addScore(userEntity.getRefUserId(), score.intValue(), AddScoreType.COMMISSION_RETURN, inviteReturnHistoryEntity);
                    logger.info("结息记录{}为用户{}增加{}积分", tpzAccruedInterestBillEntity.getOrderNo(), userEntity.getRefUserId(), score.intValue());
                } else {
                    logger.warn("结息记录{}积分不足一分", tpzAccruedInterestBillEntity.getOrderNo());
                }
            } catch (Exception e) {
                logger.error("结息记录{}为用户{}加积分失败, 次数:{}, 错误信息:{}",
                        tpzAccruedInterestBillEntity.getOrderNo(), tpzAccruedInterestBillEntity.getAddScoreTimes(), userEntity != null ? userEntity.getRefUserId() : null, e);
                // 重试三次
                tpzAccruedInterestBillEntity.setAddScoreTimes(tpzAccruedInterestBillEntity.getAddScoreTimes() + 1);
                tpzAccruedInterestBillEntityQueue.offer(tpzAccruedInterestBillEntity);
            }
        }
        return totalPzAmountMap;
    }

    private void addInvite30Points(Map<Long, String> totalPzAmountMap) {
        InviteReturnHistoryEntity inviteReturnHistoryEntity = null; // 返佣记录
        for (Long aLong : totalPzAmountMap.keySet()) {
            try {
                inviteReturnHistoryEntity = new InviteReturnHistoryEntity();
                inviteReturnHistoryEntity.setUserId(Long.valueOf(totalPzAmountMap.get(aLong)));
                inviteReturnHistoryEntity.setInviteUserId(aLong);
                inviteReturnHistoryEntity.setReturnType(AddScoreType.INVITE_RETURN.getValue());
                inviteReturnHistoryEntity.setOrderNo(0L);
                inviteReturnHistoryEntity.setOrderAmount(0L);
                inviteReturnHistoryEntity.setReturnRate(BigDecimal.ZERO);
                inviteReturnHistoryEntity.setReturnAmount(new BigDecimal(30));

                userService.addScore(Long.valueOf(totalPzAmountMap.get(aLong)), 30, AddScoreType.INVITE_RETURN, inviteReturnHistoryEntity);
                logger.info("用户{}融资上万, 为邀请者{}增加30积分", aLong, totalPzAmountMap.get(aLong).toString());
            } catch (Exception e) {
                logger.error("注册用户融资超过1万加30积分发生错误, 请检查. error:{}", e);
                continue;
            }
        }
    }

}

package com.caimao.bana.server.service.inviteInfo;

import com.caimao.bana.api.entity.InviteInfoEntity;
import com.caimao.bana.api.service.InviteInfoService;
import com.caimao.bana.server.dao.inviteInfo.InviteInfoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("inviteInfoService")
public class InviteInfoServiceImpl implements InviteInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InviteInfoServiceImpl.class);

    @Resource
    InviteInfoDao inviteInfoDao;

    /**
     * 更新返佣信息任务
     *
     * @throws Exception
     */
    @Override
    public void updateInviteInfoTask() throws Exception {
        //获取注册人数
        List<Map<String, Object>> regCountList = inviteInfoDao.getRegCount();

        if (regCountList != null) {
            for (Map<String, Object> aRegCount : regCountList) {
                try {
                    if (Long.valueOf(aRegCount.get("refUserId").toString()) == 0) continue;

                    Long userId = Long.valueOf(aRegCount.get("refUserId").toString());
                    Integer regCount = Integer.valueOf(aRegCount.get("cnt").toString());

                    //获取融资人数
                    Integer PZCount = inviteInfoDao.getPZCount(userId);

                    //获取融资金额
                    Long PZAmountDay = inviteInfoDao.getPZAmountDay(userId);
                    Long PZAmountDayHis = inviteInfoDao.getPZAmountDayHis(userId);
                    Long PZAmountMonth = inviteInfoDao.getPZAmountMonth(userId);
                    Long PZAmountMonthHis = inviteInfoDao.getPZAmountMonthHis(userId);

                    Long totalAmount = 0L;
                    if (PZAmountDay != null) totalAmount += PZAmountDay;
                    if (PZAmountDayHis != null) totalAmount += PZAmountDayHis;
                    if (PZAmountMonth != null) totalAmount += PZAmountMonth;
                    if (PZAmountMonthHis != null) totalAmount += PZAmountMonthHis;

                    //获取产生利息
                    Long interest = inviteInfoDao.getInterest(userId);
                    if (interest == null) interest = 0L;

                    //计算佣金等级
                    HashMap<String, Object> hashMap = getUserInviteLevel(totalAmount, PZCount);

                    //更新邀请信息
                    Map<String, Object> updateParamMap = new HashMap<>();

                    updateParamMap.put("regCnt", regCount);
                    updateParamMap.put("pzCnt", PZCount);
                    updateParamMap.put("pzAmount", totalAmount);
                    updateParamMap.put("interestAmount", interest);
                    updateParamMap.put("commissionLevel", Integer.parseInt(hashMap.get("commissionLevel").toString()));
                    updateParamMap.put("commissionRate", new BigDecimal(hashMap.get("commissionRate").toString()));
                    updateParamMap.put("updated", System.currentTimeMillis() / 1000);

                    updateInviteInfo(userId, updateParamMap);

                    //LOGGER.info("用户 " + userId + " 更新成功");
                } catch (Exception e) {
                    LOGGER.error("更新用户邀请数据失败", e);
                }
            }
        }
    }

    /**
     * 获取用户返佣信息
     *
     * @param userId 用户userId
     * @return
     * @throws Exception
     */
    @Override
    public InviteInfoEntity getInviteInfo(Long userId) throws Exception {
        return inviteInfoDao.getInviteInfo(userId);
    }

    /**
     * 获取用户返佣排行
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> getBrokerageRankCnt() throws Exception {
        return inviteInfoDao.getBrokerageRankCnt();
    }

    /**
     * 获取用户返佣排行
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> getBrokerageRankMoney() throws Exception {
        return inviteInfoDao.getBrokerageRankMoney();
    }

    /**
     * 获取融资总额
     *
     * @param userId 用户userId
     * @return
     * @throws Exception
     */
    @Override
    public Long getUserPzAmount(Long userId) throws Exception {
        //获取融资金额
        Long PZAmountDay = inviteInfoDao.getPZAmountDayByUserId(userId);
        Long PZAmountDayHis = inviteInfoDao.getPZAmountDayHisByUserId(userId);
        Long PZAmountMonth = inviteInfoDao.getPZAmountMonthByUserId(userId);
        Long PZAmountMonthHis = inviteInfoDao.getPZAmountMonthHisByUserId(userId);

        Long totalAmount = 0L;
        if (PZAmountDay != null) totalAmount += PZAmountDay;
        if (PZAmountDayHis != null) totalAmount += PZAmountDayHis;
        if (PZAmountMonth != null) totalAmount += PZAmountMonth;
        if (PZAmountMonthHis != null) totalAmount += PZAmountMonthHis;
        return totalAmount;
    }

    /**
     * 更新邀请信息
     *
     * @param userId
     * @param updateInfo
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    private Integer updateInviteInfo(Long userId, Map<String, Object> updateInfo) throws Exception {
        //锁定记录
        InviteInfoEntity inviteInfo = inviteInfoDao.getInviteInfoForUpdate(userId);
        //更新或插入数据
        if (inviteInfo == null) {
            updateInfo.put("userId", userId);
            return inviteInfoDao.insertInviteInfo(updateInfo);
        } else {
            return inviteInfoDao.updateInviteInfo(userId, updateInfo);
        }
    }

    /**
     * 计算等级信息
     *
     * @param pzAmount
     * @param pzCnt
     * @return
     * @throws Exception
     */
    private HashMap<String, Object> getUserInviteLevel(Long pzAmount, Integer pzCnt) throws Exception {
        HashMap<String, Object> hashMap = new HashMap();
        if (pzAmount >= 10000000000L && pzCnt >= 50) {
            hashMap.put("commissionLevel", 5);
            hashMap.put("commissionRate", new BigDecimal("0.12"));
        } else if (pzAmount >= 3000000000L && pzCnt >= 30) {
            hashMap.put("commissionLevel", 4);
            hashMap.put("commissionRate", new BigDecimal("0.1"));
        } else if (pzAmount >= 1000000000L && pzCnt >= 20) {
            hashMap.put("commissionLevel", 3);
            hashMap.put("commissionRate", new BigDecimal("0.08"));
        } else if (pzAmount >= 100000000L && pzCnt >= 5) {
            hashMap.put("commissionLevel", 2);
            hashMap.put("commissionRate", new BigDecimal("0.07"));
        } else if (pzAmount >= 30000000L && pzCnt >= 3) {
            hashMap.put("commissionLevel", 1);
            hashMap.put("commissionRate", new BigDecimal("0.05"));
        } else {
            hashMap.put("commissionLevel", 0);
            hashMap.put("commissionRate", new BigDecimal("0.03"));
        }
        return hashMap;
    }
}

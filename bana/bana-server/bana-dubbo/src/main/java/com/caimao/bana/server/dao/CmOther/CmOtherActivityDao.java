package com.caimao.bana.server.dao.CmOther;

import com.caimao.bana.api.entity.CmOtherActivityEntity;
import com.caimao.bana.api.entity.TpzLoanContractEntity;
import com.caimao.bana.api.entity.other.OTReturnDayInterestEntity;
import com.caimao.bana.api.entity.res.other.FOtherP2PReturnFeeRes;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财猫活动前3天免费的活动
 * Created by WangXu on 2015/5/13.
 */
@Repository
public class CmOtherActivityDao extends SqlSessionDaoSupport {

    /**
     * 查询按日融资符合条件的合约记录
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return  列表
     */
    public List<CmOtherActivityEntity> queryDaysContract(String beginDate, String endDate) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("beginDate", beginDate);
        queryMap.put("endDate", endDate);
        return getSqlSession().selectList("CmOther.queryDaysContract", queryMap);
    }

    /**
     * 查询按月融资符合条件的合约记录
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return  列表
     */
    public List<CmOtherActivityEntity> queryMonthsContract(String beginDate, String endDate) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("beginDate", beginDate);
        queryMap.put("endDate", endDate);
        return getSqlSession().selectList("CmOther.queryMonthsContract", queryMap);
    }

    public List<CmOtherActivityEntity> queryOldDaysContract(Long userId, String beginDate, String endDate) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", userId);
        queryMap.put("beginDate", beginDate);
        queryMap.put("endDate", endDate);
        return getSqlSession().selectList("CmOther.queryOldDaysContract", queryMap);
    }
    public List<CmOtherActivityEntity> queryOldMonthsContract(Long userId, String beginDate, String endDate) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", userId);
        queryMap.put("beginDate", beginDate);
        queryMap.put("endDate", endDate);
        return getSqlSession().selectList("CmOther.queryOldMonthsContract", queryMap);
    }
    public List<CmOtherActivityEntity> queryOldHisDaysContract(Long userId, String beginDate, String endDate) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", userId);
        queryMap.put("beginDate", beginDate);
        queryMap.put("endDate", endDate);
        return getSqlSession().selectList("CmOther.queryOldHisDaysContract", queryMap);
    }
    public List<CmOtherActivityEntity> queryOldHisMonthsContract(Long userId, String beginDate, String endDate) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", userId);
        queryMap.put("beginDate", beginDate);
        queryMap.put("endDate", endDate);
        return getSqlSession().selectList("CmOther.queryOldHisMonthsContract", queryMap);
    }


    // 查询P2P融资首次充值的活动数据
    public List<FOtherP2PReturnFeeRes> queryP2PReturnFeeList() {
        return getSqlSession().selectList("CmOther.queryP2PReturnFeeList");
    }

    /**
     * 融资利息有问题的记录查询  自动展期的问题
     * @return
     */
    public List<TpzLoanContractEntity> queryLoanInterestList() {
        return getSqlSession().selectList("CmOther.queryLoanInterestList");
    }

    /**
     * 查询指定结息日期的配资记录列表
     * @param settleDate
     * @return
     */
    public List<TpzLoanContractEntity> queryNextSettleLoanList(String settleDate) {
        return this.getSqlSession().selectList("CmOther.queryNextSettleLoanList", settleDate);
    }

    public void updateLoanInterestMonth(TpzLoanContractEntity entity) {
        getSqlSession().update("CmOther.updateLoanInterestMonth", entity);
    }

    // 返回多收取的按日融资的利息
    public List<OTReturnDayInterestEntity> queryDayInterestList(String workDate) {
        return getSqlSession().selectList("CmOther.queryDayInterestList", workDate);
    }
}

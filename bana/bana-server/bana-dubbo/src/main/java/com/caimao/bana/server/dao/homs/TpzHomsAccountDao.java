package com.caimao.bana.server.dao.homs;

import com.caimao.bana.api.entity.TpzHomsAccountEntity;
import com.caimao.bana.api.entity.TpzHomsAccountJourEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * homs主账户列表
 */
@Repository("tpzHomsAccountDao")
public class TpzHomsAccountDao extends SqlSessionDaoSupport {

    public List<TpzHomsAccountEntity> queryHomsAccount() throws Exception {
        return getSqlSession().selectList("TpzHomsAccount.queryHomsAccount");
    }

    public List<TpzHomsAccountJourEntity> queryAbnormalBizTypeAccountJour(Date dateStart, EAccountBizType eAccountBizType) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("dateStart", dateStart);
        params.put("accountBizType", eAccountBizType.getCode());
        return getSqlSession().selectList("TpzHomsAccount.queryAbnormalBizTypeAccountJour", params);
    }

    /**
     * 根据指定的开始时间，查询有异常的HOMS资金流水记录
     * @param dateStart
     * @return
     * @throws Exception
     */
    public List<TpzHomsAccountJourEntity> queryAbnormalAccountJour(Date dateStart) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("dateStart", dateStart);
        return getSqlSession().selectList("TpzHomsAccount.queryAbnormalAccountJour", params);
    }

    public TpzHomsAccountJourEntity queryRecordByContractInfo(Long userId, Long transAmount, Date dateStart) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("transAmount", transAmount);
        params.put("dateStart", dateStart);
        return getSqlSession().selectOne("TpzHomsAccount.queryRecordByContractInfo", params);
    }
}
package com.caimao.bana.server.dao.userBankCardDao;

import com.caimao.bana.api.entity.TpzBankTypeEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangXu on 2015/5/14.
 */
@Repository
public class TpzBankTypeDao extends SqlSessionDaoSupport {

    /**
     * 获取银行信息，根据银行代码与渠道
     * @param bankNo    银行代码
     * @param channelId 渠道ID
     * @return  银行信息
     */
    public TpzBankTypeEntity getBankInfo(String bankNo, Long channelId) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("bankNo", bankNo);
        queryMap.put("channelId", channelId);
        return getSqlSession().selectOne("TpzBankType.getBankInfo", queryMap);
    }

    /**
     * 根据NO获取银行信息
     * @param bankNo
     * @return
     */
    public TpzBankTypeEntity getById(String bankNo) {
        return getSqlSession().selectOne("TpzBankType.getById", bankNo);
    }

    public List<TpzBankTypeEntity> queryBankList(Long channelId) {
        return getSqlSession().selectList("TpzBankType.queryBankList", channelId);
    }
}

package com.caimao.bana.server.dao.depositDao;

import com.caimao.bana.api.entity.AlipayRecordEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 支付宝交易记录表
 */
@Repository
public class AlipayRecordDao extends SqlSessionDaoSupport {
    /**
     * 保存交易记录
     * @param alipayRecordEntity 实体类
     */
    public void saveTradeRecord(AlipayRecordEntity alipayRecordEntity) throws Exception{
        getSqlSession().insert("AlipayRecord.saveTradeRecord", alipayRecordEntity);
    }

    /**
     * 根据交易号查询记录
     * @param tradeId 交易号
     * @return 实体类
     * @throws Exception
     */
    public AlipayRecordEntity queryTradeRecord(BigDecimal tradeId) throws Exception{
        return getSqlSession().selectOne("AlipayRecord.queryTradeRecord", tradeId);
    }

    /**
     * 根据交易号查询记录
     * @param tradeId 交易号
     * @return 实体类
     * @throws Exception
     */
    public AlipayRecordEntity queryTradeRecordForUpdate(BigDecimal tradeId) throws Exception{
        return getSqlSession().selectOne("AlipayRecord.queryTradeRecordForUpdate", tradeId);
    }

    /**
     * 更新交易记录
     * @param tradeId 交易号
     * @param relNo 关联id
     * @throws Exception
     */
    public void updateTradeRecord(BigDecimal tradeId, Long relNo) throws Exception{
        HashMap<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("tradeId", tradeId);
        updateInfo.put("relNo", relNo);
        getSqlSession().update("AlipayRecord.updateTradeRecord", updateInfo);
    }
}
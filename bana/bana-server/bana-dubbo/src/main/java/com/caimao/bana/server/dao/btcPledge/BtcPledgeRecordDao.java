package com.caimao.bana.server.dao.btcPledge;

import com.caimao.bana.api.entity.BanaBtcPledgeRecordEntity;
import com.caimao.bana.api.entity.TpzAdjustOrderEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 比特币抵押
 */
@Repository("btcPledgeRecordDao")
public class BtcPledgeRecordDao extends SqlSessionDaoSupport {

    /**
     * 保存订单信息
     * @param banaBtcPledgeRecordEntity 实体类
     */
    public void save(BanaBtcPledgeRecordEntity banaBtcPledgeRecordEntity) {
        getSqlSession().insert("BtcPledgeRecord.save", banaBtcPledgeRecordEntity);
    }
}

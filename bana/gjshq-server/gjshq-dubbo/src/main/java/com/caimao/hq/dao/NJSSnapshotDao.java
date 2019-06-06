package com.caimao.hq.dao;

import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.entity.TradeAmountReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *行情数据管理
 * Created by WangXu on 2015/4/23.
 */
@Service("njsSnapshotDao")
public class NJSSnapshotDao extends SqlSessionDaoSupport {


    /**
     * 保存实时数据
     * @param
     * @return  返回值
     */
    public int insert(Snapshot snapshot) {
        return getSqlSession().insert("NJS_Snapshot.insert", snapshot);
    }
    public List<Snapshot> selectNew(String financeMic) {
        return getSqlSession().selectList("NJS_Snapshot.selectNew", financeMic);
    }

    public List<Snapshot> tradeAmountQueryHistory(TradeAmountReq tradeAmountReq){

        return getSqlSession().selectList("NJS_Snapshot.tradeAmountQueryHistory", tradeAmountReq);
    }

}

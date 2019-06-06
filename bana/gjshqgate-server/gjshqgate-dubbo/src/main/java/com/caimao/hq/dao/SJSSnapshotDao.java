package com.caimao.hq.dao;

import com.caimao.hq.api.entity.SJSSnapshot;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.entity.TradeAmountReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *行情数据管理
 * Created by WangXu on 2015/4/23.
 */
@Service("sjsSnapshotDao")
public class SJSSnapshotDao extends SqlSessionDaoSupport {


    /**
     * 保存实时数据
     * @param
     * @return  返回值
     */
    public int insert(Snapshot snapshot) {
        return getSqlSession().insert("SJS_Snapshot.insert", snapshot);
    }

    public int insertBatch(List<SJSSnapshot> snapshotList) {
        return getSqlSession().insert("SJS_Snapshot.insertBatch", snapshotList);
    }

    public List<Snapshot> selectNew(String financeMic) {
        return getSqlSession().selectList("SJS_Snapshot.selectNew", financeMic);
    }

    public List<Snapshot> tradeAmountQueryHistory(TradeAmountReq tradeAmountReq){

        return getSqlSession().selectList("SJS_Snapshot.tradeAmountQueryHistory", tradeAmountReq);
    }
    /**
     * 查询有数据的最近5日
     * */
    public List<String> selectDateNear5(TradeAmountReq tradeAmountReq){

        return getSqlSession().selectList("SJS_Snapshot.selectDateNear5", tradeAmountReq);
    }

    public List<Snapshot> query(Snapshot snapshot){

        return getSqlSession().selectList("SJS_Snapshot.query", snapshot);
    }
}

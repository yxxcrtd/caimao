package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.history.GjsNJSHistoryTransferEntity;
import com.caimao.gjs.api.entity.req.FQueryHistoryTransferReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SJS历史出入金DAO
 */
@Repository
public class GJSNJSHistoryTransferDao extends SqlSessionDaoSupport {

    /**
     * 插入SJS历史出入金
     *
     * @param entity
     * @return
     */
    public Integer insertNJSHistoryTransfer(GjsNJSHistoryTransferEntity entity) {
        return this.getSqlSession().insert("GjsNJSHistoryTransfer.insert", entity);
    }

    /**
     * 根据开始时间和结束时间查询NJS历史出入金列表
     *
     * @param req
     * @return
     */
    public List<GjsNJSHistoryTransferEntity> queryNJSHistoryTransferWithPage(FQueryHistoryTransferReq req) {
        return this.getSqlSession().selectList("GjsNJSHistoryTransfer.queryNJSHistoryTransferWithPage", req);
    }

    /**
     * 根据日期查询历史出入金
     *
     * @param date
     * @return
     */
    public List<GjsNJSHistoryTransferEntity> queryNJSHistoryTransferListByDate(String date) {
        return this.getSqlSession().selectList("GjsNJSHistoryTransfer.queryNJSHistoryTransferListByDate", date);
    }

    /**
     * 根据TraderId查询NJS历史出入金列表
     *
     * @param traderId
     * @return
     */
    public List<GjsNJSHistoryTransferEntity> queryNJSHistoryTransferListByTraderIdForManage(String traderId) {
        return this.getSqlSession().selectList("GjsNJSHistoryTransfer.queryNJSHistoryTransferListByTraderIdForManage", traderId);
    }

    /**
     * NJS累计出入金金额
     *
     * @param type
     * @return
     */
    public Long getNJSHistoryTransferTotalMoneySum(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        return this.getSqlSession().selectOne("GjsNJSHistoryTransfer.getNJSHistoryTransferTotalMoneySum", params);
    }

    /**
     * NJS累计出入金次数
     *
     * @param type
     * @return
     */
    public Long getNJSHistoryTransferCount(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        return this.getSqlSession().selectOne("GjsNJSHistoryTransfer.getNJSHistoryTransferCount", params);
    }

}

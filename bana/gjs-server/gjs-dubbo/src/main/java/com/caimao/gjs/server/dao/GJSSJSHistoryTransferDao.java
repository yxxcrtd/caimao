package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.history.GjsSJSHistoryEntrustEntity;
import com.caimao.gjs.api.entity.history.GjsSJSHistoryTransferEntity;
import com.caimao.gjs.api.entity.req.FQueryHistoryTransferReq;
import com.caimao.gjs.api.entity.res.FQueryHistoryTransferRes;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SJS历史出入金DAO
 */
@Repository
public class GJSSJSHistoryTransferDao extends SqlSessionDaoSupport {

    /**
     * 插入SJS历史出入金
     *
     * @param entity
     * @return
     */
    public Integer insertSJSHistoryTransfer(GjsSJSHistoryTransferEntity entity) {
        return this.getSqlSession().insert("GjsSJSHistoryTransfer.insert", entity);
    }

    /**
     * 根据开始时间和结束时间查询SJS历史出入金列表
     *
     * @param req
     * @return
     */
    public List<GjsSJSHistoryTransferEntity> querySJSHistoryTransferList(FQueryHistoryTransferReq req) {
        return this.getSqlSession().selectList("GjsSJSHistoryTransfer.querySJSHistoryTransferWithPage", req);
    }

    /**
     * 根据TraderId查询SJS历史出入金列表
     *
     * @param traderId
     * @return
     */
    public List<GjsSJSHistoryTransferEntity> querySJSHistoryTransferListByTraderIdForManage(String traderId) {
        return this.getSqlSession().selectList("GjsSJSHistoryTransfer.querySJSHistoryTransferListByTraderIdForManage", traderId);
    }

    /**
     * SJS累计出入金金额
     *
     * @param type
     * @return
     */
    public Long getSJSHistoryTransferTotalMoneySum(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        return this.getSqlSession().selectOne("GjsSJSHistoryTransfer.getSJSHistoryTransferTotalMoneySum", params);
    }

    /**
     * SJS累计出入金次数
     *
     * @param type
     * @return
     */
    public Long getSJSHistoryTransferCount(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        return this.getSqlSession().selectOne("GjsSJSHistoryTransfer.getSJSHistoryTransferCount", params);
    }

}

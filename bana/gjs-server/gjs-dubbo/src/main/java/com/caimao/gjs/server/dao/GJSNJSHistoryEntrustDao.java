package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.history.GjsNJSHistoryEntrustEntity;
import com.caimao.gjs.api.entity.history.GjsNJSHistoryTradeEntity;
import com.caimao.gjs.api.entity.history.GjsNJSHistoryTransferEntity;
import com.caimao.gjs.api.entity.req.FQueryHistoryEntrustReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NJS历史委托DAO
 */
@Repository
public class GJSNJSHistoryEntrustDao extends SqlSessionDaoSupport {

    /**
     * 插入NJS历史委托
     *
     * @param entity
     * @return
     */
    public Integer insertNJSHistoryEntrust(GjsNJSHistoryEntrustEntity entity) {
        return this.getSqlSession().insert("GjsNJSHistoryEntrust.insert", entity);
    }

    /**
     * 根据开始时间和结束时间查询NJS历史委托列表
     *
     * @param req
     * @return
     */
    public List<GjsNJSHistoryEntrustEntity> queryNJSHistoryEntrustList(FQueryHistoryEntrustReq req) {
        return this.getSqlSession().selectList("GjsNJSHistoryEntrust.queryNJSHistoryEntrustWithPage", req);
    }

    /**
     * 根据日期查询历史委托记录
     *
     * @param date
     * @return
     */
    public List<GjsNJSHistoryEntrustEntity> queryNJSHistoryEntrustListByDate(String date) {
        return this.getSqlSession().selectList("GjsNJSHistoryEntrust.queryNJSHistoryEntrustListByDate", date);
    }

    /**
     * 根据TraderId查询NJS历史委托列表
     *
     * @param traderId
     * @return
     */
    public List<GjsNJSHistoryEntrustEntity> queryNJSHistoryEntrustListByTraderIdForManage(String traderId) {
        return this.getSqlSession().selectList("GjsNJSHistoryEntrust.queryNJSHistoryEntrustListByTraderIdForManage", traderId);
    }

}

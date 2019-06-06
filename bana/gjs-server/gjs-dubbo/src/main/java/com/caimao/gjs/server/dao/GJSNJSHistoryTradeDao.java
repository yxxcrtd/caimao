package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.history.GjsNJSHistoryEntrustEntity;
import com.caimao.gjs.api.entity.history.GjsNJSHistoryTradeEntity;
import com.caimao.gjs.api.entity.req.FQueryHistoryTradeReq;
import com.caimao.gjs.api.entity.res.FQueryHistoryTradeRes;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NJS历史交易DAO
 */
@Repository
public class GJSNJSHistoryTradeDao extends SqlSessionDaoSupport {

    /**
     * 根据开始时间和结束时间查询NJS历史交易列表
     *
     * @param req
     * @return
     */
    public List<GjsNJSHistoryTradeEntity> queryNJSHistoryTradeWithPage(FQueryHistoryTradeReq req) {
        return this.getSqlSession().selectList("GjsNJSHistoryTrade.queryNJSHistoryTradeWithPage", req);
    }

    /**
     * 插入NJS历史交易
     *
     * @param entity
     * @return
     */
    public Integer insertNJSHistoryTrade(GjsNJSHistoryTradeEntity entity) {
        return this.getSqlSession().insert("GjsNJSHistoryTrade.insert", entity);
    }

    /**
     * 根据日期查询历史交易记录
     *
     * @param date
     * @return
     */
    public List<GjsNJSHistoryTradeEntity> queryNJSHistoryTradeListByDate(String date) {
        return this.getSqlSession().selectList("GjsNJSHistoryTrade.queryNJSHistoryTradeListByDate", date);
    }

    /**
     * 根据TraderId查询NJS历史交易列表
     *
     * @param traderId
     * @return
     */
    public List<GjsNJSHistoryTradeEntity> queryNJSHistoryTradeListByTraderIdForManage(String traderId) {
        return this.getSqlSession().selectList("GjsNJSHistoryTrade.queryNJSHistoryTradeListByTraderIdForManage", traderId);
    }

    /**
     * NJS累计交易额
     *
     * @return
     */
    public String getHistoryTradeTotalMoney() {
        return this.getSqlSession().selectOne("GjsNJSHistoryTrade.getHistoryTradeTotalMoney");
    }

    /**
     * NJS累计交易次数
     *
     * @return
     */
    public Long getHistoryTradeTotalCount() {
        return this.getSqlSession().selectOne("GjsNJSHistoryTrade.getHistoryTradeTotalCount");
    }

}

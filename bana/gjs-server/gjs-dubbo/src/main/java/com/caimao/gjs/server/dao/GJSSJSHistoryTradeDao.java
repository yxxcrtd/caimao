package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.history.GjsSJSHistoryEntrustEntity;
import com.caimao.gjs.api.entity.history.GjsSJSHistoryTradeEntity;
import com.caimao.gjs.api.entity.req.FQueryHistoryTradeReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SJS历史交易DAO
 */
@Repository
public class GJSSJSHistoryTradeDao extends SqlSessionDaoSupport {

    /**
     * 插入SJS历史交易
     *
     * @param entity
     * @return
     */
    public Integer insertSJSHistoryTrade(GjsSJSHistoryTradeEntity entity) {
        return this.getSqlSession().insert("GjsSJSHistoryTrade.insert", entity);
    }

    /**
     * 根据开始时间和结束时间查询SJS历史交易列表
     *
     * @param req
     * @return
     */
    public List<GjsSJSHistoryTradeEntity> querySJSHistoryTradeList(FQueryHistoryTradeReq req) {
        return this.getSqlSession().selectList("GjsSJSHistoryTrade.querySJSHistoryTradeWithPage", req);
    }

    /**
     * 根据TraderId查询NJS历史交易列表
     *
     * @param traderId
     * @return
     */
    public List<GjsSJSHistoryTradeEntity> querySJSHistoryTradeListByTraderIdForManage(String traderId) {
        return this.getSqlSession().selectList("GjsSJSHistoryTrade.querySJSHistoryTradeListByTraderIdForManage", traderId);
    }

    /**
     * 上金所09:00-11:30交易额
     *
     * @return
     */
    public String getHistoryTradeTotalMoney1() {
        return this.getSqlSession().selectOne("GjsSJSHistoryTrade.getHistoryTradeTotalMoney1");
    }

    /**
     * 上金所09:00-11:30交易次数
     *
     * @return
     */
    public Long getHistoryTradeTotalCount1() {
        return this.getSqlSession().selectOne("GjsSJSHistoryTrade.getHistoryTradeTotalCount1");
    }

    /**
     * 上金所13:30-15:30交易额
     *
     * @return
     */
    public String getHistoryTradeTotalMoney2() {
        return this.getSqlSession().selectOne("GjsSJSHistoryTrade.getHistoryTradeTotalMoney2");
    }

    /**
     * 上金所13:30-15:30易次数
     *
     * @return
     */
    public Long getHistoryTradeTotalCount2() {
        return this.getSqlSession().selectOne("GjsSJSHistoryTrade.getHistoryTradeTotalCount2");
    }

    /**
     * 上金所20:00-02:30交易额
     *
     * @return
     */
    public String getHistoryTradeTotalMoney3() {
        return this.getSqlSession().selectOne("GjsSJSHistoryTrade.getHistoryTradeTotalMoney3");
    }

    /**
     * 上金所20:00-02:30交易次数
     *
     * @return
     */
    public Long getHistoryTradeTotalCount3() {
        return this.getSqlSession().selectOne("GjsSJSHistoryTrade.getHistoryTradeTotalCount3");
    }

}

package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.history.GjsNJSHistoryEntrustEntity;
import com.caimao.gjs.api.entity.history.GjsSJSHistoryEntrustEntity;
import com.caimao.gjs.api.entity.req.FQueryHistoryEntrustReq;
import com.caimao.gjs.api.entity.res.FQueryHistoryEntrustRes;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SJS历史委托DAO
 */
@Repository
public class GJSSJSHistoryEntrustDao extends SqlSessionDaoSupport {

    /**
     * 插入SJS历史委托
     *
     * @param entity
     * @return
     */
    public Integer insertSJSHistoryEntrust(GjsSJSHistoryEntrustEntity entity) {
        return this.getSqlSession().insert("GjsSJSHistoryEntrust.insert", entity);
    }

    /**
     * 根据开始时间和结束时间查询SJS历史委托列表
     *
     * @param req
     * @return
     */
    public List<GjsSJSHistoryEntrustEntity> querySJSHistoryEntrustList(FQueryHistoryEntrustReq req) {
        return this.getSqlSession().selectList("GjsSJSHistoryEntrust.querySJSHistoryEntrustWithPage", req);
    }

    /**
     * 根据TraderId查询NJS历史委托列表
     *
     * @param traderId
     * @return
     */
    public List<GjsSJSHistoryEntrustEntity> querySJSHistoryEntrustListByTraderIdForManage(String traderId) {
        return this.getSqlSession().selectList("GjsSJSHistoryEntrust.querySJSHistoryEntrustListByTraderIdForManage", traderId);
    }

}

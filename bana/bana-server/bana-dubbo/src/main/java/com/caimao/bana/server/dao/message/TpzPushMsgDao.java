/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.dao.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.caimao.bana.api.entity.req.content.FMsgQueryListReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.content.TpzPushMsgEntity;
import com.caimao.bana.api.entity.req.F830904Req;

/**
 * @author yanjg
 * 2015年5月22日
 */
@Repository
public class TpzPushMsgDao extends SqlSessionDaoSupport{

    /**
     * @param req
     * @return
     */
    public List queryF830904ResWithPage(F830904Req req) {
        return getSqlSession().selectList("TpzPushMsg.queryF830904ResWithPage", req);
    }

    /**
     * @param pushMsgId
     * @return
     */
    public TpzPushMsgEntity getById(Long pushMsgId) {
        return getSqlSession().selectOne("TpzPushMsg.getById", pushMsgId);
    }

    public void save(TpzPushMsgEntity entity) {
        this.getSqlSession().insert("TpzPushMsg.save", entity);
    }

    /**
     * @param update
     */
    public void update(TpzPushMsgEntity update) {
        getSqlSession().update("TpzPushMsg.update", update);
    }

    public List<HashMap> getTodayPushRiskMsg() throws Exception{
        return getSqlSession().selectList("TpzPushMsg.getTodayPushRiskMsg");
    }

    /**
     * 获取指定用户未读的消息数量
     * @param userId
     * @return
     */
    public Integer getNotReadNum(Long userId) {
        return getSqlSession().selectOne("TpzPushMsg.getNotReadNum", userId);
    }

    /**
     * 获取指定用户未读的消息数量
     * @param userId
     * @param pushTypes
     * @return
     */
    public Integer getNotReadNum(Long userId, List<String> pushTypes) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("pushTypes", pushTypes);
        return getSqlSession().selectOne("TpzPushMsg.getNotReadNumType", params);
    }

    /**
     * 查询指定用户的消息列表
     * @param req
     * @return
     */
    public List<TpzPushMsgEntity> queryMsgListWithPage(FMsgQueryListReq req) {
        return getSqlSession().selectList("TpzPushMsg.queryMsgListWithPage", req);
    }

    public void msgReadAll(Long pushUserId) throws Exception{
        getSqlSession().update("TpzPushMsg.msgReadAll", pushUserId);
    }

    public TpzPushMsgEntity getLastRiskMsg(Long userId) {
        return getSqlSession().selectOne("TpzPushMsg.getLastRiskMsg", userId);
    }


    public void clearMsg(Long userId) {
        this.getSqlSession().delete("TpzPushMsg.clearMsg", userId);
    }

    public void del(Long userId, Long pushMsgId) {
        Map<String, Long> params = new HashMap<>();
        params.put("userId", userId);
        params.put("pushMsgId", pushMsgId);
        this.getSqlSession().delete("TpzPushMsg.del", params);
    }

}

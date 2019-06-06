package com.caimao.bana.server.dao.guji;

import com.caimao.bana.api.entity.guji.GujiShareRecordEntity;
import com.caimao.bana.api.entity.req.guji.FQueryAdminShareListReq;
import com.caimao.bana.api.entity.req.guji.FQueryGujiFollowShareListReq;
import com.caimao.bana.api.entity.req.guji.FQueryGujiHallShareListReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* GujiShareRecordEntity DAO实现
*
* Created by wangxu@huobi.com on 2016-01-07 17:40:29 星期四
*/
@Repository
public class GujiShareRecordDAO extends SqlSessionDaoSupport {

    /**
     * 根据ID获取推荐信息
     * @param id
     * @return
     */
    public GujiShareRecordEntity selectById(Long id) {
        return this.getSqlSession().selectOne("GujiShareRecord.selectById", id);
    }

    /**
     * 查询指定人指定的股票推荐列表
     * @param wxId
     * @param stockCode
     * @return
     */
    public List<GujiShareRecordEntity> selectByWxIdAndCode(Long wxId, String stockCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("wxId", wxId);
        params.put("stockCode", stockCode);
        return this.getSqlSession().selectList("GujiShareRecord.selectByWxIdAndCode", params);
    }

    /**
     * 查询大厅中用户推荐的股票列表
     * @param req
     * @return
     */
    public List<GujiShareRecordEntity> selectHallListWithPage(FQueryGujiHallShareListReq req) {
        return this.getSqlSession().selectList("GujiShareRecord.selectHallListWithPage", req);
    }

    /**
     * 查询我关注用户推荐的股票动态
     * @param req
     * @return
     */
    public List<GujiShareRecordEntity> selectMyFollowListWithPage(FQueryGujiFollowShareListReq req) {
        return this.getSqlSession().selectList("GujiShareRecord.selectMyFollowListWithPage", req);
    }

    /**
     * 后台查询用户推荐的股票信息
     * @param req
     * @return
     */
    public List<GujiShareRecordEntity> queryAdminShareListWithPage(FQueryAdminShareListReq req) {
        return this.getSqlSession().selectList("GujiShareRecord.queryAdminShareListWithPage", req);
    }

    /**
     * 点赞加一
     * @param shareId
     */
    public void addLikeNum(Long shareId) {
        this.getSqlSession().update("GujiShareRecord.addLikeNum", shareId);
    }

   /**
    * 添加GujiShareRecord的接口
    *
    * @param entity
    */
    public Integer insert(GujiShareRecordEntity entity) {
        return this.getSqlSession().insert("GujiShareRecord.insert", entity);
    }

    public void update(GujiShareRecordEntity entity) {
        this.getSqlSession().update("GujiShareRecord.update", entity);
    }


    public void delete(Long id) {
        this.getSqlSession().delete("GujiShareRecord.delete", id);
    }

}
package com.caimao.bana.server.dao.userDao;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.TpzUserTradeEntity;

/**
 * tpz_user_trade操作类
 * yanjg
 */
@Repository
public class TpzUserTradeDao extends SqlSessionDaoSupport {

    /**
     * @param userId
     * @return
     */
    public TpzUserTradeEntity getById(long userId) {
        return getSqlSession().selectOne("TpzUserTrade.getById",userId);
    }

    /**
     * @param userTrade
     */
    public void update(TpzUserTradeEntity userTrade) {
        getSqlSession().update("TpzUserTrade.update",userTrade);
    }

    /**
     * @param userId
     */
    public void updateErrorCountById(Long userId) {
        getSqlSession().update("TpzUserTrade.updateErrorCountById",userId);
    }

    /**
     * 保存用户资金密码信息
     * @param userTradeEntity
     * @return
     */
    public int save(TpzUserTradeEntity userTradeEntity) {
        return getSqlSession().insert("TpzUserTrade.save", userTradeEntity);
    }
}

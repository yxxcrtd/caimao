package com.caimao.bana.server.dao.accountDao;

import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.req.FZeusUserBalanceReq;
import com.caimao.bana.api.entity.res.F830101Res;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 融资主账户管理
 * Created by WangXu on 2015/4/23.
 */
@Repository
public class TpzAccountDao extends SqlSessionDaoSupport {

    /**
     * 获取用户资产总账户信息，根据用户ID (FOR UPDATE)
     * @param userId
     * @return
     */
    public TpzAccountEntity getAccountByUserId(Long userId) {
        return getSqlSession().selectOne("TpzAccount.getAccountByUserid", userId);
    }

    /**
     * 获取用户资产总账户信息，根据账户ID (FOR UPDATE)
     * @param accountId
     * @return
     */
    public TpzAccountEntity getAccountByAccountId(Long accountId) {
        return getSqlSession().selectOne("TpzAccount.getAccountByAccountid", accountId);
    }

    /**
     * 跟新用户资产信息
     * @param tpzAccountEntity
     */
    public void update(TpzAccountEntity tpzAccountEntity) {
        getSqlSession().update("TpzAccount.update", tpzAccountEntity);
    }

    /**
     * 保存用户的账户信息
     * @param accountEntity 账户信息
     * @return  返回值
     */
    public int save(TpzAccountEntity accountEntity) {
        return getSqlSession().insert("TpzAccount.save", accountEntity);
    }

    /**
     * @param pzAccountId
     * @return
     */
    public TpzAccountEntity getById(Long pzAccountId) {
        return getSqlSession().selectOne("TpzAccount.getById", pzAccountId);
    }

    /**
     * @param longValue
     * @return
     */
    public F830101Res getByUserId(long longValue) {
        return getSqlSession().selectOne("TpzAccount.getByUserId",longValue);
    }

    /**
     * 查询用户资产
     * @param req
     * @return
     */
    public List<TpzAccountEntity> queryUserBalanceListWithPage(FZeusUserBalanceReq req){
        return getSqlSession().selectList("TpzAccount.queryUserBalanceListWithPage", req);
    }

    /**
     * 锁定用户资产
     * @param pzAccountId
     * @throws Exception
     */
    public void lockingAccount(Long pzAccountId) throws Exception{
        getSqlSession().update("TpzAccount.lockingAccount", pzAccountId);
    }
}

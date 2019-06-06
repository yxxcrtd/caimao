package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.req.ybk.FYBKQueryAccountListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkApiQueryAccountListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkNewAccountListReq;
import com.caimao.bana.api.entity.res.ybk.FYbkAccountSimpleRes;
import com.caimao.bana.api.entity.res.ybk.FYbkNewAccountListRes;
import com.caimao.bana.api.entity.ybk.YBKAccountEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 邮币卡账户
 */
@Repository
public class YBKAccountDao extends SqlSessionDaoSupport {

    public List<YBKAccountEntity> queryAccountWithPage(FYBKQueryAccountListReq req) {
        return this.getSqlSession().selectList("YBKAccount.queryListWithPage", req);
    }

    public void insert(YBKAccountEntity entity) throws Exception{
        this.getSqlSession().insert("YBKAccount.insert", entity);
    }

    public void update(YBKAccountEntity entity) throws Exception{
        this.getSqlSession().update("YBKAccount.update", entity);
    }

    public YBKAccountEntity queryById(Long id) {
        return this.getSqlSession().selectOne("YBKAccount.queryById", id);
    }

    public List<YBKAccountEntity> queryByUserId(Long userId) {
        return this.getSqlSession().selectList("YBKAccount.queryByUserId", userId);
    }

    public void delete(Integer id) {
        this.getSqlSession().delete("YBKAccount.delete", id);
    }

    public List<FYbkAccountSimpleRes> queryApiAccountApplyList(FYbkApiQueryAccountListReq req) {
        return this.getSqlSession().selectList("YBKAccount.queryApiAccountApplyList", req);
    }

    public List<FYbkNewAccountListRes> queryNewAccountApplyList(FYbkNewAccountListReq req) {
        return this.getSqlSession().selectList("YBKAccount.queryNewAccountApplyList", req);
    }

}
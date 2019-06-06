package com.caimao.bana.server.dao.accountDao;

import com.caimao.bana.api.entity.TpzAccountFreezeJourEntity;
import com.caimao.bana.api.entity.req.FAccountQueryAccountFrozenJourReq;
import com.caimao.bana.api.entity.res.FAccountQueryAccountFrozenJourRes;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 融资主账户管理
 * Created by WangXu on 2015/4/23.
 */
@Repository("tpzAccountFreezeJourDao")
public class TpzAccountFreezeJourDao extends SqlSessionDaoSupport {
    @Resource
    private MemoryDbidGenerator dbidGenerator;

    /**
     * 保存资产冻结变更历史
     * @param tpzAccountFreezeJour
     */
    public int save(TpzAccountFreezeJourEntity tpzAccountFreezeJour) {
        tpzAccountFreezeJour.setId(this.dbidGenerator.getNextId());
        return getSqlSession().insert("TpzAccountFreezeJour.save", tpzAccountFreezeJour);
    }

    /**
     * 查询资产变更列表
     * @param req 条件列表
     * @return  变更历史
     */
    public List<FAccountQueryAccountFrozenJourRes> queryAccountFrozenJourWithPage(FAccountQueryAccountFrozenJourReq req) {
        return getSqlSession().selectList("TpzAccountFreezeJour.queryAccountFrozenJourWithPage", req);
    }
}

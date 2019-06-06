/*
*TpzAccountJourDao.java
*Created on 2015/5/13 11:59
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server.dao.accountDao;

import com.caimao.bana.api.entity.TpzAccountJourEntity;
import com.caimao.bana.api.entity.req.FAccountQueryAccountJourReq;
import com.caimao.bana.api.entity.res.FAccountQueryAccountJourRes;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0.1
 */
@Repository
public class TpzAccountJourDao extends SqlSessionDaoSupport {
    @Resource
    private MemoryDbidGenerator dbidGenerator;

    /**
     * 保存资产变更历史
     * @param tpzAccountJour    资产变更
     */
    public int save(TpzAccountJourEntity tpzAccountJour) {
        tpzAccountJour.setId(this.dbidGenerator.getNextId());
        return getSqlSession().insert("TpzAccountJour.save", tpzAccountJour);
    }

    /**
     * 根据ID查询单条数据
     * @param id
     * @return
     * @throws Exception
     */
    public TpzAccountJourEntity queryById(Long id) throws Exception{
        return getSqlSession().selectOne("TpzAccountJour.queryById", id);
    }

    /**
     * 更新业务类型
     * @param id
     * @param accountBizType
     * @return
     * @throws Exception
     */
    public Integer updateBizType(Long id, String accountBizType) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("accountBizType", accountBizType);
        return getSqlSession().update("TpzAccountJour.updateBizType", params);
    }
    /**
     * 查询资产变更列表
     * @param accountQueryAccountJourReq 条件列表
     * @return  变更历史
     */
    public List<FAccountQueryAccountJourRes> queryAccountJourWithPage(FAccountQueryAccountJourReq accountQueryAccountJourReq) {
        return getSqlSession().selectList("TpzAccountJour.queryAccountJourWithPage", accountQueryAccountJourReq);
    }

}

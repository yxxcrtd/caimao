package com.caimao.bana.server.dao.homs;

import com.caimao.bana.api.entity.TpzHomsAccountChildEntity;
import com.caimao.bana.api.entity.TpzHomsAccountEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * homs子账户列表
 */
@Repository
public class TpzHomsAccountChildDao extends SqlSessionDaoSupport {

    public TpzHomsAccountChildEntity queryAccountChildByAccount(String homsFundAccount, String homsCombineId) throws Exception{
        HashMap<String, String> params = new HashMap<>();
        params.put("homsFundAccount", homsFundAccount);
        params.put("homsCombineId", homsCombineId);
        return getSqlSession().selectOne("TpzHomsAccountChild.queryAccountChildByAccount", params);
    }
}

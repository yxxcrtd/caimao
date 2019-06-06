package com.caimao.bana.server.dao.homs;

import com.caimao.bana.api.entity.req.FZeusHomsAccountHoldReq;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountHoldEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import javax.jws.Oneway;
import java.util.HashMap;
import java.util.List;

/**
 * 保存homs持仓
 */
@Repository
public class ZeusHomsAccountHoldDao extends SqlSessionDaoSupport {

    public Integer saveZeusHomsAccountHold(ZeusHomsAccountHoldEntity zeusHomsAccountHoldEntity) throws Exception {
        return getSqlSession().insert("ZeusHomsAccountHold.saveZeusHomsAccountHold", zeusHomsAccountHoldEntity);
    }

    public List<ZeusHomsAccountHoldEntity> searchHomsHoldListWithPage(FZeusHomsAccountHoldReq req) {
        return getSqlSession().selectList("ZeusHomsAccountHold.searchHomsHoldListWithPage", req);
    }

    public List<HashMap<String, String>> queryUpdated(String dateString) throws Exception {
        return getSqlSession().selectList("ZeusHomsAccountHold.queryUpdated", dateString);
    }
}

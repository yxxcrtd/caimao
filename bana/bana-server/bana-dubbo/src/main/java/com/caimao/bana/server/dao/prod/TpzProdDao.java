package com.caimao.bana.server.dao.prod;

import com.caimao.bana.api.entity.TpzProdEntity;
import com.caimao.bana.api.entity.req.product.FQueryProductReq;
import com.caimao.bana.api.entity.res.F830910Res;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * tpz_prod操作类 yanjg
 */
@Repository
public class TpzProdDao extends SqlSessionDaoSupport {

    /**
     * @param prodId
     * @return
     */
    public TpzProdEntity getProd(Long prodId) {
        return getSqlSession().selectOne("TpzProd.getById", prodId);
    }

    public List<F830910Res> queryProductList(FQueryProductReq req) {
        return getSqlSession().selectList("TpzProd.queryProductList", req);
    }

    public List<F830910Res> getProdListByStatusWithPage(FQueryProductReq req) {
        return getSqlSession().selectList("TpzProd.getProdListByStatusWithPage", req);
    }
}

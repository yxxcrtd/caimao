package com.caimao.bana.server.dao.prod;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.TpzProdDetailEntity;
import com.caimao.bana.api.entity.res.product.FProductDetailRes;

/**
 * tpz_prod_detail操作类
 * yanjg
 */
@Repository
public class TpzProdDetailDao extends SqlSessionDaoSupport {

    /**
     * @param param
     * @return
     */
    public TpzProdDetailEntity getProdDetail(Map param) {
        return getSqlSession().selectOne("TpzProdDetail.getProdDetail",param);
    }
    
    public List<FProductDetailRes> queryProdDetailList(Long prodId){
        return getSqlSession().selectList("TpzProdDetail.queryProdDetailList",prodId);
    }
}

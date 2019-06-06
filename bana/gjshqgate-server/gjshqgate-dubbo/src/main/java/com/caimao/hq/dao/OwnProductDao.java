package com.caimao.hq.dao;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.hq.api.entity.OwnProduct;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/30.
 */
@Service("ownProductDao")
public class OwnProductDao extends SqlSessionDaoSupport {

    public int insert(OwnProduct ownProduct) {
        return getSqlSession().insert("HQ_OwnProduct.insert", ownProduct);
    }

    public int update(List<OwnProduct> list) {
        return getSqlSession().insert("HQ_OwnProduct.batchUpdate", list);
    }

    public List<OwnProduct> query(Long userId) {
        return getSqlSession().selectList("HQ_OwnProduct.selectList", userId);
    }


    public List<OwnProduct> query(Long userId, String exchange, String prodCode) {
        OwnProduct ownProduct = new OwnProduct();
        ownProduct.setUserId(userId);
        ownProduct.setExchange(exchange);
        ownProduct.setProdCode(prodCode);
        return getSqlSession().selectList("HQ_OwnProduct.selectSingle", ownProduct);
    }


    public int delete(String userId,String ownProductId) {
        Map map=new HashMap();
        map.put("ownProductId",convert(ownProductId));
        map.put("userId",userId);
        return getSqlSession().delete("HQ_OwnProduct.deleteByPrimaryKey",map);
    }


    public int delete(List<OwnProduct> ownProduct) {
        return getSqlSession().delete("HQ_OwnProduct.delete", ownProduct);
    }

    private List convert(String ownProductId) {
        List list = new ArrayList();
        if (!StringUtils.isBlank(ownProductId)) {
            String[] idList = ownProductId.split(",");
            for (String str : idList) {
                if (!StringUtils.isBlank(str)) {
                    list.add(str);
                }
            }
        }
        return list;
    }
}

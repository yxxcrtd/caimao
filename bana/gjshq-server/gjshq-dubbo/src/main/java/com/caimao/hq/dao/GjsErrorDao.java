package com.caimao.hq.dao;

import com.caimao.hq.api.entity.GjsError;
import com.caimao.hq.api.entity.Product;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Created by Administrator on 2015/9/30.
 *
 */
@Service("gjsErrorDao")
public class GjsErrorDao extends SqlSessionDaoSupport {

    public int insert(GjsError gjsError) {

        return getSqlSession().insert("GJS_Error.insert", gjsError);

    }

    public List<Product> query(String finance_mic) {

        return getSqlSession().selectList("GJS_Error.selectList", finance_mic);

    }
}

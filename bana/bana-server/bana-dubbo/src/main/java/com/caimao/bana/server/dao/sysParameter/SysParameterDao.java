/*
*SysparameterDao.java
*Created on 2015/5/25 14:01
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server.dao.sysParameter;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.TsysParameterEntity;
import com.caimao.bana.api.entity.res.FP2PParameterRes;

/**
 * @author Administrator
 * @version 1.0.1
 */
@Repository
public class SysParameterDao extends SqlSessionDaoSupport {

    public TsysParameterEntity getById(String paramCode) {
        return getSqlSession().selectOne("TsysParameter.getById", paramCode);
    }
    /**
     * 获取P2P配置参数
     */
    public List<FP2PParameterRes> getP2PParameter() {
        return getSqlSession().selectList("TsysParameter.getP2PParameter");
    }
}

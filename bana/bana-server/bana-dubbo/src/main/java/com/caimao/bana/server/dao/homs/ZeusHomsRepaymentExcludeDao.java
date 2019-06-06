package com.caimao.bana.server.dao.homs;

import com.caimao.bana.api.entity.zeus.ZeusHomsRepaymentExcludeEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 有持仓可还款的排除列表数据操作
 * Created by WangXu on 2015/7/16.
 */
@Repository
public class ZeusHomsRepaymentExcludeDao extends SqlSessionDaoSupport {

    /**
     * 获取指定的账户的排除列表（如果都为null，则查询所有的）
     * @param homsFundAccount
     * @param homsCombineId
     * @return
     */
    public List<ZeusHomsRepaymentExcludeEntity> queryList(String homsFundAccount, String homsCombineId) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("homsFundAccount", homsFundAccount);
        paramsMap.put("homsCombineId", homsCombineId);
        return getSqlSession().selectList("ZeusHomsRepaymentExclude.queryList", paramsMap);
    }

    /**
     * 保存
     * @param entity
     * @return
     */
    public Integer save(ZeusHomsRepaymentExcludeEntity entity) {
        return getSqlSession().insert("ZeusHomsRepaymentExclude.save", entity);
    }

    /**
     * 删除
     * @param homsFundAccount
     * @param homsCombineId
     * @return
     */
    public Integer delete(String homsFundAccount, String homsCombineId) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("homsFundAccount", homsFundAccount);
        paramsMap.put("homsCombineId", homsCombineId);
        return getSqlSession().delete("ZeusHomsRepaymentExclude.delete", paramsMap);
    }
}

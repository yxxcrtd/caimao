package com.caimao.bana.server.dao.activity;

import com.caimao.bana.api.entity.activity.BanaLoseRPEntity;
import com.caimao.bana.api.entity.activity.BanaLoseRPRecordEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;

/**
 * 失落红包
 * Created by WangXu on 2015/5/22.
 */
@Repository
public class BanaLoseRPDao extends SqlSessionDaoSupport {
    public BanaLoseRPRecordEntity getRecordByUserId(Long userId) throws Exception {
        return getSqlSession().selectOne("BanaLoseRP.getRecordByUserId", userId);
    }

    public BanaLoseRPEntity getLoseRpDataForUpdate(Long rpId) throws Exception {
        return getSqlSession().selectOne("BanaLoseRP.getLoseRpDataForUpdate", rpId);
    }

    public Integer updateBanaLoseRP(Long rpId, String rpData) throws Exception {
        HashMap<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("rpId", rpId);
        updateInfo.put("rpData", rpData);
        updateInfo.put("updated", new Date());
        return getSqlSession().update("BanaLoseRP.updateBanaLoseRP", updateInfo);
    }

    public Integer insertRecord(BanaLoseRPRecordEntity banaLoseRPRecordEntity) throws Exception{
        return getSqlSession().insert("BanaLoseRP.insertRecord", banaLoseRPRecordEntity);
    }
}
package com.caimao.bana.server.dao.guji;

import com.caimao.bana.api.entity.guji.GujiFavourRecordEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
* 股计点赞的记录表 DAO实现
*
* Created by wangxu@huobi.com on 2016-01-07 17:33:02 星期四
*/
@Repository
public class GujiFavourRecordDAO extends SqlSessionDaoSupport {

   /**
    * 查询指定GujiFavourRecord内容
    * @param shareId
    * @param openId
    * @return
    */
    public GujiFavourRecordEntity selectByShareIdAndOpenId(Long shareId, String openId) {
        Map<String, Object> params = new HashMap<>();
        params.put("shareId", shareId);
        params.put("openId", openId);
        return this.getSqlSession().selectOne("GujiFavourRecord.selectByShareIdAndOpenId", params);
    }

   /**
    * 添加GujiFavourRecord的接口
    *
    * @param entity
    */
    public Integer insert(GujiFavourRecordEntity entity) {
        return this.getSqlSession().insert("GujiFavourRecord.insert", entity);
    }

   /**
    * 删除GujiFavourRecord的接口
    *
    * @param shareId
    * @param openId
    */
    public Integer delete(Long shareId, String openId) {
        Map<String, Object> params = new HashMap<>();
        params.put("shareId", shareId);
        params.put("openId", openId);
        return this.getSqlSession().delete("GujiFavourRecord.delete", params);
    }
}
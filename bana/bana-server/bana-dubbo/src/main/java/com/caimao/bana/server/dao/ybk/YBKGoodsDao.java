package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.ybk.YBKGoodsEntity;
import com.caimao.bana.api.entity.ybk.YBKSummaryEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 邮币卡 藏品数据表
 */
@Repository
public class YBKGoodsDao extends SqlSessionDaoSupport {
    public Integer insert(YBKGoodsEntity ybkGoodsEntity) throws Exception{
        return this.getSqlSession().insert("YBKGoods.insert", ybkGoodsEntity);
    }

    public Integer update(YBKGoodsEntity ybkGoodsEntity) throws Exception{
        return this.getSqlSession().update("YBKGoods.update", ybkGoodsEntity);
    }

    public YBKGoodsEntity queryGood(Integer exchangeId,String goodCode) throws Exception{
        YBKGoodsEntity entity = new YBKGoodsEntity();
        entity.setExchangeId(exchangeId);
        entity.setGoodCode(goodCode);
        return this.getSqlSession().selectOne("YBKGoods.queryGood", entity);
    }

    public List<YBKGoodsEntity> queryAllGoods(String exchangeShortName) throws Exception{
        return this.getSqlSession().selectList("YBKGoods.queryAllGoods", exchangeShortName);
    }

    public List<YBKGoodsEntity> searchGoods(String goodName) throws Exception{
        YBKGoodsEntity entity = new YBKGoodsEntity();
        entity.setGoodName(goodName);
        return this.getSqlSession().selectList("YBKGoods.searchGoods", entity);
    }
}
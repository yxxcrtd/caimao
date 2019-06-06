package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.req.ybk.FYbkExchangeQueryListReq;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 邮币卡交易所数据操作
 * Created by Administrator on 2015/9/7.
 */
@Repository
public class YBKExchangeDao extends SqlSessionDaoSupport {
    public int deleteByPrimaryKey(Integer id) {
        return this.getSqlSession().delete("YbkExchange.deleteByPrimaryKey", id);
    }

    public int insertSelective(YbkExchangeEntity record) {
        return this.getSqlSession().insert("YbkExchange.insertSelective", record);
    }

    public YbkExchangeEntity selectByPrimaryKey(Integer id) {
        return this.getSqlSession().selectOne("YbkExchange.selectByPrimaryKey", id);
    }

    public YbkExchangeEntity selectByShortName(String shortName) {
        return this.getSqlSession().selectOne("YbkExchange.selectByShortName", shortName);
    }

    public int updateByPrimaryKeySelective(YbkExchangeEntity record) {
        return this.getSqlSession().update("YbkExchange.updateByPrimaryKeySelective", record);
    }

    public List<YbkExchangeEntity> queryList(FYbkExchangeQueryListReq req) {
        return this.getSqlSession().selectList("YbkExchange.queryList", req);
    }

    public List<YbkExchangeEntity> queryListByBankNo(String bankNo) {
        return this.getSqlSession().selectList("YbkExchange.queryListByBankNo", bankNo);
    }

}

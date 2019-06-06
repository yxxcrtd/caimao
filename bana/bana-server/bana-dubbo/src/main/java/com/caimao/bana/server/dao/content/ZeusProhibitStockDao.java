package com.caimao.bana.server.dao.content;

import com.caimao.bana.api.entity.zeus.ZeusProhibitStockEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 禁买股票列表
 * Created by Administrator on 2015/8/17.
 */
@Repository
public class ZeusProhibitStockDao extends SqlSessionDaoSupport {

    /**
     * 保存禁买股票信息
     * @param entity
     */
    public void save(ZeusProhibitStockEntity entity) {
        this.getSqlSession().insert("ZeusProhibitStock.save", entity);
    }

    /**
     * 获取所有禁买股票的信息
     * @return
     */
    public List<ZeusProhibitStockEntity> list() {
        return this.getSqlSession().selectList("ZeusProhibitStock.list");
    }

    /**
     * 删除指定的禁买股票信息
     * @param id
     */
    public void del(Integer id) {
        this.getSqlSession().delete("ZeusProhibitStock.del", id);
    }
}

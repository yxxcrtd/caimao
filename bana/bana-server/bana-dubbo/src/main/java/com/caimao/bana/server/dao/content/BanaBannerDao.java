package com.caimao.bana.server.dao.content;

import com.caimao.bana.api.entity.content.BanaBannerEntity;
import com.caimao.bana.api.entity.req.content.FQueryBannerListReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 首页banner数据表操作
 * Created by Administrator on 2015/10/14.
 */
@Repository
public class BanaBannerDao extends SqlSessionDaoSupport {

    public List<BanaBannerEntity> queryListWithPage(FQueryBannerListReq req) {
        return this.getSqlSession().selectList("BanaBanner.queryListWithPage", req);
    }

    public BanaBannerEntity select(Integer id) {
        return this.getSqlSession().selectOne("BanaBanner.select", id);
    }

    public Integer delete(Integer id) {
        return this.getSqlSession().delete("BanaBanner.delete", id);
    }

    public Integer insert(BanaBannerEntity entity) {
        return this.getSqlSession().insert("BanaBanner.insert", entity);
    }

    public Integer update(BanaBannerEntity entity) {
        return this.getSqlSession().insert("BanaBanner.update", entity);
    }

}

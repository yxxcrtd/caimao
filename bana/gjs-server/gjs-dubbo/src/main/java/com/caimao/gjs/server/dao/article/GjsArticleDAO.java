package com.caimao.gjs.server.dao.article;

import com.caimao.gjs.api.entity.article.GjsArticleEntity;
import com.caimao.gjs.api.entity.article.GjsArticleJin10Entity;
import com.caimao.gjs.api.entity.req.FQueryArticleReq;
import com.caimao.gjs.api.entity.req.FQueryGjsArticleJin10Req;
import com.caimao.gjs.api.entity.res.FQueryGjsArticleRes;
import com.google.code.routing4db.holder.RoutingHolder;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属文章数据操作
 * Created by Administrator on 2015/10/12.
 */
public class GjsArticleDAO extends SqlSessionDaoSupport implements IArticleDAO {

    public GjsArticleEntity selectById(Long id) {
        return this.getSqlSession().selectOne("GjsArticle.selectById", id);
    }

    public Integer deleteById(Long id) {
        return this.getSqlSession().delete("GjsArticle.deleteById", id);
    }

    public Integer insert(GjsArticleEntity entity) {
        return this.getSqlSession().insert("GjsArticle.insert", entity);
    }

    public Integer update(GjsArticleEntity entity) {
        return this.getSqlSession().update("GjsArticle.update", entity);
    }

    /**
     * 查询指定的文章列表
     *
     * @param req
     * @return
     */
    public List<GjsArticleEntity> queryArticleWithPage(FQueryArticleReq req) {
        return this.getSqlSession().selectList("GjsArticle.queryArticleWithPage", req);
    }

    /**
     * 查询指定的文章列表（后台管理）
     *
     * @param req
     * @return
     */
    public List<FQueryGjsArticleRes> queryArticleForManageWithPage(FQueryArticleReq req) {
        return this.getSqlSession().selectList("GjsArticle.queryArticleForManageWithPage", req);
    }

    /**
     * 根据标题和抓取的地址获取 GjsArticleEntity
     *
     * @param title
     * @param sourceUrl
     * @return
     */
    public List<GjsArticleEntity> queryArticleByTitleAndSourceUrl(String title, String sourceUrl) {
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("sourceUrl", sourceUrl);
        return this.getSqlSession().selectList("GjsArticle.queryArticleByTitleAndSourceUrl", map);
    }

    /**
     * 查询指定GjsArticleJin10内容
     *
     * @param id
     * @return
     */
    public GjsArticleJin10Entity selectByArticleJin10Id(Long id) {
        return this.getSqlSession().selectOne("GjsArticleJin10.selectById", id);
    }

    /**
     * 根据时间id查询指定GjsArticleJin10内容
     *
     * @param timeId
     * @return
     */
    public GjsArticleJin10Entity selectByTimeId(String timeId) {
        return this.getSqlSession().selectOne("GjsArticleJin10.selectByTimeId", timeId);
    }

    /**
     * 删除GjsArticleJin10的接口
     *
     * @param id
     */
    public Integer deleteByArticleJin10Id(Long id) {
        return this.getSqlSession().delete("GjsArticleJin10.deleteById", id);
    }

    /**
     * 更新GjsArticleJin10的接口
     *
     * @param entity
     */
    public Integer update(GjsArticleJin10Entity entity) {
        return this.getSqlSession().update("GjsArticleJin10.update", entity);
    }

    /**
     * 查询GjsArticleJin10列表
     *
     * @param req
     * @return
     */
    public List<GjsArticleJin10Entity> queryGjsArticleJin10WithPage(FQueryGjsArticleJin10Req req) {
        return this.getSqlSession().selectList("GjsArticleJin10.queryArticleWithPage", req);
    }

    /**
     * 添加GjsArticleJin10的接口
     *
     * @param entity
     */
    public Integer insert(GjsArticleJin10Entity entity) {
        return this.getSqlSession().insert("GjsArticleJin10.insert", entity);
    }

}

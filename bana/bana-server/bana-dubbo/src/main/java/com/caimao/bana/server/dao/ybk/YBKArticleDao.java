package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.req.ybk.FYBKQueryArticleIdReq;
import com.caimao.bana.api.entity.req.ybk.FYBKQueryArticleListReq;
import com.caimao.bana.api.entity.req.ybk.FYBKQueryArticleSimpleListReq;
import com.caimao.bana.api.entity.res.ybk.FYBKArticleSimpleRes;
import com.caimao.bana.api.entity.ybk.YBKAccountEntity;
import com.caimao.bana.api.entity.ybk.YBKArticleEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮币卡文章
 */
@Repository
public class YBKArticleDao extends SqlSessionDaoSupport {
    public Integer insert(YBKArticleEntity ybkArticleEntity) throws Exception{
        return this.getSqlSession().insert("YBKArticle.insert", ybkArticleEntity);
    }

    public void delete(Long id) throws Exception{
        this.getSqlSession().delete("YBKArticle.delete", id);
    }

    public void update(YBKArticleEntity ybkArticleEntity) throws Exception{
        this.getSqlSession().update("YBKArticle.update", ybkArticleEntity);
    }

    public YBKArticleEntity queryById(Long id) throws Exception{
        return this.getSqlSession().selectOne("YBKArticle.queryById", id);
    }

    public void readArticle(Long id) throws Exception {
        this.getSqlSession().update("YBKArticle.readArticle", id);
    }

    public List<YBKArticleEntity> queryListWithPage(FYBKQueryArticleListReq req) throws Exception{
        return this.getSqlSession().selectList("YBKArticle.queryListWithPage", req);
    }

    /**
     * 获取指定交易所指定类型下，最新文章的信息
     * @param exchangeId
     * @param categoryId
     * @return
     * @throws Exception
     */
    public YBKArticleEntity queryLastTime(Integer exchangeId, Integer categoryId) throws Exception {
        Map<String, Integer> paramMap = new HashMap<>();
        paramMap.put("exchangeId", exchangeId);
        paramMap.put("categoryId", categoryId);
        return this.getSqlSession().selectOne("YBKArticle.queryLastTime", paramMap);
    }

    public List<FYBKArticleSimpleRes> querySimpleListWithPage(FYBKQueryArticleSimpleListReq req) throws Exception{
        return this.getSqlSession().selectList("YBKArticle.querySimpleListWithPage", req);
    }

    public YBKArticleEntity queryId(FYBKQueryArticleIdReq req) throws Exception{
        return this.getSqlSession().selectOne("YBKArticle.queryId", req);
    }
}
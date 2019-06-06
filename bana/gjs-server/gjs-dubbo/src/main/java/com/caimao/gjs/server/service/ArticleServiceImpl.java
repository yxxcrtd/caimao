package com.caimao.gjs.server.service;

import com.caimao.gjs.api.entity.GjsArticleCategoryEntity;
import com.caimao.gjs.api.entity.GjsArticleIndexEntity;
import com.caimao.gjs.api.entity.article.GjsArticleEntity;
import com.caimao.gjs.api.entity.article.GjsArticleJin10Entity;
import com.caimao.gjs.api.entity.req.FQueryArticleIndexReq;
import com.caimao.gjs.api.entity.req.FQueryArticleReq;
import com.caimao.gjs.api.entity.req.FQueryGjsArticleJin10Req;
import com.caimao.gjs.api.entity.res.FQueryGjsArticleRes;
import com.caimao.gjs.api.service.IArticleService;
import com.caimao.gjs.server.dao.GjsArticleCategoryDAO;
import com.caimao.gjs.server.dao.GjsArticleIndexDAO;
import com.caimao.gjs.server.dao.article.IArticleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 贵金属文章的相关服务接口实现
 * Created by Administrator on 2015/10/12.
 */
@Service("articleService")
public class ArticleServiceImpl implements IArticleService {

   @Autowired
    private IArticleDAO articleDAO;

    @Autowired
    private GjsArticleCategoryDAO gjsArticleCategoryDAO;

    @Autowired
    private GjsArticleIndexDAO gjsArticleIndexDAO;

    /**
     * 查询文章列表
     *
     * @param req
     * @return
     * @throws Exception
     * @remark 后台管理功能使用
     */
    @Override
    public FQueryArticleReq queryArticleListForManage(FQueryArticleReq req) throws Exception {
        req.setItems(this.articleDAO.queryArticleForManageWithPage(req));
        return req;
    }

    /**
     * 查询文章列表
     *
     * @param req
     * @return
     * @throws Exception
     * @remark 提供接口调用
     */
    @Override
    public FQueryArticleReq queryArticleList(FQueryArticleReq req) throws Exception {
        if (req.getDateStart() != null) req.setDateStart(req.getDateStart() + " 00:00:00");
        if (req.getDateEnd() != null) req.setDateEnd(req.getDateEnd() + " 23:59:59");
        if (null != req.getCategoryId()) { req.setCategoryId(req.getCategoryId()); }
        if (null != req.getIsHot()) { req.setIsHot(req.getIsHot()); }
        List<GjsArticleEntity> list = this.articleDAO.queryArticleWithPage(req);

        // 重新给前台接口组装一个新的List
        List<FQueryGjsArticleRes> listNew = new ArrayList<>();
        for (GjsArticleEntity aList : list) {
            FQueryGjsArticleRes res = new FQueryGjsArticleRes();
            res.setId(aList.getId());
            res.setCategoryId(aList.getCategoryId());
            res.setSourceUrl(aList.getSourceUrl());
            res.setSourceName(aList.getSourceName());
            res.setTitle(aList.getTitle());
            res.setSummary(aList.getSummary());
            res.setIsHot(aList.getIsHot());
            res.setIsShow(aList.getIsShow());
            res.setSort(aList.getSort());
            res.setViewCount(aList.getViewCount());
            res.setCreated(1 == aList.getCategoryId() ? aList.getCreated() : GJSArticleServiceHelper.getPrettyTime(aList.getCreated(), ""));
            listNew.add(res);
        }
        req.setItems(listNew);
        return req;
    }

    /**
     * 根据 ArticleId 查询指定文章对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public GjsArticleEntity getArticleById(Long id) throws Exception {
        // 查看文章的同时，将文章的阅读数+1
        GjsArticleEntity entity = this.articleDAO.selectById(id);
        if (null != entity) {
            int viewCount = entity.getViewCount() + 1;
            entity.setViewCount(viewCount);
            this.articleDAO.update(entity);
        }
        return entity;
    }

    /**
     * 查询指定的Jin10对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public GjsArticleJin10Entity getArticleJin10ById(Long id) throws Exception {
        return this.articleDAO.selectByArticleJin10Id(id);
    }

    /**
     * 添加文章的接口
     * @param entity
     * @throws Exception
     */
    @Override
    public void addArticleInfo(GjsArticleEntity entity) throws Exception {
        this.articleDAO.insert(entity);
    }

    /**
     * 更新文章的接口
     * @param entity
     * @throws Exception
     */
    @Override
    public void updateArticleInfo(GjsArticleEntity entity) throws Exception {
        this.articleDAO.update(entity);
    }

    /**
     * 删除文章的接口
     * @param id
     * @throws Exception
     */
    @Override
    public void delArticle(Long id) throws Exception {
        this.articleDAO.deleteById(id);
    }


    /**
     * 删除jin10文章的接口
     * @param id
     * @throws Exception
     */
    @Override
    public void deleteByArticleJin10Id(Long id) throws Exception {
        this.articleDAO.deleteByArticleJin10Id(id);
    }

    @Override
    public void addArticleJin10(GjsArticleJin10Entity entity) throws Exception {
        this.articleDAO.insert(entity);
    }

    /**
     * 更新文章的接口
     * @param entity
     * @throws Exception
     */
    @Override
    public void updateArticleJin10(GjsArticleJin10Entity entity) throws Exception {
        this.articleDAO.update(entity);
    }

    /**
     * 查询GjsArticleJin10列表
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FQueryGjsArticleJin10Req queryGjsArticleJin10List(FQueryGjsArticleJin10Req req) throws Exception {
        List<GjsArticleJin10Entity> list = this.articleDAO.queryGjsArticleJin10WithPage(req);
        // 重新给前台接口组装一个新的List
        List<GjsArticleJin10Entity> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            GjsArticleJin10Entity entity = new GjsArticleJin10Entity();
            entity.setId(list.get(i).getId());
            entity.setTimeId(GJSArticleServiceHelper.getPrettyTime(list.get(i).getTimeId().substring(0, 14), "f"));
            entity.setCategory(list.get(i).getCategory());
            entity.setTime(list.get(i).getTime());
            entity.setContent(list.get(i).getContent());
            entity.setStar(list.get(i).getStar());
            entity.setInfact(list.get(i).getInfact());
            entity.setBeforeValue(list.get(i).getBeforeValue());
            entity.setExpect(list.get(i).getExpect());
            entity.setResult(list.get(i).getResult());
            newList.add(entity);
        }
        req.setItems(newList);
        return req;
    }

    @Override
    public List<GjsArticleCategoryEntity> queryGjsArticleCategoryList() throws Exception {
        return this.gjsArticleCategoryDAO.queryGjsArticleCategoryAllList();
    }

    @Override
    public GjsArticleCategoryEntity getArticleCategoryById(Long id) throws Exception {
        return this.gjsArticleCategoryDAO.selectById(id);
    }

    @Override
    public void insertArticleCategory(GjsArticleCategoryEntity entity) throws Exception {
        this.gjsArticleCategoryDAO.insert(entity);
    }

    @Override
    public void deleteArticleCategoryById(Long id) throws Exception {
        this.gjsArticleCategoryDAO.deleteById(id);
    }

    @Override
    public void updateArticleCategory(GjsArticleCategoryEntity entity) throws Exception {
        this.gjsArticleCategoryDAO.update(entity);
    }

    @Override
    public FQueryArticleIndexReq queryGjsArticleIndexList(FQueryArticleIndexReq req) throws Exception {
        List<GjsArticleIndexEntity> list = gjsArticleIndexDAO.queryGjsArticleIndexAllList(req);
        req.setItems(list);
        return req;
    }

    @Override
    public FQueryArticleIndexReq queryGjsArticleIndexListForInterface(FQueryArticleIndexReq req, String url) throws Exception {
        req.setPubStart(String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));
        List<GjsArticleIndexEntity> list = gjsArticleIndexDAO.queryGjsArticleIndexListForInterface(req);
        List<GjsArticleIndexEntity> newList = new ArrayList<>();
        if (0 < list.size()) {
            for (GjsArticleIndexEntity e : list) {
                GjsArticleIndexEntity entity = new GjsArticleIndexEntity();
                entity.setId(e.getId());
                entity.setCategory(e.getCategory());
                entity.setTitle(e.getTitle());
                entity.setStatus(e.getStatus());
                entity.setView(e.getView());
                entity.setPub(e.getPub() + ":00");
                entity.setCreated(e.getPub() + ":00");
                entity.setLabel(GJSArticleServiceHelper.getLabelTime(e.getPub() + ":00"));
                entity.setBefore(GJSArticleServiceHelper.getPrettyTime(e.getPub() + ":00", ""));
                entity.setUser(getUser(url, e.getUser()));
                entity.setHref(url + "/upload/article/html/index_" + e.getId() + ".html?" + System.currentTimeMillis());
                newList.add(entity);
            }
        }
        req.setItems(newList);
        return req;
    }

    private String getUser(String url, String s) {
        String[] array = s.split(",");
        return array[1] + "," + url + "/upload/article/" + array[2];
    }

    @Override
    public GjsArticleIndexEntity getArticleIndexById(Long id) throws Exception {
        GjsArticleIndexEntity entity = this.gjsArticleIndexDAO.selectById(id);
        if (null != entity) {
            int view = entity.getView() + 1;
            entity.setView(view);
            this.gjsArticleIndexDAO.update(entity);
        }
        return entity;
    }

    @Override
    public GjsArticleIndexEntity insertArticleIndex(GjsArticleIndexEntity entity) throws Exception {
        this.gjsArticleIndexDAO.insert(entity);
        return entity;
    }

    @Override
    public void deleteArticleIndexById(Long id) throws Exception {
        this.gjsArticleIndexDAO.deleteById(id);
    }

    @Override
    public void updateArticleIndex(GjsArticleIndexEntity entity) throws Exception {
        this.gjsArticleIndexDAO.update(entity);
    }

    @Override
    public List<GjsArticleIndexEntity> queryTopGjsArticleIndexList() throws Exception {
        return this.gjsArticleIndexDAO.queryTopGjsArticleIndexList();
    }

    @Override
    public void resetStatus(int oldStatus) throws Exception {
        List<GjsArticleIndexEntity> topList = gjsArticleIndexDAO.queryTopGjsArticleIndexList();
        for (GjsArticleIndexEntity entity : topList) {
            if (oldStatus < entity.getStatus()) {
                entity.setStatus(entity.getStatus() - 1);
                gjsArticleIndexDAO.update(entity);
            }
        }
    }

    @Override
    public List<GjsArticleIndexEntity> queryGjsArticleIndexCategoryList(int category) throws Exception {
        return gjsArticleIndexDAO.queryGjsArticleIndexCategoryList(category);
    }

}

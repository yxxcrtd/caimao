package com.caimao.gjs.api.service;

import com.caimao.gjs.api.entity.GjsArticleCategoryEntity;
import com.caimao.gjs.api.entity.GjsArticleIndexEntity;
import com.caimao.gjs.api.entity.article.GjsArticleEntity;
import com.caimao.gjs.api.entity.article.GjsArticleJin10Entity;
import com.caimao.gjs.api.entity.req.FQueryArticleIndexReq;
import com.caimao.gjs.api.entity.req.FQueryArticleReq;
import com.caimao.gjs.api.entity.req.FQueryGjsArticleJin10Req;

import java.util.List;

/**
 * 贵金属文章的服务接口
 */
public interface IArticleService {

    FQueryArticleReq queryArticleList(FQueryArticleReq req) throws Exception;

    FQueryArticleReq queryArticleListForManage(FQueryArticleReq req) throws Exception;

    GjsArticleEntity getArticleById(Long id) throws Exception;

    void addArticleInfo(GjsArticleEntity entity) throws Exception;

    void updateArticleInfo(GjsArticleEntity entity) throws Exception;

    void delArticle(Long id) throws Exception;

    GjsArticleJin10Entity getArticleJin10ById(Long id) throws Exception;

    void addArticleJin10(GjsArticleJin10Entity entity) throws Exception;

    void updateArticleJin10(GjsArticleJin10Entity entity) throws Exception;

    void deleteByArticleJin10Id(Long id) throws Exception;

    FQueryGjsArticleJin10Req queryGjsArticleJin10List(FQueryGjsArticleJin10Req req) throws Exception;

    List<GjsArticleCategoryEntity> queryGjsArticleCategoryList() throws Exception;

    GjsArticleCategoryEntity getArticleCategoryById(Long id) throws Exception;

    void insertArticleCategory(GjsArticleCategoryEntity entity) throws Exception;

    void deleteArticleCategoryById(Long id) throws Exception;

    void updateArticleCategory(GjsArticleCategoryEntity entity) throws Exception;

    FQueryArticleIndexReq queryGjsArticleIndexList(FQueryArticleIndexReq req) throws Exception;

    FQueryArticleIndexReq queryGjsArticleIndexListForInterface(FQueryArticleIndexReq req, String url) throws Exception;

    GjsArticleIndexEntity getArticleIndexById(Long id) throws Exception;

    GjsArticleIndexEntity insertArticleIndex(GjsArticleIndexEntity entity) throws Exception;

    void deleteArticleIndexById(Long id) throws Exception;

    void updateArticleIndex(GjsArticleIndexEntity entity) throws Exception;

    List<GjsArticleIndexEntity> queryTopGjsArticleIndexList() throws Exception;

    void resetStatus(int oldStatus) throws Exception;

    List<GjsArticleIndexEntity> queryGjsArticleIndexCategoryList(int category) throws Exception;

}

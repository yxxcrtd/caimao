package com.caimao.gjs.server.dao.article;

import com.caimao.gjs.api.entity.article.GjsArticleEntity;
import com.caimao.gjs.api.entity.article.GjsArticleJin10Entity;
import com.caimao.gjs.api.entity.req.FQueryArticleReq;
import com.caimao.gjs.api.entity.req.FQueryGjsArticleJin10Req;
import com.caimao.gjs.api.entity.res.FQueryGjsArticleRes;

import java.util.List;

/**
 * Article DAO Interface
 */
public interface IArticleDAO {

    GjsArticleEntity selectById(Long id);

    Integer deleteById(Long id);

    Integer insert(GjsArticleEntity entity);

    Integer update(GjsArticleEntity entity);

    List<GjsArticleEntity> queryArticleWithPage(FQueryArticleReq req);

    List<FQueryGjsArticleRes> queryArticleForManageWithPage(FQueryArticleReq req);

    List<GjsArticleEntity> queryArticleByTitleAndSourceUrl(String title, String sourceUrl);

    List<GjsArticleJin10Entity> queryGjsArticleJin10WithPage(FQueryGjsArticleJin10Req req);

    GjsArticleJin10Entity selectByTimeId(String timeId);

    Integer insert(GjsArticleJin10Entity entity);

    GjsArticleJin10Entity selectByArticleJin10Id(Long id);

    Integer deleteByArticleJin10Id(Long id);

    Integer update(GjsArticleJin10Entity entity);

}

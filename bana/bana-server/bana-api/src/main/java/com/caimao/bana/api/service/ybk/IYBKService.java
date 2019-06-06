package com.caimao.bana.api.service.ybk;

import com.caimao.bana.api.entity.req.ybk.*;
import com.caimao.bana.api.entity.res.ybk.*;
import com.caimao.bana.api.entity.ybk.YBKActivityEntity;
import com.caimao.bana.api.entity.ybk.YBKArticleEntity;
import com.caimao.bana.api.entity.ybk.YBKGoodsEntity;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;

import java.util.List;

/**
 * 邮币卡相关服务接口
 */
public interface IYBKService {
    /**
     * 添加新的文章
     * @param ybkArticleEntity 实体类
     * @throws Exception
     */
    public void articleInsert(YBKArticleEntity ybkArticleEntity) throws Exception;

    /**
     * 删除文章
     * @param id 文章ID
     * @throws Exception
     */
    public void articleDelete(Long id) throws Exception;

    /**
     * 更新文章
     * @param ybkArticleEntity 实体类
     * @throws Exception
     */
    public void articleUpdate(YBKArticleEntity ybkArticleEntity) throws Exception;

    /**
     * 查询单条文章
     * @param id 文章ID
     * @return YBKArticleEntity
     * @throws Exception
     */
    public YBKArticleEntity queryArticleById(Long id) throws Exception;

    /**
     * 阅读文章，文章阅读数加一
     * @param id
     * @throws Exception
     */
    public void readArticle(Long id) throws Exception;

    /**
     * 查询列表分页
     * @param req req类
     * @return FYBKQueryArticleListReq
     * @throws Exception
     */
    public FYBKQueryArticleListReq queryArticleWithPage(FYBKQueryArticleListReq req) throws Exception;

    /**
     * 前台API用到的，返回简单的文章列表页面
     * @param req   请求对象
     * @return
     * @throws Exception
     */
    public FYBKQueryArticleSimpleListReq queryArticleSimpleList(FYBKQueryArticleSimpleListReq req) throws Exception;

    /**
     * 获得上一篇或下一篇的id
     * @param req 请求对象
     * @return
     * @throws Exception
     */
    public YBKArticleEntity queryArticleId(FYBKQueryArticleIdReq req) throws Exception;

    /**
     * 查询交易所的列表
     * @param req
     * @return
     * @throws Exception
     */
    public List<YbkExchangeEntity> queryExchangeList(FYbkExchangeQueryListReq req) throws Exception;

    /**
     * 添加邮币卡交易所信息
     * @param ybkExchange
     * @throws Exception
     */
    public void addExchange(YbkExchangeEntity ybkExchange) throws Exception;

    /**
     * 更新邮币卡交易所信息
     * @param ybkExchange
     * @throws Exception
     */
    public void updateExchange(YbkExchangeEntity ybkExchange) throws Exception;

    /**
     * 删除邮币卡交易所信息
     * @param id
     * @throws Exception
     */
    public void deleteExchange(Integer id) throws Exception;

    /**
     * 获取指定的交易所信息，根据ID来获取
     * @param id
     * @return
     * @throws Exception
     */
    public YbkExchangeEntity getExchangeById(Integer id) throws Exception;

    /**
     * 获取指定的交易所信息，根据简称来获取
     * @param shortName
     * @return
     * @throws Exception
     */
    public YbkExchangeEntity getExchangeByShortName(String shortName) throws Exception;

    /**
     * 根据银行卡代号，获取所支持的交易所列表
     * @param bankNo
     * @return
     * @throws Exception
     */
    public List<YbkExchangeEntity> queryExchangeByBankNo(String bankNo) throws Exception;

    /**
     * 查询综合指数
     * @return 综合指数
     * @throws Exception
     */
    public List<FYBKCompositeIndexRes> queryCompositeIndex() throws Exception;

    /**
     * 查询指定交易所下的藏品商品行情信息列表
     * @param req
     * @return
     * @throws Exception
     */
    public FYbkQueryCollectionRankingReq queryCollectionRanking(FYbkQueryCollectionRankingReq req) throws Exception;

    /**
     * 查询指定交易所下的指定商品行情信息
     * @param exchangeShortName 交易所简称
     * @param code  藏品代码
     * @return
     * @throws Exception
     */
    public FYBKCollectionRankingRes getCollectionInfo(String exchangeShortName, String code) throws Exception;

    /**
     * 查询分时
     * @param req
     * @return
     * @throws Exception
     */
    public List<FYBKTimeLineRes> queryTimeLine(FYbkMarketReq req) throws Exception;

    /**
     * 查询日K
     * @param req
     * @return
     * @throws Exception
     */
    public List<FYBKKLineRes> queryKLine(FYbkMarketReq req) throws Exception;

    /**
     * 查询MACD
     * @param req
     * @return
     * @throws Exception
     */
    public List<FYBKMACDRes> queryMACD(FYbkMarketReq req) throws Exception;

    /**
     * 查询KDJ
     * @param req
     * @return
     * @throws Exception
     */
    public List<FYBKKDJRes> queryKDJ(FYbkMarketReq req) throws Exception;

    /**
     * 查询RSI
     * @param req
     * @return
     * @throws Exception
     */
    public List<FYBKRSIRes> queryRSI(FYbkMarketReq req) throws Exception;

    /**
     * 搜索藏品
     * @param condition
     * @return
     * @throws Exception
     */
    public List<YBKGoodsEntity> searchGoods(String condition) throws Exception;

    /**
     * 查询活动分页
     * @param req 请求对象
     * @return FYBKQueryActivityListReq
     * @throws Exception
     */
    public FYBKQueryActivityListReq queryActivityWithPage(FYBKQueryActivityListReq req) throws Exception;

    /**
     * 插入活动
     * @param entity 活动实体
     * @throws Exception
     */
    public void activityInsert(YBKActivityEntity entity) throws Exception;

    /**
     * 更新活动
     * @param entity 活动实体
     * @throws Exception
     */
    public void activityUpdate(YBKActivityEntity entity) throws Exception;

    /**
     * 删除活动
     * @param id 活动编号
     * @throws Exception
     */
    public void activityDel(Long id) throws Exception;

    /**
     * 查询单条活动
     * @param id 活动编号
     * @return YBKActivityEntity
     * @throws Exception
     */
    public YBKActivityEntity queryActivityById(Long id) throws Exception;
}

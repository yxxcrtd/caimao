package com.caimao.gjs.api.service;

/**
 * 文章抓取接口
 *
 * Created by yangxinxin@huobi.com on 2015/10/13
 */
public interface IArticleJobService {

    /**
     * 抓取快讯通新闻的接口
     */
    void catchArticle();

    /**
     * 抓取黄金头条资讯的接口
     */
    void catchGoldTouTiaoNews();

    /**
     * 抓取金十首页资讯
     */
    void catchJin10();

}

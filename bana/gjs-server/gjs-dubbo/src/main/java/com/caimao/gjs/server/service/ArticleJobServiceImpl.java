package com.caimao.gjs.server.service;

import com.caimao.gjs.api.entity.article.GjsArticleEntity;
import com.caimao.gjs.api.entity.article.GjsArticleJin10Entity;
import com.caimao.gjs.api.enums.EGJSArticleCategory;
import com.caimao.gjs.api.service.IArticleJobService;
import com.caimao.gjs.api.utils.DateUtil;
import com.caimao.gjs.server.dao.article.GjsArticleDAO;
import com.caimao.gjs.server.utils.HttpClientUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 文章、新闻或资讯的抓取接口的实现
 *
 * Created by yangxinxin@huobi.com on 2015/10/22
 */
@Service("articleJobService")
public class ArticleJobServiceImpl implements IArticleJobService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleJobServiceImpl.class);
    private static final String targetURL = "http://www.kxt.com/news";
    private static final String URL_GOLD_TOUTIAO_NEWS = "http://www.goldtoutiao.com/news/list?cid=1";
    private static final String URL_JIN_10 = "http://www.jin10.com";

    @Autowired
    private GjsArticleDAO gjsArticleDAO;

    /**
     * 抓取快讯通新闻
     */
    @Override
    public void catchArticle() {
        logger.info("开始抓取快讯通新闻 .....");
        String result = HttpClientUtils.getString(targetURL);
        if ("".equals(result)) {
            logger.info("没有抓取到任何信息，可能是地址不对！");
        } else {
            try {
                htmlParser(result);
                logger.info("抓取成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析快讯通新闻
     *
     * @param originUrl
     * @throws Exception
     */
    private void htmlParser(String originUrl) throws Exception {
        // 1，找到文章所在的ul节点
        Parser parserUl = Parser.createParser(originUrl, "UTF-8");
        TagNameFilter filterUl = new TagNameFilter("ul"); // 抓取的文章列表在ul中
        NodeList nodeListUl = parserUl.parse(filterUl);
        Node nodeUl = nodeListUl.elementAt(1); // 在第二个ul节点内
        String htmlUl = nodeUl.getChildren().toHtml();

        // 2，找到其中有详情页地址的节点
        Parser parserLink = Parser.createParser(htmlUl, "UTF-8");
        NodeFilter filterLink = new TagNameFilter("h5");
        NodeList nodeListLink = parserLink.parse(filterLink);
        // for (int i = 0; i < nodeListLink.size(); i++) { logger.info(nodeListLink.elementAt(i).toHtml()); // toHtml toPlainTextString }

        // 3，获取详情页的URL
        Parser parserLinkUrl = Parser.createParser(nodeListLink.toHtml(), "UTF-8");
        NodeFilter filterLinkUrl = new TagNameFilter("a");
        NodeList nodeListLinkUrl = parserLinkUrl.parse(filterLinkUrl);
        for (int i = 0; i < nodeListLinkUrl.size(); i++) {
            Node node = nodeListLinkUrl.elementAt(i);
            if (node instanceof LinkTag) {
                LinkTag linkTag = (LinkTag) node;
                String link = linkTag.getLink();
                // 每个详情页的URL
                String url = targetURL + link.substring(link.lastIndexOf("/"));

                List<GjsArticleEntity> obj = this.gjsArticleDAO.queryArticleByTitleAndSourceUrl(GJSArticleServiceHelper.getTitleByURL(url), url);
                if (null != obj && 0 == obj.size()) {
                    GjsArticleEntity gjsArticleEntity = new GjsArticleEntity();
                    gjsArticleEntity.setCategoryId(EGJSArticleCategory.ARTICLE.getCode());
                    gjsArticleEntity.setSourceName("快讯通");
                    gjsArticleEntity.setSourceUrl(url);
                    gjsArticleEntity.setIsHot(1);
                    gjsArticleEntity.setTitle(GJSArticleServiceHelper.getTitleByURL(url));
                    gjsArticleEntity.setSummary(GJSArticleServiceHelper.getSummaryByURL(url));
                    gjsArticleEntity.setContent(GJSArticleServiceHelper.getContentByURL(url).substring(6).replace(GJSArticleServiceHelper.getSummaryByURL(url), ""));
                    gjsArticleEntity.setSort(0);
                    gjsArticleEntity.setViewCount(0);
                    gjsArticleEntity.setCreated(GJSArticleServiceHelper.getCreateTimeByURL(url));
                    this.gjsArticleDAO.insert(gjsArticleEntity);
                }
            }
        }
    }

    /**
     * 抓取黄金头条的资讯
     */
    @Override
    public void catchGoldTouTiaoNews() {
        logger.info("开始抓取黄金头条的资讯 ......");
        String result = HttpClientUtils.getString(URL_GOLD_TOUTIAO_NEWS);

        if ("".equals(result)) {
            logger.info("没有抓取到任何资讯信息，可能是地址不对！");
        } else {
            try {
                parseGoldTouTiaoNews(result);
                logger.info("黄金头条的资讯抓取成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析黄金头条的资讯
     *
     * @param originUrl 原始地址
     * @throws Exception
     */
    private void parseGoldTouTiaoNews(String originUrl) throws Exception {
        // 1，找到资讯所在的ul节点
        Parser parserUl = Parser.createParser(originUrl, "UTF-8");
        TagNameFilter filterUl = new TagNameFilter("ul"); // 要抓取的资讯列表在ul中
        NodeList nodeListUl = parserUl.parse(filterUl);
        Node nodeUl = nodeListUl.elementAt(1); // 在第二个ul节点内
        String htmlUl = nodeUl.getChildren().toHtml();
        // 2，找到其中有详情页地址的节点
        Parser parserLink = Parser.createParser(htmlUl, "UTF-8");
        NodeFilter filterLink = new TagNameFilter("a");
        NodeList nodeListLink = parserLink.parse(filterLink);
        // 3，获取详情页的URL
        Parser parserLinkUrl = Parser.createParser(nodeListLink.toHtml(), "UTF-8");
        NodeFilter filterLinkUrl = new TagNameFilter("a");
        NodeList nodeListLinkUrl = parserLinkUrl.parse(filterLinkUrl);
        for (int i = 0; i < nodeListLinkUrl.size(); i++) {
            Node node = nodeListLinkUrl.elementAt(i);
            if (node instanceof LinkTag) {
                LinkTag linkTag = (LinkTag) node;
                String link = linkTag.getLink();
                // 每个详情页的URL，String url = "http://www.goldtoutiao.com/post/388e40cc9d94ae23bbc79e77d630ead3";String url = "http://www.goldtoutiao.com/post/64419aaaa47852b32b8f6071102a9d8e";
                String url = URL_GOLD_TOUTIAO_NEWS.substring(0, URL_GOLD_TOUTIAO_NEWS.indexOf("/news")) + "/post" + link.substring(link.lastIndexOf("/"));

                List<GjsArticleEntity> obj = this.gjsArticleDAO.queryArticleByTitleAndSourceUrl(GJSArticleServiceHelper.getGoldTouTiaoTitleByURL(url), url);
                if (null != obj && 0 == obj.size()) {
                    GjsArticleEntity entity = new GjsArticleEntity();
                    entity.setCategoryId(EGJSArticleCategory.ARTICLE.getCode());
                    entity.setSourceName(GJSArticleServiceHelper.getGoldTouTiaoSourceNameByURL(url));
                    entity.setSourceUrl(url);
                    entity.setIsHot(0); // 0 不热门；1 热门
                    entity.setTitle(GJSArticleServiceHelper.getGoldTouTiaoTitleByURL(url));
                    String summary = GJSArticleServiceHelper.getGoldTouTiaoSummaryByURL(url).trim();
                    entity.setSummary(summary);
                    String content = GJSArticleServiceHelper.getGoldTouTiaoContentByURL(url).trim();
                    if (0 < content.length() && null != content && !"".equals(content)) {
                        entity.setContent(content);
                        // 如果摘要为空的话，将内容的第一段作为摘要
                        if (null == summary || 0 == summary.length() || "".equals(summary)) {
                            summary = content.substring(0, content.indexOf("</p>")).replaceAll("<([^>]*)>", "").replace(" ", "");
                            entity.setSummary("摘要：" + (256 < summary.length() ? summary.substring(0, 256) + "..." : summary));
                        }
                    } else {
                        continue;
                    }
                    entity.setSort(0);
                    entity.setViewCount(GJSArticleServiceHelper.getGoldTouTiaoViewCountByURL(url));
                    entity.setCreated(DateUtil.convertDateToString(DateUtil.DATA_TIME_PATTERN_1, DateUtil.strToDate(GJSArticleServiceHelper.getGoldTouTiaoTimeByURL(url), DateUtil.DATA_TIME_PATTERN_5)));
                    this.gjsArticleDAO.insert(entity);
                }
            }
        }
    }

    /**
     * 抓取金十首页资讯
     */
    @Override
    public void catchJin10() {
        Document doc = null;
        try {
            // 生成 Document 对象
            doc = Jsoup.connect(URL_JIN_10).get();
        } catch (IOException e) {
            logger.error("{}", e);
            return;
        }
        if (null != doc) {
            Elements news = doc.select("#newslist").select(".newsline");
            for (int i = 0; i < news.size(); i++) {
                GjsArticleJin10Entity entity = new GjsArticleJin10Entity();
                Elements tds = news.get(i).select("td");
                String idValue = news.get(i).attr("id");
                int tdCount = tds.size();
                if (null != this.gjsArticleDAO.selectByTimeId(idValue)) {
                    break;
                }
                entity.setTimeId(idValue);
                entity.setTime(idValue.substring(8, 10) + ":" + idValue.substring(10, 12));

                for (int j = 0; j < tdCount; j++) {
                    if (0 == j) {
                        entity.setCategory(GJSArticleServiceHelper.getCategoryByImgTitle(tds.get(j).select("a").select("img").attr("title")));
                    } else {
                        if (10 == tdCount) {
                            if (5 == j) {
                                entity.setContent(tds.get(j).text());
                            } else if (6 == j) {
                                entity.setStar(Integer.valueOf(tds.get(j).select("img").attr("title").substring(0, 1)));
                            } else if (7 == j) {
                                entity.setInfact(tds.get(j).text().substring(3, tds.get(j).text().length()));
                            } else if (8 == j) {
                                entity.setResult(GJSArticleServiceHelper.getResultByImgTitle(tds.get(j).select("div").select("span").first().text()));
                            } else if (9 == j) {
                                String s = tds.get(j).text();
                                entity.setBeforeValue(s.substring(3, s.lastIndexOf(" ")));
                                entity.setExpect(s.substring(s.lastIndexOf(" ") + 4, s.length()));
                            }
                        } else {
                            if (2 == j) {
                                entity.setContent(tds.get(j).text());
                            }
                        }
                    }
                }
                if (!"".equals(entity.getContent())) {
                    this.gjsArticleDAO.insert(entity);
                }
            }
            logger.info("金十首页抓取成功！");
        }
    }

}

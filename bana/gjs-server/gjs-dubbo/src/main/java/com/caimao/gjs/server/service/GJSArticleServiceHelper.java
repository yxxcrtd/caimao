package com.caimao.gjs.server.service;

import com.caimao.gjs.api.enums.EGJSArticleJin10Category;
import com.caimao.gjs.api.enums.EGJSArticleJin10Result;
import com.caimao.gjs.server.utils.DateUtil;
import com.caimao.gjs.server.utils.HttpClientUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ArticleService的帮助类
 *
 * Created by yangxinxin@huobi.com on 2015/10/22
 */
public class GJSArticleServiceHelper {
    private static final Logger logger = LoggerFactory.getLogger(GJSArticleServiceHelper.class);

    /**
     * 格式化时间，3秒钟前，6分钟前，8天前；超过3天的只显示年月日
     *
     * @param time
     * @return
     * @throws ParseException
     */
    protected static final String getPrettyTime(String time, String f) throws ParseException {
        String returnString = "";
        SimpleDateFormat format = null;
        if (null != f && !"".equals(f)) {
            format = new SimpleDateFormat("yyyyMMddHHmmss");
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        Date date = format.parse(time);
        Date now = new Date();
        // 超过3天的只显示年月日
        if (3 < DateUtil.countDays(time, DateUtil.getNowTime1(), null != f && !"".equals(f) ? DateUtil.DATA_TIME_PATTERN_3 : DateUtil.DATA_TIME_PATTERN_1)) {
            returnString = DateUtil.formateDateStrT(time);
        } else {
            PrettyTime t = new PrettyTime(Locale.CHINESE);
            t.format(now);
            t.setLocale(Locale.CHINESE); // 只显示中文的格式
            returnString = t.format(date).replaceAll(" ", "");
        }
        return returnString;
    }

    /**
     * 根据时间返回：今天，昨天，11月21日，11月20日 ...
     *
     * @param time
     * @return
     * @throws Exception
     */
    protected static final String getLabelTime(String time) throws Exception {
        if (isSameDay(DateUtil.convertStringToDate(time), new Date())) {
            logger.info(" {} - 今天", time);
            return "今天";
        }

        if (isSameDay(DateUtil.convertStringToDate(time), DateUtil.addDays(new Date(), -1))) {
            logger.info(" {} - 昨天", time);
            return "昨天";
        } else {
            logger.info(" {} - " + time.substring(5, 7) + "月" + time.substring(8, 10) + "日", time);
            return time.substring(5, 7) + "月" + time.substring(8, 10) + "日";
        }
    }

    /**
     * 比较是不是同一天
     *
     * @param d1
     * @param d2
     * @return
     */
    private static boolean isSameDay(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据url获取标题
     *
     * @param url
     * @return
     * @throws ParserException
     */
    protected static String getTitleByURL(String url) throws ParserException {
        String detail = HttpClientUtils.getString(url);
        Parser parser = Parser.createParser(detail, "UTF-8");
        NodeFilter nodeFilter = new TagNameFilter("h1");
        NodeList nodeList = parser.parse(nodeFilter);
        return nodeList.asString();
    }

    /**
     * 根据url获取发布时间
     *
     * @param url
     * @return
     * @throws ParserException
     */
    protected static String getCreateTimeByURL(String url) throws ParserException {
        String detail = HttpClientUtils.getString(url);
        Parser parser = Parser.createParser(detail, "UTF-8");
        NodeFilter nodeFilter = new CssSelectorNodeFilter("div[class='g_tag']");
        NodeList nodeList = parser.parse(nodeFilter);
        Node node = nodeList.elementAt(0);
        return node.getChildren().elementAt(2).toHtml();
    }

    /**
     * 根据url获取摘要
     *
     * @param url
     * @return
     * @throws ParserException
     */
    protected static String getSummaryByURL(String url) throws ParserException {
        String detail = HttpClientUtils.getString(url);
        Parser parser = Parser.createParser(detail, "UTF-8");
        NodeFilter nodeFilter = new CssSelectorNodeFilter("p[class='summary']");
        NodeList nodeList = parser.parse(nodeFilter);
        Node node = nodeList.elementAt(0);
        return node.getChildren().elementAt(3).toHtml();
    }

    /**
     * 根据url获取内容
     *
     * @param url
     * @return
     * @throws ParserException
     */
    protected static String getContentByURL(String url) throws ParserException {
        String detail = HttpClientUtils.getString(url);
        Parser parser = Parser.createParser(detail, "UTF-8");
        NodeFilter nodeFilter = new CssSelectorNodeFilter("div[class='nei content snei'] p");
        NodeList nodeList = parser.parse(nodeFilter);
        return nodeList.asString();
    }

    /**
     * 根据黄金头条详情页的url获取资讯的标题
     *
     * @param url
     * @return
     * @throws ParserException
     */
    protected static String getGoldTouTiaoTitleByURL(String url) throws Exception {
//        String detail = HttpClientUtils.getString(url);
//        Parser parser = new Parser();
//        parser.setURL(url);
//        parser.setEncoding("UTF-8");
//        NodeFilter nodeFilter = new CssSelectorNodeFilter("div[id='main'] h2[class='title']");
//        NodeList nodeList = parser.parse(nodeFilter);
//        return nodeList.asString();

        String retrunString = "";
        Document doc = Jsoup.connect(url).get();
        if (null != doc) {
            Elements title = doc.select("#main").select("h2").select(".title");
            retrunString = title.text();
        }
        return retrunString;
    }

    /**
     * 根据黄金头条详情页的url获取资讯的发表时间
     *
     * @param url
     * @return
     * @throws ParserException
     */
    protected static String getGoldTouTiaoTimeByURL(String url) throws ParserException {
        String detail = HttpClientUtils.getString(url);
        Parser parser = new Parser();
        parser.setURL(url);
        parser.setEncoding("UTF-8");
        NodeFilter nodeFilter = new CssSelectorNodeFilter("div[id='main'] span[class='item time']");
        NodeList nodeList = parser.parse(nodeFilter);
        return nodeList.asString();
    }

    /**
     * 根据黄金头条详情页的url获取资讯的来源
     *
     * @param url
     * @return
     * @throws ParserException
     */
    protected static String getGoldTouTiaoSourceNameByURL(String url) throws ParserException {
        String detail = HttpClientUtils.getString(url);
        Parser parser = new Parser();
        parser.setURL(url);
        parser.setEncoding("UTF-8");
        NodeFilter nodeFilter = new CssSelectorNodeFilter("div[id='main'] span[class='item source right']");
        NodeList nodeList = parser.parse(nodeFilter);
        return nodeList.asString().substring(3, nodeList.asString().length());
    }

    /**
     * 根据黄金头条详情页的url获取资讯的摘要
     *
     * @param url
     * @return
     * @throws ParserException
     */
    protected static String getGoldTouTiaoSummaryByURL(String url) throws ParserException {
        String detail = HttpClientUtils.getString(url);
        Parser parser = new Parser();
        parser.setURL(url);
        parser.setEncoding("UTF-8");
        NodeFilter nodeFilter = new CssSelectorNodeFilter("div[id='main'] div[class='summary']");
        NodeList nodeList = parser.parse(nodeFilter);
        return nodeList.asString().trim();
    }

    /**
     * 根据黄金头条详情页的url获取资讯的全文
     *
     * @param url
     * @return
     * @throws ParserException
     */
    protected static int getGoldTouTiaoViewCountByURL(String url) throws ParserException {
        String detail = HttpClientUtils.getString(url);
        Parser parser = new Parser();
        parser.setURL(url);
        parser.setEncoding("UTF-8");
        NodeFilter nodeFilter = new CssSelectorNodeFilter("div[id='main'] span[id='js-read-times-text']");
        NodeList nodeList = parser.parse(nodeFilter);
        return Integer.parseInt(nodeList.asString());
    }

    /**
     * 根据黄金头条详情页的url获取资讯的全文
     *
     * @param url
     * @return
     * @throws ParserException
     */
    protected static String getGoldTouTiaoContentByURL(String url) throws ParserException, IOException {
        Document doc = Jsoup.connect(url).get();
        Elements Econtent = doc.select("#main").select(".content");
        // Econtent.select("p").first().remove(); // 删除第一个p节点
        Econtent.select("p").select("iframe").parents().remove();
        // 过滤不必要的信息
        for (Element Ep: Econtent.select("p,div,a,strong,span,img")) {
            Ep.removeAttr("class");
            Ep.removeAttr("style");
            Ep.removeAttr("align");
            Ep.removeAttr("height");
            Ep.removeAttr("width");
        }

        // 删除包含“黄金头条”的p段落
        List<org.jsoup.nodes.Node> nodes = null;
        for (Element ee: Econtent.select("p")) {
            if (0 < ee.text().indexOf("黄金头条")) {
                nodes = ee.childNodes();
                System.out.println("找到了包含“黄金头条”的关键字：" + nodes);
                ee.remove();
            }
            if (0 < ee.select("a").select("img").attr("src").indexOf("goldtoutiao")) {
                nodes = ee.childNodes();
                System.out.println("找到了在图片地址中包含“goldtoutiao”的关键字：" + nodes);
                ee.remove();
            }
        }
        return Econtent.html();

        // htmlparser方式解析
//        String detail = HttpClientUtils.getString(url);
//        Parser parser = new Parser();
//        parser.setURL(url);
//        parser.setEncoding("UTF-8");
//        NodeFilter nodeFilter = new CssSelectorNodeFilter("div[id='main'] div[class='content']");
//        NodeList nodeList = parser.parse(nodeFilter);
//        return nodeList.toHtml().replace(filterString1, "");
    }

    /**
     * 根据图片标题获取资讯的分类
     *
     * @param title
     * @return
     * @throws ParserException
     */
    protected static int getCategoryByImgTitle(String title) {
        int returnValue;
        switch(title) {
            case "一般新闻":
                returnValue = EGJSArticleJin10Category.NOMARLNEWS.getCode();
                break;
            case "普通新闻":
                returnValue = EGJSArticleJin10Category.COMMONNEWS.getCode();
                break;
            case "重要新闻":
                returnValue = EGJSArticleJin10Category.IMPORTANTNEWS.getCode();
                break;
            case "一般数据":
                returnValue = EGJSArticleJin10Category.NOMARLDATA.getCode();
                break;
            case "重要数据":
                returnValue = EGJSArticleJin10Category.IMPORTANTDATA.getCode();
                break;
            default:
                returnValue = EGJSArticleJin10Category.NOMARLNEWS.getCode();
                break;
        }
        return returnValue;
    }

    /**
     * 根据图片标题获取资讯的分析结果
     *
     * @param title
     * @return
     * @throws ParserException
     */
    protected static int getResultByImgTitle(String title) {
        int returnValue;
        switch(title) {
            case "利空":
                returnValue = EGJSArticleJin10Result.LIKONG.getCode();
                break;
            case "利空2":
                returnValue = EGJSArticleJin10Result.LIKONG2.getCode();
                break;
            case "利多":
                returnValue = EGJSArticleJin10Result.LIDUO.getCode();
                break;
            case "利多2":
                returnValue = EGJSArticleJin10Result.LIDUO2.getCode();
                break;
            case "无影响":
                returnValue = EGJSArticleJin10Result.SMALL1.getCode();
                break;
            case "无影响2":
                returnValue = EGJSArticleJin10Result.SMALL2.getCode();
                break;
            case "影响较小":
                returnValue = EGJSArticleJin10Result.SMALL3.getCode();
                break;
            case "原油利多2":
                returnValue = EGJSArticleJin10Result.CRUDE4.getCode();
                break;
            case "原油利空":
                returnValue = EGJSArticleJin10Result.CRUDE5.getCode();
                break;
            default:
                returnValue = EGJSArticleJin10Result.LIKONG.getCode();
                break;
        }
        return returnValue;
    }

}

package com.caimao.gjs.server.utils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.PropertyResourceBundle;
import java.util.Random;
import java.util.ResourceBundle;


/**
 * @author yanjg
 */
public class Constants {
    private static final Logger logger = LoggerFactory.getLogger(Constants.class);
    private static ResourceBundle appBundle = PropertyResourceBundle.getBundle("META-INF/conf/application");

    //南交所系统账号
    public static final String NJS_ADMIN_USER_ID = appBundle.getString("NJS_ADMIN_USER_ID");
    public static final String NJS_ADMIN_USER_PWD = appBundle.getString("NJS_ADMIN_USER_PWD");

    //南交所http协议地址
    public static String[] getHTTPServer(){
        String njs_trade_http = appBundle.getString("njs_trade_http");
        String[] httpServer = njs_trade_http.split("\\|");
        Random random = new Random();
        String pluginName = httpServer[random.nextInt(httpServer.length)];
        return pluginName.split(":");
    }

    //南交所http协议地址
    public static String[] getHTTPServerAll(){
        String njs_trade_http = appBundle.getString("njs_trade_http_all");
        String[] httpServer = njs_trade_http.split("\\|");
        Random random = new Random();
        String pluginName = httpServer[random.nextInt(httpServer.length)];
        return pluginName.split(":");
    }

    public static final String NJS_TRADE_URL = appBundle.getString("njs_trade_url");

    public static final String NJS_TRADE_URL_ALL = appBundle.getString("njs_trade_url_all");

    //上金所图片上传地址
    public static final String SJS_UPLOAD_URL = appBundle.getString("sjs_upload_url");

    //上金所图片审核查询地址
    public static final String SJS_QUERY_URL = appBundle.getString("sjs_query_url");

    //南交所手续费
    public static final String NJS_TRADE_FEE = appBundle.getString("njs_trade_fee");

    //上金所手续费
    public static final String SJS_TRADE_FEE = appBundle.getString("sjs_trade_fee");

    //上金所银行编号
    public static final String SJS_BANK_NO = appBundle.getString("sjs_bank_no");

    //上金所业务代码
    public static final String SJS_BRANCH_ID = appBundle.getString("sjs_branch_id");

    //上金所操作员
    public static final String SJS_OP_USER = appBundle.getString("sjs_op_user");

    //上金所渠道类型
    public static final String SJS_TERM_TYPE = appBundle.getString("sjs_term_type");
}
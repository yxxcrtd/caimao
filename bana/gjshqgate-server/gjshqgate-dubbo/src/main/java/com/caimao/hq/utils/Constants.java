/*
 * Timeloit.com Inc.
 * Copyright (c) 2012 
 * All Rights Reserved
 */
package com.caimao.hq.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * @author yanjg
 */
public class Constants {
    private static final Logger logger = LoggerFactory.getLogger(Constants.class);
    private static ResourceBundle appBundle = PropertyResourceBundle.getBundle("META-INF/conf/application");
    // 是否是测试环境
    public static final boolean IS_DEBUG = Boolean.valueOf(appBundle.getString("isDebug"));
    //用户邀请有效期（天）
    public static final int USER_INVITE_EXPIRE_DAY = 3;
    //发短信重试次数
    public static final int SMS_SEND_RETRY_COUNT = 3;
    /** 汇付宝相关的配置 BEGIN **/
    // 汇付宝是否测试的参数
    public static final String HEEPAY_TEST = appBundle.getString("heepayTest");
    // 汇付宝充值回调的地址
    public static final String HEEPAY_PAY_SYNC_URL = appBundle.getString("heepayPaySyncUrl");
    // 汇付宝充值异步回调的地址
    public static final String HEEPAY_PAY_ASYNC_URL = appBundle.getString("heepayPayAsyncUrl");
    // 汇付宝批付的回调URL配置
    public static final String HEEPAY_BATCH_NOTIFY_URL = appBundle.getString("heepayBatchNotifyUrl");
    // 汇付宝批付请求的接口地址
    public static final String HEEPAY_BATCH_WORK_REQUEST_URL = appBundle.getString("heepayBatchWorkRequestUrl");
    // 汇付宝大额批付请求的接口地址
    public static final String HEEPAY_BATCH_REQUEST_URL = appBundle.getString("heepayBatchRequestUrl");
    // 汇付宝支付查询请求接口地址
    public static final String HEEPAY_PAY_CHECK_REQUEST_URL = appBundle.getString("heepayPayCheckRequestUrl");
    // 汇付宝批付KEY
    public static final String HEEPAY_BATCH_KEY = appBundle.getString("heepayBatchKey");
    // 汇付宝批付查询请求接口地址
    public static final String HEEPAY_BATCH_CHECK_REQUEST_URL = appBundle.getString("heepayBatchCheckRequestUrl");
    /** 汇付宝相关的配置 END **/

    /** 易宝的配置项 **/
    // 易宝商户号
    public static final String YEEPAY_MERCHANT_ACCOUNT = appBundle.getString("yeepayMerchantAccount");
    // 易宝手机充值页面回调
    public static final String YEEPAY_MOBILE_FCALBACKURL = appBundle.getString("yeepayMobileFCallBackUrl");
    // 易宝手机充值异步回调
    public static final String YEEPAY_MOBILE_CALBACKURL = appBundle.getString("yeepayMobileCallBackUrl");
    // 易宝手机充值提交地址
    public static final String YEEPAY_MOBILE_PAY_URL = appBundle.getString("yeepayMobilePayUrl");
    // 易宝手机充值查询地址
    public static final String YEEPAY_MOBILE_QUERY_URL = appBundle.getString("yeepayMobileQueryUrl");
    // 易宝手机支付商户公钥
    public static final String YEEPAY_MOBILE_MERC_PUB_KEY = appBundle.getString("yeepayMobilepayMerchantPublickey");
    // 易宝手机支付商户私钥
    public static final String YEEPAY_MOBILE_MERC_PRI_KEY = appBundle.getString("yeepayMobilepayMerchantPrivatekey");
    // 易宝手机支付易宝公钥
    public static final String YEEPAY_MOBILE_YIBAO_PUB_KEY = appBundle.getString("yeepayMobilepayYibaoPublickey");
    /** 易宝的配置项 END **/

    // 提现是否冻结资产
    public static final Boolean WITHDRAW_FROZEN = Boolean.valueOf(appBundle.getString("withdrawFrozen"));
    public static final Boolean WITHDRAW_CHECK = Boolean.valueOf(appBundle.getString("withdrawCheck"));

    // 实名认证
    public static final boolean TRUST_ID5TEST = Boolean.valueOf(appBundle.getString("trunstId5Test"));

    // P2P融资借贷的产品ID
    public static final Long P2P_LOAN_PZ_PRODUCT_ID = Long.valueOf(appBundle.getString("p2pLoanPZProductID"));
}
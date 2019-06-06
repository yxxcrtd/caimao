package com.caimao.bana.server.utils;

import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.caimao.bana.api.entity.TpzChargeOrderEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.utils.yeepay.AES;
import com.caimao.bana.server.utils.yeepay.EncryUtil;
import com.caimao.bana.server.utils.yeepay.RSA;
import com.caimao.bana.server.utils.yeepay.RandomUtil;
import com.huobi.commons.utils.HttpUtil;
import com.huobi.commons.utils.JsonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * 易宝充值相关功能方法
 * Created by WangXu on 2015/5/21.
 */
@Component
public class YeepayChargeUtils {
    private static final Logger logger = LoggerFactory.getLogger(YeepayChargeUtils.class);

    /**
     * 生成手机充值的URL
     * @param userEntity    用户信息
     * @param chargeOrderEntity 充值订单
     * @param userIP    用户IP
     * @param userUA    浏览器标示
     * @return  支付URL
     * @throws Exception
     */
    public String createMobileChargeUrl(TpzUserEntity userEntity, TpzChargeOrderEntity chargeOrderEntity, String userIP, String userUA) throws Exception {
        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("merchantaccount", Constants.YEEPAY_MERCHANT_ACCOUNT);
        dataMap.put("orderid", String.valueOf(chargeOrderEntity.getOrderNo()));
        dataMap.put("transtime", System.currentTimeMillis() / 1000);
        dataMap.put("amount", chargeOrderEntity.getOrderAmount());
        dataMap.put("currency", 156);
        dataMap.put("productcatalog", "18");    // TODO 金融投资
        dataMap.put("productname", chargeOrderEntity.getOrderName());
        dataMap.put("productdesc", chargeOrderEntity.getOrderAbstract());
        dataMap.put("identitytype", 2);
        dataMap.put("identityid", chargeOrderEntity.getUserId().toString());
        dataMap.put("terminaltype", 3);
        dataMap.put("terminalid", chargeOrderEntity.getPzAccountId().toString());
        dataMap.put("userip", userIP);
        dataMap.put("userua", userUA);
        dataMap.put("fcallbackurl", Constants.YEEPAY_MOBILE_FCALBACKURL);
        dataMap.put("callbackurl", Constants.YEEPAY_MOBILE_CALBACKURL);
        dataMap.put("paytypes", "1");
        dataMap.put("orderexpdate", 60);
        dataMap.put("idcardtype", "01");
        dataMap.put("idcard", userEntity.getIdcard());
        dataMap.put("owner", userEntity.getUserRealName());
        String sign = EncryUtil.handleRSA(dataMap, Constants.YEEPAY_MOBILE_MERC_PRI_KEY);
        dataMap.put("sign", sign);

        String merchantAESKey = RandomUtil.getRandom(16);
        String jsonStr = JSON.toJSONString(dataMap);
        this.logger.info("易宝手机支付请求原字符串：{}", jsonStr);
        String data = AES.encryptToBase64(jsonStr, merchantAESKey);
        this.logger.info("易宝手机支付请求加密串：{}", data);
        // 使用RSA算法将商户自己随机生成的AESkey加密
        String encryptkey = RSA.encrypt(merchantAESKey, Constants.YEEPAY_MOBILE_YIBAO_PUB_KEY);
        this.logger.info("易宝手机支付请求加密串：{}", encryptkey);

        String payUrl = Constants.YEEPAY_MOBILE_PAY_URL + "?" + "merchantaccount="
                + URLEncoder.encode(Constants.YEEPAY_MERCHANT_ACCOUNT, "UTF-8") + "&data="
                + URLEncoder.encode(data, "UTF-8") + "&encryptkey="
                + URLEncoder.encode(encryptkey, "UTF-8");
        this.logger.info("易宝手机充值生成的URL:{}", payUrl);

        return payUrl;
    }

    /**
     * 解密易宝手机支付返回的数据
     * @param data  密文数据
     * @param encryptkey 密文数据
     * @return  返回值
     * @throws Exception
     */
    public Map<String, Object> decryptYeepayReturnData(String data, String encryptkey) throws Exception {
        String yeepayAESKey = RSA.decrypt(encryptkey, Constants.YEEPAY_MOBILE_MERC_PRI_KEY);
        this.logger.info("易宝手机充值返回值，yeepayAESKey {}", yeepayAESKey);
        String decryptData = AES.decryptFromBase64(data, yeepayAESKey);
        this.logger.info("易宝手机充值返回解密数据  {}", decryptData);
        Map<String, Object> decryptDataMap = JSON.parseObject(decryptData, TreeMap.class);
        return decryptDataMap;
    }

    /**
     * 查询手机支付结果
     * @param chargeOrderEntity 支付订单信息
     * @return  返回值
     * @throws Exception
     */
    public Map<String, Object> queryYeepayMobilePayRes(TpzChargeOrderEntity chargeOrderEntity) throws Exception {
        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("merchantaccount", Constants.YEEPAY_MERCHANT_ACCOUNT);
        //dataMap.put("transtime", System.currentTimeMillis() / 1000);
        dataMap.put("orderid", String.valueOf(chargeOrderEntity.getOrderNo()));
        String sign = EncryUtil.handleRSA(dataMap, Constants.YEEPAY_MOBILE_MERC_PRI_KEY);
        dataMap.put("sign", sign);

        String merchantAESKey = RandomUtil.getRandom(16);
        String jsonStr = JSON.toJSONString(dataMap);
        this.logger.info("易宝手机支付查询原字符串：{}", jsonStr);
        String data = AES.encryptToBase64(jsonStr, merchantAESKey);
        this.logger.info("易宝手机支付查询加密串：{}", data);
        // 使用RSA算法将商户自己随机生成的AESkey加密
        String encryptkey = RSA.encrypt(merchantAESKey, Constants.YEEPAY_MOBILE_YIBAO_PUB_KEY);
        this.logger.info("易宝手机支付查询请求加密串：{}", encryptkey);
        String payUrl = Constants.YEEPAY_MOBILE_QUERY_URL + "?" + "merchantaccount="
                + URLEncoder.encode(Constants.YEEPAY_MERCHANT_ACCOUNT, "UTF-8") + "&data="
                + URLEncoder.encode(data, "UTF-8") + "&encryptkey="
                + URLEncoder.encode(encryptkey, "UTF-8");
        this.logger.info("易宝手机充值查询生成的URL:{}", payUrl);

        String payRes = HttpUtil.doGet(payUrl);
        this.logger.info("易宝手机充值查询返回值：{}", payRes);

        Map<String, Object> payMap = JsonUtil.toObject(payRes, Map.class);

        if (payMap.get("data") == null) {
            throw new CustomerException("易宝返回查询信息解析错误", 888888);
        }
        // 解密返回的值并返回
        return this.decryptYeepayReturnData(payMap.get("data").toString(), payMap.get("encryptkey").toString());
    }

}

package com.caimao.bana.server.utils;

import com.caimao.bana.api.entity.TpzChargeOrderEntity;
import com.caimao.bana.api.entity.TpzPayChannelEntity;
import com.caimao.bana.api.entity.TpzWithdrawOrderEntity;
import com.caimao.bana.api.entity.req.FAccountPreChargeReq;
import com.caimao.bana.common.api.exception.CustomerException;
import com.hsnet.pz.core.exception.BizServiceException;
import com.huobi.commons.utils.HttpUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 充值相关通用方法
 * Created by WangXu on 2015/4/23.
 */
@Component
public class ChannelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ChannelUtil.class);

    /**
     * 生成汇付宝提交的请求数据
     * @param tpzChargeOrderEntity 充值订单信息
     * @param tpzPayChannelEntity  支付通道信息
     * @param accountPreChargeReq   请求信息
     * @return 返回汇付宝提交表单信息
     * @throws Exception
     */
    public Map<String, Object> convertHeepaySubmitEntity(TpzChargeOrderEntity tpzChargeOrderEntity, TpzPayChannelEntity tpzPayChannelEntity, FAccountPreChargeReq accountPreChargeReq) throws Exception {
        Date date = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Map<String, Object> heepaySubmitMap = new TreeMap<>();
        heepaySubmitMap.put("version", "1");     // 设置版本号
        heepaySubmitMap.put("isPhone", "0");     // 是否使用手机版
        heepaySubmitMap.put("payType", "20");    // 使用银行卡支付
        heepaySubmitMap.put("payCode", StringUtils.isEmpty(tpzChargeOrderEntity.getBankCode()) ? "0" : tpzChargeOrderEntity.getBankCode());  // 设置支付的银行代号
        heepaySubmitMap.put("agentId", tpzPayChannelEntity.getMerchantId());     // 设置商户的编号
        heepaySubmitMap.put("agentBillId", String.valueOf(tpzChargeOrderEntity.getOrderNo()));   // 设置商户内部订单号
        heepaySubmitMap.put("payAmt", this.longToString(tpzChargeOrderEntity.getOrderAmount()));  // 设置充值数量
        heepaySubmitMap.put("notifyUrl", Constants.HEEPAY_PAY_ASYNC_URL);   // 设置异步回调通知接口
        heepaySubmitMap.put("returnUrl", Constants.HEEPAY_PAY_SYNC_URL);    // 同步回调接口
        heepaySubmitMap.put("userIp", accountPreChargeReq.getUserIp().replace(".", "_"));   // 获取用户的请求的IP
        heepaySubmitMap.put("agentBillTime", dataFormat.format(date));    // 设置提交的时间
        heepaySubmitMap.put("goodsName", tpzChargeOrderEntity.getOrderName());  // 商品名称
        heepaySubmitMap.put("goodsNum", "1");    // 产品数量
        heepaySubmitMap.put("remark", "财猫充值" + String.valueOf(tpzChargeOrderEntity.getOrderNo()));// 备注
        heepaySubmitMap.put("goodsNote", "");    // 支付说明
        heepaySubmitMap.put("submitUrl", tpzPayChannelEntity.getBusinessWebgateway());   // 设置提交地址
        String sign = "";
        if ("1".equals(Constants.HEEPAY_TEST)) {
            heepaySubmitMap.put("isTest", Constants.HEEPAY_TEST);
            sign = MD5(String.format("version=%s&agent_id=%s&agent_bill_id=%s&agent_bill_time=%s&pay_type=%s&pay_amt=%s&notify_url=%s&return_url=%s&user_ip=%s&is_test=%s&key=%s",
                    heepaySubmitMap.get("version"),
                    heepaySubmitMap.get("agentId"),
                    heepaySubmitMap.get("agentBillId"),
                    heepaySubmitMap.get("agentBillTime"),
                    heepaySubmitMap.get("payType"),
                    heepaySubmitMap.get("payAmt"),
                    heepaySubmitMap.get("notifyUrl"),
                    heepaySubmitMap.get("returnUrl"),
                    heepaySubmitMap.get("userIp"),
                    heepaySubmitMap.get("isTest"),
                    tpzPayChannelEntity.getSignKey()
            )).toLowerCase();
        } else {
            sign = MD5(String.format("version=%s&agent_id=%s&agent_bill_id=%s&agent_bill_time=%s&pay_type=%s&pay_amt=%s&notify_url=%s&return_url=%s&user_ip=%s&key=%s",
                    heepaySubmitMap.get("version"),
                    heepaySubmitMap.get("agentId"),
                    heepaySubmitMap.get("agentBillId"),
                    heepaySubmitMap.get("agentBillTime"),
                    heepaySubmitMap.get("payType"),
                    heepaySubmitMap.get("payAmt"),
                    heepaySubmitMap.get("notifyUrl"),
                    heepaySubmitMap.get("returnUrl"),
                    heepaySubmitMap.get("userIp"),
                    tpzPayChannelEntity.getSignKey()
            )).toLowerCase();
        }
        heepaySubmitMap.put("sign", sign); // 签名
        return heepaySubmitMap;
    }

    /**
     * 汇付宝提现请求申请
     *
     * @param payChannelEntity    支付通道信息
     * @param withdrawOrderEntity 提现订单信息
     * @return 汇付宝接受成功返回 true，否则返回 false
     */
    public boolean withdrawByHeepay(TpzPayChannelEntity payChannelEntity, TpzWithdrawOrderEntity withdrawOrderEntity) throws Exception {
        // 充值与体现的银行卡编号吗对应关系
        Map<String, String> heepayBankCodeMap = new HashMap<>();
        heepayBankCodeMap.put("001", "001");  // 工商银行
        heepayBankCodeMap.put("002", "007");  // 招商银行
        heepayBankCodeMap.put("003", "002");  // 建设银行
        heepayBankCodeMap.put("004", "005");  // 中国银行
        heepayBankCodeMap.put("005", "003");  // 农业银行
        heepayBankCodeMap.put("006", "006");  // 交通银行
        heepayBankCodeMap.put("007", "009");  // 上海浦发银行
        heepayBankCodeMap.put("008", "011");  // 广东发展银行
        heepayBankCodeMap.put("009", "012");  // 中信银行
        heepayBankCodeMap.put("010", "008");  // 光大银行
        heepayBankCodeMap.put("011", "013");  // 兴业银行
        heepayBankCodeMap.put("012", "018");  // 平安银行
        heepayBankCodeMap.put("013", "014");  // 民生银行
        heepayBankCodeMap.put("014", "010");  // 华夏银行
        // 构建批付请求
        Map<String, Object> heepayBatchParams = new HashMap<>();
        heepayBatchParams.put("version", 2);
        heepayBatchParams.put("agent_id", payChannelEntity.getMerchantId());
        heepayBatchParams.put("batch_no", withdrawOrderEntity.getOrderNo());
        heepayBatchParams.put("batch_amt", this.longToString(withdrawOrderEntity.getOrderAmount()));
        heepayBatchParams.put("batch_num", 1);
        heepayBatchParams.put("detail_data",
                String.format("%s^%s^%s^%s^%s^%s^%s^%s^%s^%s",
                        withdrawOrderEntity.getOrderNo(),
                        heepayBankCodeMap.get(withdrawOrderEntity.getBankNo()),
                        "0",
                        withdrawOrderEntity.getBankCardNo(),
                        withdrawOrderEntity.getBankCardName().trim(),
                        this.longToString(withdrawOrderEntity.getOrderAmount()),
                        //withdrawOrderEntity.getOrderAbstract(),
                        "取现" + withdrawOrderEntity.getOrderAmount(),
                        StringEscapeUtils.unescapeHtml(withdrawOrderEntity.getProvince().trim()),
                        StringEscapeUtils.unescapeHtml(withdrawOrderEntity.getCity().trim()),
                        StringEscapeUtils.unescapeHtml(withdrawOrderEntity.getOpenBank().trim())));
        heepayBatchParams.put("notify_url", Constants.HEEPAY_BATCH_NOTIFY_URL);
        heepayBatchParams.put("ext_param1", "");
        String sign = String.format(
                "agent_id=%s&batch_amt=%s&batch_no=%s&batch_num=%s&detail_data=%s&ext_param1=%s&key=%s&notify_url=%s&version=%s",
                heepayBatchParams.get("agent_id"),
                heepayBatchParams.get("batch_amt"),
                heepayBatchParams.get("batch_no"),
                heepayBatchParams.get("batch_num"),
                heepayBatchParams.get("detail_data"),
                heepayBatchParams.get("ext_param1"),
                Constants.HEEPAY_BATCH_KEY,
                heepayBatchParams.get("notify_url"),
                heepayBatchParams.get("version")
        );
        heepayBatchParams.put("sign", this.MD5(sign.toLowerCase()).toLowerCase());
        // MD，总算构建完了
        this.logger.info("汇付宝批付请求参数：{}", heepayBatchParams);
        String requestUrl = null;
        if (withdrawOrderEntity.getOrderAmount() >= 5000000) {
            requestUrl = Constants.HEEPAY_BATCH_WORK_REQUEST_URL;
        } else {
            requestUrl = Constants.HEEPAY_BATCH_REQUEST_URL;
        }
        String heepayReturnStr = HttpUtil.post(requestUrl, heepayBatchParams);
        this.logger.info("汇付宝批付返回数据：{}", heepayReturnStr);
        Document document = null;
        try {
            document = DocumentHelper.parseText(heepayReturnStr);
        } catch (DocumentException e) {
            e.printStackTrace();
            this.logger.error("解析XML失败！", e);
            throw new BizServiceException("831410", "解析XML失败！");
        }
        Element rootElement = document.getRootElement();
        String retCode = rootElement.elementTextTrim("ret_code");
        if (!"0000".equals(retCode)) {
            throw new CustomerException("打款失败，原因：" + rootElement.elementTextTrim("ret_msg"), 888888);
        }
        return true;
    }

    /**
     * 汇付宝批付结果查询
     *
     * @param payChannelEntity    支付通道的信息
     * @param withdrawOrderEntity 提现的信息
     * @return 结果
     * @throws Exception
     */
    public List<Map<String, Object>> checkHeepayBatchResult(TpzPayChannelEntity payChannelEntity, TpzWithdrawOrderEntity withdrawOrderEntity) throws Exception {
        Map<String, Object> heepayCheckParams = new HashMap<>();
        heepayCheckParams.put("version", 2);
        heepayCheckParams.put("agent_id", payChannelEntity.getMerchantId());
        heepayCheckParams.put("batch_no", withdrawOrderEntity.getOrderNo());
        String signStr = String.format("agent_id=%s&batch_no=%s&key=%s&version=%s",
                heepayCheckParams.get("agent_id"),
                heepayCheckParams.get("batch_no"),
                Constants.HEEPAY_BATCH_KEY,
                heepayCheckParams.get("version")
        );
        heepayCheckParams.put("sign", this.MD5(signStr.toLowerCase()).toLowerCase());

        this.logger.info("汇付宝批付结果查询请求参数：{}", heepayCheckParams);
        String heepayResultStr = HttpUtil.post(Constants.HEEPAY_BATCH_CHECK_REQUEST_URL, heepayCheckParams);
        this.logger.info("汇付宝批付结果查询返回结果：{}", heepayResultStr);
        Document document = null;
        try {
            document = DocumentHelper.parseText(heepayResultStr);
        } catch (DocumentException e) {
            e.printStackTrace();
            this.logger.error("解析XML失败！", e);
            throw new BizServiceException("831410", "汇付宝解析XML失败！");
        }
        Element rootElement = document.getRootElement();
        if (!"0000".equals(rootElement.elementTextTrim("ret_code"))) {
            throw new CustomerException("汇付宝查询批付接口返回失败", 888888);
        }
        String detailData = rootElement.elementTextTrim("detail_data");
        if (detailData == null) {
            throw new CustomerException("汇付宝查询批付接口未返回 detail_data 字段", 888888);
        }
        return ChannelUtil.analysisHeepayBatchDetailData(detailData);
    }

    /**
     * 查询汇付宝充值订单支付状态
     *
     * @param tpzPayChannelEntity  支付渠道信息
     * @param tpzChargeOrderEntity 充值订单信息
     * @return 汇付宝返回信息
     * @throws Exception
     */
    public Map<String, Object> checkHeepayPayResult(TpzPayChannelEntity tpzPayChannelEntity, TpzChargeOrderEntity tpzChargeOrderEntity) throws Exception {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Map<String, Object> heepayCheckParams = new HashMap<>();
        heepayCheckParams.put("version", 1);
        heepayCheckParams.put("agent_id", tpzPayChannelEntity.getMerchantId());
        heepayCheckParams.put("agent_bill_id", tpzChargeOrderEntity.getOrderNo());
        heepayCheckParams.put("agent_bill_time", dataFormat.format(tpzChargeOrderEntity.getCreateDatetime()));
        heepayCheckParams.put("remark", "");
        heepayCheckParams.put("return_mode", 1);
        String sign = String.format(
                "version=%s&agent_id=%s&agent_bill_id=%s&agent_bill_time=%s&return_mode=%s&key=%s",
                heepayCheckParams.get("version"),
                heepayCheckParams.get("agent_id"),
                heepayCheckParams.get("agent_bill_id"),
                heepayCheckParams.get("agent_bill_time"),
                heepayCheckParams.get("return_mode"),
                tpzPayChannelEntity.getSignKey()
        );
        heepayCheckParams.put("sign", this.MD5(sign).toLowerCase());
        this.logger.info("汇付宝请求参数：{}", heepayCheckParams);
        String heepayPayResultStr = HttpUtil.post(Constants.HEEPAY_PAY_CHECK_REQUEST_URL, heepayCheckParams);
        this.logger.info("汇付宝返回数据：{}", heepayPayResultStr);
        Map<String, Object> heepayReturnMap = new HashMap<>();
        String[] strArr = heepayPayResultStr.split("\\|");
        for (int i = 0; i < strArr.length; i++) {
            String[] strSubArr = strArr[i].split("=");
            if (strSubArr.length == 2) {
                heepayReturnMap.put(strSubArr[0], strSubArr[1]);
            } else {
                heepayReturnMap.put(strSubArr[0], "");
            }
        }
        if (heepayReturnMap.get("agent_bill_id") == null) {
            this.logger.error("汇付宝返回数据错误 {}", heepayPayResultStr);
            return null;
        }
        String mySignStr = String.format(
                "agent_id=%s|agent_bill_id=%s|jnet_bill_no=%s|pay_type=%s|result=%s|pay_amt=%s|pay_message=%s|remark=%s|key=%s",
                heepayReturnMap.get("agent_id").toString(),
                heepayReturnMap.get("agent_bill_id").toString(),
                heepayReturnMap.get("jnet_bill_no").toString(),
                heepayReturnMap.get("pay_type").toString(),
                heepayReturnMap.get("result").toString(),
                heepayReturnMap.get("pay_amt").toString(),
                heepayReturnMap.get("pay_message").toString(),
                heepayReturnMap.get("remark").toString(),
                tpzPayChannelEntity.getSignKey()
        );
        String mySignMD5 = this.MD5(mySignStr).toLowerCase();
        if (!mySignMD5.equals(heepayReturnMap.get("sign").toString())) {
            this.logger.error("汇付宝返回数据验证错误 原字符串 {} MD5 {}", mySignStr, mySignMD5);
            return null;
        }
        return heepayReturnMap;
    }

    /**
     * 解析汇付宝批付回调参数中的detail_data字段
     *
     * @param detailStr 字符串
     * @return 可读性的列表
     */
    public static List<Map<String, Object>> analysisHeepayBatchDetailData(String detailStr) {
        List<Map<String, Object>> detailList = new ArrayList<>();
        String[] detailArr = detailStr.split("\\|");
        for (int i = 0; i < detailArr.length; i++) {
            Map<String, Object> map = new HashMap<>();
            String[] subDetailArr = detailArr[i].split("\\^");
            map.put("orderNo", subDetailArr[0]);
            map.put("bankCardNo", subDetailArr[1]);
            map.put("bankCardName", subDetailArr[2]);
            map.put("amount", subDetailArr[3]);
            map.put("status", subDetailArr[4]);
            detailList.add(map);
        }
        return detailList;
    }

    /**
     * 汇付宝MD5签名加密方法
     *
     * @param s 需要签名的字符串
     * @return 返回MD5签名后的字符串
     * @throws Exception
     */
    public String MD5(String s) throws Exception {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        //与汇付宝编码一致
        byte[] btInput = s.getBytes();
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest mdInst = MessageDigest.getInstance("MD5");
        // 使用指定的字节更新摘要
        mdInst.update(btInput);
        // 获得密文
        byte[] md = mdInst.digest();
        // 把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * 将长整形转换为字符串类型
     *
     * @param num 需要转换的值
     * @return 转换后的值
     */
    public String longToString(Long num) {
        return new BigDecimal(num).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN).toPlainString();
    }

    /**
     * 将字符串转换成长整形
     *
     * @param num 需要转换的值
     * @return 转换后的值
     */
    public Long stringToLong(String num) {
        return new BigDecimal(num).multiply(new BigDecimal("100")).longValue();
    }

    /**
     * 隐藏字符中的某些字符
     *
     * @param source    字符串
     * @param preDigit  前边保留
     * @param lastDigit 后边保留
     * @return 处理后的字符串
     */
    public static String hide(String source, int preDigit, int lastDigit) {
        int sum = preDigit + lastDigit;
        if (StringUtils.isNotBlank(source) && source.length() >= sum) {
            int length = source.length();
            String pre = source.substring(0, preDigit);
            String last = source.substring(length - lastDigit, length);
            StringBuffer sb = new StringBuffer(pre);
            for (int i = 0; i < length - sum; ++i) {
                sb.append("*");
            }
            sb.append(last);
            return sb.toString();
        } else {
            return null;
        }
    }

    public static String sirOrLady(String userRealName, String idCard){
        if("".equals(userRealName) || userRealName == null || "".equals(idCard) || idCard == null) return "";
        String firstName = userRealName.length() == 4?userRealName.substring(0, 2):userRealName.substring(0, 1);
        String res;
        if(idCard.length() == 15){
            res = firstName + ((Integer.parseInt(idCard.substring(14,15)) % 2 == 1)?"先生":"女士");
        }else{
            res = firstName + ((Integer.parseInt(idCard.substring(16,17)) % 2 == 1)?"先生":"女士");
        }
        return res;
    }
}

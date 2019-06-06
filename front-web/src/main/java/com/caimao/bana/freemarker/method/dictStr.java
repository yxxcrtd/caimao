package com.caimao.bana.freemarker.method;

import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.EP2PInvestStatus;
import com.caimao.bana.api.enums.EP2PLoanStatus;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

public class dictStr implements TemplateMethodModelEx {
    public HashMap<String, HashMap<String, String>> getDict(){
        HashMap<String, HashMap<String, String>> dict = new HashMap<>();

        //合约状态
        HashMap<String, String> contractStatus = new HashMap<>();
        contractStatus.put("0", "操盘中");
        contractStatus.put("1", "终止");
        dict.put("contractStatus", contractStatus);
        //产品名称
        HashMap<String, String> productID = new HashMap<>();
        productID.put("1", "免费体验");
        productID.put("2", "实盘大赛");
        productID.put("3", "按月配");
        productID.put("4", "按日配");
        productID.put("5", "最新特惠");
        dict.put("productID", productID);
        //密码强度
        HashMap<String, String> pwdStrength = new HashMap<>();
        pwdStrength.put("1", "弱");
        pwdStrength.put("2", "中");
        pwdStrength.put("3", "强");
        dict.put("pwdStrength", pwdStrength);
        //投资利息类型
        HashMap<String, String> invsInterestType = new HashMap<>();
        invsInterestType.put("1", "息随本清");
        dict.put("invsInterestType", invsInterestType);
        //验证状态
        HashMap<String, String> verifyStatus = new HashMap<>();
        verifyStatus.put("0", "待审");
        verifyStatus.put("1", "审核通过");
        verifyStatus.put("2", "审核不通过");
        verifyStatus.put("3", "审核不通过");
        dict.put("verifyStatus", verifyStatus);
        //委托状态
        HashMap<String, String> entrustStatus = new HashMap<>();
        entrustStatus.put("1", "未报");
        entrustStatus.put("2", "待报");
        entrustStatus.put("3", "正报");
        entrustStatus.put("4", "已报");
        entrustStatus.put("5", "废单");
        entrustStatus.put("6", "部成");
        entrustStatus.put("7", "已成");
        entrustStatus.put("8", "部撤");
        entrustStatus.put("9", "已撤");
        entrustStatus.put("a", "待撤");
        entrustStatus.put("A", "未撤");
        entrustStatus.put("B", "待撤");
        entrustStatus.put("C", "正撤");
        entrustStatus.put("D", "撤认");
        entrustStatus.put("E", "撤废");
        entrustStatus.put("F", "已撤");
        dict.put("entrustStatus", entrustStatus);
        //委托方向
        HashMap<String, String> entrustDirection = new HashMap<>();
        entrustDirection.put("1", "股票买入");
        entrustDirection.put("2", "股票卖出");
        dict.put("entrustDirection", entrustDirection);
        //订单状态
        HashMap<String, String> orderStatus = new HashMap<>();
        orderStatus.put("02", "待处理");
        orderStatus.put("03", "成功");
        orderStatus.put("04", "失败");
        orderStatus.put("05", "已取消");
        orderStatus.put("06", "待确认");
        dict.put("orderStatus", orderStatus);
        //借贷申请状态
        HashMap<String, String> loanApplyStatus = new HashMap<>();
        loanApplyStatus.put("0", "申请中");
        loanApplyStatus.put("1", "申请通过");
        loanApplyStatus.put("2", "申请失败");
        dict.put("loanApplyStatus", loanApplyStatus);
        //资金变动
        HashMap<String, String> accountBizType = new HashMap<>();
        for (EAccountBizType e: EAccountBizType.values()) {
            accountBizType.put(e.getCode(), e.getValue());
        }
        dict.put("accountBizType", accountBizType);
        //充值渠道
        HashMap<String, String> rechargeChannel = new HashMap<>();
        rechargeChannel.put("-1", "支付宝");
        rechargeChannel.put("-2", "银行转账");
        rechargeChannel.put("3", "网银充值");
        dict.put("rechargeChannel", rechargeChannel);

        // 投资状态
        HashMap<String, String> investStatus = new HashMap<>();
        for (EP2PInvestStatus ep2PInvestStatus : EP2PInvestStatus.values()) {
            investStatus.put(ep2PInvestStatus.getCode().toString(), ep2PInvestStatus.getValue());
        }
        dict.put("investStatus", investStatus);
        // 标的状态
        HashMap<String, String> targetStatus = new HashMap<>();
        for (EP2PLoanStatus e : EP2PLoanStatus.values()) {
            targetStatus.put(e.getCode().toString(), e.getValue());
        }
        dict.put("targetStatus", targetStatus);


        return dict;
    }

    public HashMap<String, HashMap<String, String>> getDictColor(){
        HashMap<String, HashMap<String, String>> dict = new HashMap<>();

        //验证状态
        HashMap<String, String> verifyStatus = new HashMap<>();
        verifyStatus.put("0", "");
        verifyStatus.put("1", "font_cyan");
        verifyStatus.put("2", "font_red");
        verifyStatus.put("3", "font_red");
        dict.put("verifyStatus", verifyStatus);

        //订单状态
        HashMap<String, String> orderStatus = new HashMap<>();
        orderStatus.put("02", "");
        orderStatus.put("03", "font_cyan");
        orderStatus.put("04", "font_red");
        orderStatus.put("05", "");
        orderStatus.put("06", "");
        dict.put("orderStatus", orderStatus);

        // 投资状态
        HashMap<String, String> investStatus = new HashMap<>();
        investStatus.put(EP2PInvestStatus.INIT.getCode().toString(), "");
        investStatus.put(EP2PInvestStatus.FULL.getCode().toString(), "font_red");
        investStatus.put(EP2PInvestStatus.FAIL.getCode().toString(), "");
        investStatus.put(EP2PInvestStatus.INCOME.getCode().toString(), "font_blue");
        investStatus.put(EP2PInvestStatus.END.getCode().toString(), "font_cyan");
        investStatus.put(EP2PInvestStatus.CANCEL.getCode().toString(), "");
        dict.put("investStatus", investStatus);

        // 标的状态
        HashMap<String, String> targetStatus = new HashMap<>();
        targetStatus.put(EP2PLoanStatus.REPAYMENT.getCode().toString(), "font_cyan");
        targetStatus.put(EP2PLoanStatus.FAIL.getCode().toString(), "font_red");
        dict.put("targetStatus", targetStatus);

        //借贷申请状态
        HashMap<String, String> loanApplyStatus = new HashMap<>();
        loanApplyStatus.put("0", "");
        loanApplyStatus.put("1", "font_cyan");
        loanApplyStatus.put("2", "font_red");
        dict.put("loanApplyStatus", loanApplyStatus);


        return dict;
    }


    @Override
    public Object exec(List args) throws TemplateModelException {
        if(args.size() != 3) return null;
        //定义三个参数 key, code, returnType
        String key = args.get(0).toString();
        String code = args.get(1).toString();
        Integer returnType = Integer.parseInt(args.get(2).toString());

        HashMap<String, HashMap<String, String>> dict = this.getDict();;
        if(returnType == 1) dict = this.getDictColor();
        HashMap<String, String> searchDict = dict.get(key);
        if(searchDict == null) return "";
        String statusStr = searchDict.get(code);
        if(statusStr == null) return "";
        return statusStr;
    }
}
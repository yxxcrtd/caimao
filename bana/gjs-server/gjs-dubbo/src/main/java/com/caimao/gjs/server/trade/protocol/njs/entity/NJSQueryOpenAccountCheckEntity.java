package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询开户校验
 */
public class NJSQueryOpenAccountCheckEntity implements Serializable {
    private String TYPE; //校验类别
    private String CONTENT; //内容 A校验证件信息 B校验银行卡信息 C校验用户名密码
    private String RESULT; //校验结果 Y校验通过 N校验不通过

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }
}
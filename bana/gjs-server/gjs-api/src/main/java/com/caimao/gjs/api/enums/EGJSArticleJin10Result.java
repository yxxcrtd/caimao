package com.caimao.gjs.api.enums;

/**
 * 金十分析结果
 *
 * Created by yangxinxin@huobi.com on 2015/11/5
 */
public enum EGJSArticleJin10Result {
    LIKONG(1, "利空"),
    LIKONG2(1, "利空2"),
    LIDUO(2, "利多"),
    LIDUO2(2, "利多2"),
    SMALL1(3, "无影响"),
    SMALL2(3, "无影响2"),
    SMALL3(3, "影响较小"),
    CRUDE4(4, "原油利多2"),
    CRUDE5(5, "原油利空");

    private Integer code;
    private String name;

    EGJSArticleJin10Result(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EGJSArticleJin10Result findByCode(Integer code) {
        for (EGJSArticleJin10Result sCode : EGJSArticleJin10Result.values()) {
            if (sCode.getCode().equals(code)) {
                return sCode;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

package com.caimao.gjs.api.enums;

/**
 * 金十资讯分类
 *
 * Created by yangxinxin@huobi.com on 2015/11/5
 */
public enum EGJSArticleJin10Category {
    NOMARLNEWS(1, "一般新闻"),
    COMMONNEWS(1, "普通新闻"),
    IMPORTANTNEWS(2, "重要新闻"),
    NOMARLDATA(3, "一般数据"),
    IMPORTANTDATA(4, "重要数据");

    private Integer code;
    private String name;

    EGJSArticleJin10Category(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EGJSArticleJin10Category findByCode(Integer code) {
        for (EGJSArticleJin10Category sCode : EGJSArticleJin10Category.values()) {
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

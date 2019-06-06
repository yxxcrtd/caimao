package com.caimao.gjs.api.enums;

/**
 * <p>
 *     贵金属文章类型
 * </p>
 * Created by Administrator on 2015/10/10.
 */
public enum EGJSArticleCategory {
    NOTICE(1, "公告"),
    ARTICLE(2, "文章");

    private Integer code;
    private String name;

    EGJSArticleCategory(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EGJSArticleCategory findByCode(Integer code) {
        for (EGJSArticleCategory sCode : EGJSArticleCategory.values()) {
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

package com.caimao.bana.api.enums.ybk;

/**
 * 文章分类
 */
public enum EArticleCategory {
    DXSG("1", "打新申购"),
    SCJG("2", "市场价格"),
    TPGG("3", "停牌公告"),
    WZGG("4", "网站公告"),
    WZBK("5", "网站百科");

    private final String code;
    private final String value;

    EArticleCategory(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}

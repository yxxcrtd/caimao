package com.caimao.bana.api.enums;

/**
 * Created by WangXu on 2015/5/26.
 */
public enum EIdcardKind {
    IDCARD("1", "身份证"),
    PASSPORT("2", "护照");

    private String id;
    private String content;

    private EIdcardKind(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

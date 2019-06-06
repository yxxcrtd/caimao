package com.caimao.bana.api.enums;

public enum EPushModel {
    HOME("1", "主站"),
    YBK("2", "邮币卡"),
    GJS("3", "贵金属");

    private String code;
    private String value;

    private EPushModel(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
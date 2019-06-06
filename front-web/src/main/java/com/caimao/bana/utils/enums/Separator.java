package com.caimao.bana.utils.enums;

/**
 * Created by Administrator on 2015/9/29.
 */
public enum Separator {
    SEMICOLON(";"),
    COMMA(","),
    COLON(":"),
    PERIOD(".");

    private String separator;

    private Separator(String separator) {
        this.separator = separator;
    }

    public String value() {
        return this.separator;
    }
}

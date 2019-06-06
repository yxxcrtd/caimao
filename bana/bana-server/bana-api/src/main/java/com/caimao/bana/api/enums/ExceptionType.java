package com.caimao.bana.api.enums;

/**
 * web普通异常  
 * mobile移动端异常
 * @author yanjg
 * 2015年4月11日
 */
public enum ExceptionType {
    WEB(1), MOBILE(3);

    private int value;

    private ExceptionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

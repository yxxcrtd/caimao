package com.fmall.bana.utils.enums;

/**
 * Created by Administrator on 2015/9/29.
 */
public enum ExceptionTypeConstants {
    SYSTEM_EXCEPTION(0),
    DATA_VALIDATION_EXCEPTION(1),
    BIZ_EXCEPTION(2);

    private int id;

    private ExceptionTypeConstants(int id) {
        this.id = id;
    }

    public int value() {
        return this.id;
    }
}

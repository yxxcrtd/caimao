package com.fmall.bana.utils.exception;

/**
 * Created by Administrator on 2015/9/29.
 */
public class Error {
    protected String info;

    public Error() {
    }

    public Error(String info) {
        this.info = info;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

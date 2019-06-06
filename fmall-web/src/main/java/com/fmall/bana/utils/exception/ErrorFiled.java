package com.fmall.bana.utils.exception;


/**
 * Created by Administrator on 2015/9/29.
 */
public class ErrorFiled extends Error {
    private String name;

    public ErrorFiled(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

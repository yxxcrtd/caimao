package com.fmall.bana.utils;

/**
 * HTTP输出的JSON信息
 * Created by Administrator on 2015/9/15.
 */
public class JsonObject {
    private Boolean success;
    private String code;
    private String msg;
    private Object data;

    public JsonObject(Object data) {
        this.success = Boolean.TRUE;
        this.data = data;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

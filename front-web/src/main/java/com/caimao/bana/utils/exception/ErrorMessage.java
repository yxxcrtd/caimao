package com.caimao.bana.utils.exception;


/**
 * Created by Administrator on 2015/9/29.
 */
public class ErrorMessage extends Error {
    private String no;

    public ErrorMessage() {
    }

    public ErrorMessage(String no, String info) {
        this.no = no;
        this.info = info;
    }

    public String getNo() {
        return this.no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String toString() {
        return "ErrorMessage [no=" + this.no + ", info=" + this.info + "]";
    }
}

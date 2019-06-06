package com.caimao.zeus.entity;

import java.io.Serializable;

/**
 * ajax响应类
 *
 * @author zhengrj
 */
public class AjaxResponse implements Serializable {

    private static final long serialVersionUID = -603454364015061374L;

    public static final AjaxResponse OK = new AjaxResponse(1, "ok");
    public static final AjaxResponse FAILED = new AjaxResponse(-1, "failed");
    public static final AjaxResponse SYSTEM_BUSY = new AjaxResponse(-100000, "系统繁忙，请您稍后再试");

    /**
     * 状态码
     */
    private int status;

    /**
     * 描述原因
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 构造一个有状态码和描述的Ajax响应
     */
    public AjaxResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * 构造一个有状态码、描述、数据的Ajax响应
     */
    public AjaxResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * 产生一个成功的响应，并携带数据
     *
     * @return
     */
    public static AjaxResponse OK(Object data) {
        return new AjaxResponse(1, "ok", data);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

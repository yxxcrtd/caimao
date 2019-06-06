package com.caimao.hq.api.entity;

import java.util.List;

/**
 * Created by yzc on 2015/11/25.
 * 网易行情数据返回对象
 */
public class WyHqMonthRes {

    private List<List<String>> ret;
    private List<List<String>> data;
    private String retCode;
    private String retDesc;
    private String version;


    public List getRet() {
        return ret;
    }

    public void setRet(List ret) {
        this.ret = ret;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetDesc() {
        return retDesc;
    }

    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}

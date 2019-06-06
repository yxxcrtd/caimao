package com.caimao.hq.api.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yzc on 2015/12/4.
 */
public class LIFFECandleFrom163Res  implements Serializable {

    private List<String> ret;
    private String num;
    private String retCode;
    private String retDesc;
    private String version;
    private  String prodCode;

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public List<String> getRet() {
        return ret;
    }

    public void setRet(List<String> ret) {
        this.ret = ret;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
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

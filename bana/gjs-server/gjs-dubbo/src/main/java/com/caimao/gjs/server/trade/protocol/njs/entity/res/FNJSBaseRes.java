package com.caimao.gjs.server.trade.protocol.njs.entity.res;

import java.io.Serializable;
import java.util.List;

public class FNJSBaseRes<T> implements Serializable {
    private String ADAPTER; //协议号
    private String STATE; //状态 0为成功， 错误对应相应的错误代码
    private String MSG; //错误信息
    private List<T> DATAS; //数据体

    public String getADAPTER() {
        return ADAPTER;
    }

    public void setADAPTER(String ADAPTER) {
        this.ADAPTER = ADAPTER;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    public List<T> getDATAS() {
        return DATAS;
    }

    public void setDATAS(List<T> DATAS) {
        this.DATAS = DATAS;
    }
}

package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSQuerySysParamsReq implements Serializable{
    private String para_id; //参数编号 签约银行代码

    public String getPara_id() {
        return para_id;
    }

    public void setPara_id(String para_id) {
        this.para_id = para_id;
    }
}

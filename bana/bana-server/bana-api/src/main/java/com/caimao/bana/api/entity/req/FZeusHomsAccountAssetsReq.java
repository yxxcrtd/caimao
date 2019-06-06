package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountAssetsEntity;

import java.io.Serializable;

/**
 * 后台查询homs资产是否盈亏的请求对象
 * Created by xavier on 15/7/8.
 */
public class FZeusHomsAccountAssetsReq extends QueryBase<ZeusHomsAccountAssetsEntity> implements Serializable {
    private String match;
    private String updateDate;
    private Integer type;

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

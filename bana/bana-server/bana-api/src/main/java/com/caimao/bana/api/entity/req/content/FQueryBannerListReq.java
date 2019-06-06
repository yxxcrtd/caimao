package com.caimao.bana.api.entity.req.content;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.content.BanaBannerEntity;

import java.io.Serializable;

/**
 * Banner 请求列表类
 * Created by Administrator on 2015/10/14.
 */
public class FQueryBannerListReq extends QueryBase<BanaBannerEntity> implements Serializable {

    private String appType;
    private Integer isShow;

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }
}

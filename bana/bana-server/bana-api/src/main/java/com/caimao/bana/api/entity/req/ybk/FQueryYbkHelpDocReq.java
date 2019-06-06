package com.caimao.bana.api.entity.req.ybk;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.ybk.YbkHelpDocEntity;

import java.io.Serializable;

/**
* YbkHelpDocEntity
*
* Created by wangxu@huobi.com on 2015-11-16 17:26:31
*/
public class FQueryYbkHelpDocReq extends QueryBase<YbkHelpDocEntity> implements Serializable {
    private Integer categoryId;
    private String isShow;
    private String beginDate;
    private String endDate;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
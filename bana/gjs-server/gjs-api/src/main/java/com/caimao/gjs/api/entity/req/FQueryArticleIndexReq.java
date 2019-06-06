package com.caimao.gjs.api.entity.req;

import com.caimao.bana.common.api.entity.QueryBase;
import com.caimao.gjs.api.entity.GjsArticleIndexEntity;

import java.io.Serializable;

/**
 * 首页文章请求对象
 */
public class FQueryArticleIndexReq extends QueryBase<GjsArticleIndexEntity> implements Serializable {
    private Integer category;
    private String dateStart;
    private String dateEnd;
    private Integer isShow;
    private String pubStart;
    private String pubEnd;

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getPubStart() {
        return pubStart;
    }

    public void setPubStart(String pubStart) {
        this.pubStart = pubStart;
    }

    public String getPubEnd() {
        return pubEnd;
    }

    public void setPubEnd(String pubEnd) {
        this.pubEnd = pubEnd;
    }
}

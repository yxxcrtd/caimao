package com.caimao.gjs.api.entity.req;

import com.caimao.bana.common.api.entity.QueryBase;
import com.caimao.gjs.api.entity.res.FQueryGjsArticleRes;

import java.io.Serializable;

/**
 * 贵金属文章、公告的查询请求对象
 * Created by Administrator on 2015/10/12.
 */
public class FQueryArticleReq extends QueryBase<FQueryGjsArticleRes> implements Serializable {
    private Integer categoryId;
    private Integer isShow;
    private Integer isHot;
    private String dateStart;
    private String dateEnd;
    private String url;

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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

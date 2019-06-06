package com.caimao.bana.api.entity.content;

import java.io.Serializable;
import java.util.Date;

/**
 * 首页banner数据实例
 * Created by Administrator on 2015/10/14.
 */
public class BanaBannerEntity implements Serializable {

    private Integer id;
    private String appType;
    private String name;
    private String pcPic;
    private String appPic;
    private String pcJumpUrl;
    private String appJumpUrl;
    private Integer isShow;
    private Integer sort;
    private Date createTime;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPcPic() {
        return pcPic;
    }

    public void setPcPic(String pcPic) {
        this.pcPic = pcPic;
    }

    public String getAppPic() {
        return appPic;
    }

    public void setAppPic(String appPic) {
        this.appPic = appPic;
    }

    public String getPcJumpUrl() {
        return pcJumpUrl;
    }

    public void setPcJumpUrl(String pcJumpUrl) {
        this.pcJumpUrl = pcJumpUrl;
    }

    public String getAppJumpUrl() {
        return appJumpUrl;
    }

    public void setAppJumpUrl(String appJumpUrl) {
        this.appJumpUrl = appJumpUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

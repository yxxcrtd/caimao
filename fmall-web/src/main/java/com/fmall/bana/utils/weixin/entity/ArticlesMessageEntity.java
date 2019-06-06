package com.fmall.bana.utils.weixin.entity;

/**
 * 微信被动回复图文信息消息
 * Created by Administrator on 2016/1/4.
 */
public class ArticlesMessageEntity {
    private String Title;
    private String Description;
    private String PicUrl;
    private String Url;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
}

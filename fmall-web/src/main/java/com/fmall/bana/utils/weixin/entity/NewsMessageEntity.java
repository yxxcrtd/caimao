package com.fmall.bana.utils.weixin.entity;

import java.util.List;

/**
 * 微信被动回复图文消息
 * Created by Administrator on 2016/1/4.
 */
public class NewsMessageEntity {
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String MsgType = "news";
    private List<ArticlesMessageEntity> Articles;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public List<ArticlesMessageEntity> getArticles() {
        return Articles;
    }

    public void setArticles(List<ArticlesMessageEntity> articles) {
        Articles = articles;
    }
}

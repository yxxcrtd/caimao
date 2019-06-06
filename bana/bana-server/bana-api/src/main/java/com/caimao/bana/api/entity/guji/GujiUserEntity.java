package com.caimao.bana.api.entity.guji;

import java.io.Serializable;
import java.util.Date;

/**
* GujiUserEntity 实例对象
*
* Created by wangxu@huobi.com on 2016-01-07 17:42:48 星期四
*/
public class GujiUserEntity implements Serializable {

    /** 微信用户的自增ID */
    private Long wxId;

    /** 微信用户唯一编号 */
    private String openId;

    /** 微信公众平台的唯一编号（不一定有） */
    private String unionId;

    /** 用户昵称 */
    private String nickname;

    /** 用户头像 */
    private String headimgurl;

    /** 是否关注服务号 */
    private Integer subscribe;

    /** 是否公开推荐(0 不 1 是) */
    private Integer publicRecom;

    /** 认证机构 */
    private String certificationAuth;

    /** 上传证件照片 */
    private String cardPic;

    /** 认证状态 */
    private Integer authStatus;

    /** 信息更新时间 */
    private Date updateTime;

    /** 用户注册时间 */
    private Date createTime;


    public Long getWxId() {
        return wxId;
    }

    public void setWxId(Long wxId) {
        this.wxId = wxId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    public Integer getPublicRecom() {
        return publicRecom;
    }

    public void setPublicRecom(Integer publicRecom) {
        this.publicRecom = publicRecom;
    }

    public String getCertificationAuth() {
        return certificationAuth;
    }

    public void setCertificationAuth(String certificationAuth) {
        this.certificationAuth = certificationAuth;
    }

    public String getCardPic() {
        return cardPic;
    }

    public void setCardPic(String cardPic) {
        this.cardPic = cardPic;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
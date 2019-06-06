package com.caimao.bana.api.entity.guji;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
* GujiShareRecordEntity 实例对象
*
* Created by wangxu@huobi.com on 2016-01-07 17:40:29 星期四
*/
public class GujiShareRecordEntity implements Serializable {

    /** 推荐ID */
    private Long shareId;

    /** 微信用户ID */
    @NotNull
    private Long wxId;

    /** 微信用户唯一标示 */
    @NotNull
    private String openId;

    /** 股票类型 */
    @NotNull
    private String stockType;

    /** 股票代码 */
    @NotNull(message = "股票代码不能为空！")
    private String stockCode;

    /** 股票名称 */
    @NotNull
    private String stockName;

    /** 股票时价 */
    @NotNull
    private String stockPrice;

    /** 目标价位 */
    private String targetPrice;

    /** 变更后仓位 */
    private Integer positions;

    /** 变更前仓位 */
    private Integer prePositions;

    /** 推荐类型 */
    private Integer operType;

    /** 原因 */
    @NotNull(message = "点评不能为空！")
    private String reason;

    /** 点赞数 */
    private Integer favour;

    /** 是否公开 */
    private Integer isPublic;

    /** 推荐时间 */
    private Date createTime;

    // 以下是其他功能需要的一些字段
    private Integer isFollow;
    private Integer isLike;
    private String publishTimeStr;
    private String nickname;
    private String headimgurl;
    private Integer subscribe;
    private String certificationAuth;
    private String authStatus;


    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

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

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(String stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(String targetPrice) {
        this.targetPrice = targetPrice;
    }

    public Integer getPositions() {
        return positions;
    }

    public void setPositions(Integer positions) {
        this.positions = positions;
    }

    public Integer getPrePositions() {
        return prePositions;
    }

    public void setPrePositions(Integer prePositions) {
        this.prePositions = prePositions;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getFavour() {
        return favour;
    }

    public void setFavour(Integer favour) {
        this.favour = favour;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Integer isFollow) {
        this.isFollow = isFollow;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
    }

    public String getPublishTimeStr() {
        return publishTimeStr;
    }

    public void setPublishTimeStr(String publishTimeStr) {
        this.publishTimeStr = publishTimeStr;
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

    public String getCertificationAuth() {
        return certificationAuth;
    }

    public void setCertificationAuth(String certificationAuth) {
        this.certificationAuth = certificationAuth;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }
}
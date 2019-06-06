package com.caimao.bana.api.entity.guji;

import java.io.Serializable;
import java.util.Date;

/**
* GujiFocusRecordEntity 实例对象
*
* Created by wangxu@huobi.com on 2016-01-07 17:37:28 星期四
*/
public class GujiFocusRecordEntity implements Serializable {

    /** 微信用户ID */
    private Long wxId;

    /** 微信用户唯一标示 */
    private String openId;

    /** 关注的 */
    private Long focusWxId;

    /** 关注的 */
    private String focusOpenId;

    /** 关注时间 */
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

    public Long getFocusWxId() {
        return focusWxId;
    }

    public void setFocusWxId(Long focusWxId) {
        this.focusWxId = focusWxId;
    }

    public String getFocusOpenId() {
        return focusOpenId;
    }

    public void setFocusOpenId(String focusOpenId) {
        this.focusOpenId = focusOpenId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
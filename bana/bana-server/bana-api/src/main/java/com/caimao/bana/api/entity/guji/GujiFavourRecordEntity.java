package com.caimao.bana.api.entity.guji;

import java.io.Serializable;
import java.util.Date;

/**
* GujiFavourRecordEntity 实例对象
*
* Created by wangxu@huobi.com on 2016-01-07 17:33:02 星期四
*/
public class GujiFavourRecordEntity implements Serializable {

    /** 分享的推荐股票记录 */
    private Long shareId;

    /** 赞的微信用户 */
    private String openId;

    /** 赞的时间 */
    private Date createTime;


    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
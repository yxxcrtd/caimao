package com.caimao.bana.api.entity.req.guji;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.guji.GujiShareRecordEntity;

/**
 * 查询用户关注用户的分享的推荐股票信息列表
 * Created by Administrator on 2016/1/7.
 */
public class FQueryGujiFollowShareListReq extends QueryBase<GujiShareRecordEntity> {

    private Long wxId;
    private String openId;

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
}

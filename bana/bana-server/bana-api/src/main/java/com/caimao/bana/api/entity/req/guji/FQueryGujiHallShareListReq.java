package com.caimao.bana.api.entity.req.guji;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.guji.GujiShareRecordEntity;

/**
 * 获取股计大厅分享的股票推荐信息列表
 * Created by Administrator on 2016/1/7.
 */
public class FQueryGujiHallShareListReq extends QueryBase<GujiShareRecordEntity> {
    /**
     * 这个openId，指的是查询用户的openId，不是只看这个openId的用户推荐
     */
    private String checkOpenId;

    public String getCheckOpenId() {
        return checkOpenId;
    }

    public void setCheckOpenId(String checkOpenId) {
        this.checkOpenId = checkOpenId;
    }
}

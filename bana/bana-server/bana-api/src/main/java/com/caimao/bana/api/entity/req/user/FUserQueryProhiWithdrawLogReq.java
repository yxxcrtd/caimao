package com.caimao.bana.api.entity.req.user;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.user.FUserQueryProhiWithdrawLogRes;

import java.io.Serializable;

/**
 * 查询用户禁止提现的历史
 * Created by Administrator on 2015/8/14.
 */
public class FUserQueryProhiWithdrawLogReq extends QueryBase<FUserQueryProhiWithdrawLogRes> implements Serializable {

    private Long userId;
    private String status;
    private String type;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

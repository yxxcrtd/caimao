package com.caimao.bana.api.entity.req.guji;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.guji.GujiUserEntity;

/**
 * 后台查询用户列表的请求对象
 * Created by Administrator on 2016/1/14.
 */
public class FQueryAdminUserListReq extends QueryBase<GujiUserEntity> {

    // 根据昵称模糊搜索
    private String nickName;
    private Integer authStatus;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }
}

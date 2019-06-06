package com.caimao.bana.api.entity.session;

/**
 * Created by Administrator on 2015/9/30.
 */
public class SessionUser extends UserDetail {
    private Long user_id;

    public SessionUser() {
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}

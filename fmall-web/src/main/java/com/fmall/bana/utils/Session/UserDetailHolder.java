package com.fmall.bana.utils.Session;


import com.caimao.bana.api.entity.session.UserDetail;

/**
 * Created by Administrator on 2015/9/29.
 */
public class UserDetailHolder {
    public static final ThreadLocal<UserDetail> userDetail = new ThreadLocal();

    public UserDetailHolder() {
    }

    public static UserDetail getUserDetail() {
        return (UserDetail) userDetail.get();
    }

    public static void setUserDeail(UserDetail user) {
        userDetail.set(user);
    }

    public static void remove() {
        userDetail.remove();
    }
}

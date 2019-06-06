package com.caimao.bana.utils.Session;


import com.caimao.bana.api.entity.session.SessionUser;

/**
 * Created by Administrator on 2015/9/29.
 */
public class SessionHelper {
    public SessionHelper() {
    }

    public static SessionUser setIdentity(Long userId) {
        SessionUser sessionUser = null;
        if(userId.longValue() > 0L) {
            sessionUser = new SessionUser();
            sessionUser.setUser_id(userId);
        }

        return sessionUser;
    }
}

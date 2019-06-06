package com.caimao.bana.utils.Session;

import com.caimao.bana.api.entity.session.UserDetail;

/**
 * Created by Administrator on 2015/9/29.
 */
public interface SessionProvider {
    String SESSION_KEY_USER = "user";

    void setUserDetail(UserDetail var1);

    void removeUserDetail();

    UserDetail getUserDetail();

    void setAttribute(String var1, Object var2);

    Object getAttribute(String var1);

    void removeAttribute(String var1);
}

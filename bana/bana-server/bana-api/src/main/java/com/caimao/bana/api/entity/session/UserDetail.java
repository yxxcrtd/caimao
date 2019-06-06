package com.caimao.bana.api.entity.session;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/30.
 */
public abstract class UserDetail implements Serializable {
    public UserDetail() {
    }

    public Boolean isValid() {
        return Boolean.TRUE;
    }
}

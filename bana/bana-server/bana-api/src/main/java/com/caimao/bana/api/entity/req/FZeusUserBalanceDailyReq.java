package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.zeus.ZeusUserBalanceDailyEntity;

import java.io.Serializable;

/**
 * 后台查询用户资产日报列表
 */
public class FZeusUserBalanceDailyReq extends QueryBase<ZeusUserBalanceDailyEntity> implements Serializable {
    private String dateStart;
    private String dateEnd;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}

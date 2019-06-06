package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FAccountQueryAccountFrozenJourRes;
import com.caimao.bana.api.entity.res.FAccountQueryAccountJourRes;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询账户资金冻结流水请求
 * Created by WangXu on 2015/5/27.
 */
public class FAccountQueryAccountFrozenJourReq extends QueryBase<FAccountQueryAccountFrozenJourRes> implements Serializable {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    private Long pzAccountId;
    private String bizType;

    @NotEmpty(message = "请设置开始时间")
    private String startDate;

    @NotEmpty(message = "请设置结束时间")
    private String endDate;

    private String refSerialNo;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRefSerialNo() {
        return refSerialNo;
    }

    public void setRefSerialNo(String refSerialNo) {
        this.refSerialNo = refSerialNo;
    }
}

package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by WangXu on 2015/4/14.
 */
public class DepositAlipayTransferEntity implements Serializable {
    private static final long serialVersionUID = 3346280108752805355L;
    private int id;
    private int userId;
    private String account;
    private BigDecimal amount;
    private byte status;
    private String remark;
    private int created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepositAlipayTransferEntity that = (DepositAlipayTransferEntity) o;

        if (created != that.created) return false;
        if (id != that.id) return false;
        if (status != that.status) return false;
        if (userId != that.userId) return false;
        if (account != null ? !account.equals(that.account) : that.account != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (int) status;
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + created;
        return result;
    }
}

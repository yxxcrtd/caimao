package com.caimao.bana.api.entity;

import java.io.Serializable;

public class ProdTypeEntity implements Serializable {
    private static final long serialVersionUID = 9208985240133747713L;
    private String prodTypeId;
    private String prodTypePid;
    private String prodTypeName;

    public String getProdTypeId() {
        return this.prodTypeId;
    }

    public void setProdTypeId(String prodTypeId) {
        this.prodTypeId = prodTypeId;
    }

    public String getProdTypePid() {
        return this.prodTypePid;
    }

    public void setProdTypePid(String prodTypePid) {
        this.prodTypePid = prodTypePid;
    }

    public String getProdTypeName() {
        return this.prodTypeName;
    }

    public void setProdTypeName(String prodTypeName) {
        this.prodTypeName = prodTypeName;
    }
}
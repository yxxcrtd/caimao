package com.caimao.bana.api.entity;

import java.io.Serializable;

public class CompanyEntity implements Serializable {
    private static final long serialVersionUID = -8674025871904373629L;
    private String companyId;
    private String companyName;
    private String compnayLicense;
    private String companyLogo;
    private String companyResource;
    private String payAccountId;
    private String payUserId;
    private String companyDescription;

    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompnayLicense() {
        return this.compnayLicense;
    }

    public void setCompnayLicense(String compnayLicense) {
        this.compnayLicense = compnayLicense;
    }

    public String getCompanyLogo() {
        return this.companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyResource() {
        return this.companyResource;
    }

    public void setCompanyResource(String companyResource) {
        this.companyResource = companyResource;
    }

    public String getPayAccountId() {
        return this.payAccountId;
    }

    public void setPayAccountId(String payAccountId) {
        this.payAccountId = payAccountId;
    }

    public String getPayUserId() {
        return this.payUserId;
    }

    public void setPayUserId(String payUserId) {
        this.payUserId = payUserId;
    }

    public String getCompanyDescription() {
        return this.companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }
}
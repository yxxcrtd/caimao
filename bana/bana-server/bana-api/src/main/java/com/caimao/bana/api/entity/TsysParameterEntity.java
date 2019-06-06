package com.caimao.bana.api.entity;

import java.io.Serializable;

public class TsysParameterEntity implements Serializable {

    private static final long serialVersionUID = 7463341284958248323L;

    private String paramCode;

    private String relOrg;

    private String kindCode;

    private String paramName;

    private String paramValue;

    private String extFlag;

    private String lifecycle;

    private String platform;

    private String paramDesc;

    private String paramRegex;

    public String getKindCode() {
        return kindCode;
    }

    public void setKindCode(String kindCode) {
        this.kindCode = kindCode;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getExtFlag() {
        return extFlag;
    }

    public void setExtFlag(String extFlag) {
        this.extFlag = extFlag;
    }

    public String getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(String lifecycle) {
        this.lifecycle = lifecycle;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public String getParamRegex() {
        return paramRegex;
    }

    public void setParamRegex(String paramRegex) {
        this.paramRegex = paramRegex;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getRelOrg() {
        return relOrg;
    }

    public void setRelOrg(String relOrg) {
        this.relOrg = relOrg;
    }
}
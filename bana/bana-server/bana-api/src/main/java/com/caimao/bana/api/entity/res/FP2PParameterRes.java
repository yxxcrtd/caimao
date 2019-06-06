/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.entity.res;

import java.io.Serializable;

/**
 * @author zxd $Id$
 * 
 */
public class FP2PParameterRes implements Serializable{

    private static final long serialVersionUID = -4374689957739447161L;
    
    private String paramCode;

    private String paramName;

    private String paramValue;

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
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


}

package com.caimao.gjs.api.enums;

/**
 * <p>
 *     南交所银行编号
 * </p>
 * Created by Administrator on 2015/10/10.
 */
public enum ENJSBankNo {
    /**003 招商银行 0*/
    CMB("003","招商银行","0"),
    /**005 建设银行 0*/
    //CCB("005","建设银行","0"),
    /**012 中信银行 0*/
    CNCB("012","中信银行","0"),
    /**013 华夏银行 0*/
    HXB("013","华夏银行","0"),
    /**014 平安银行 0*/
    PAB("014","平安银行","0"),
    /**019 光大银行 0*/
    BCM("019","光大银行","0"),
    /**408 中国银行 1*/
    BOC("408","中国银行","1"),
    /**410 兴业银行 0*/
    CIB("410","兴业银行","0"),
    /**212 中信异度*/
    CNCBYD("212","中信异度","0");

    private String code;
    private String name;
    private String isCardPwd;

    ENJSBankNo(String code, String name, String isCardPwd) {
        this.code = code;
        this.name = name;
        this.isCardPwd = isCardPwd;
    }

    public static ENJSBankNo findByCode(String code) {
        for (ENJSBankNo sCode : ENJSBankNo.values()) {
            if (sCode.getCode().equals(code)) {
                return sCode;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsCardPwd() {
        return isCardPwd;
    }

    public void setIsCardPwd(String isCardPwd) {
        this.isCardPwd = isCardPwd;
    }
}

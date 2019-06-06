package com.caimao.gjs.api.enums;

/**
 * <p>
 *     上金所银行编号
 * </p>
 * Created by Administrator on 2015/10/10.
 */
public enum ESJSBankNo {
    /**005 招商银行 0*/
    CCB("005", "建设银行", "0"),
    /**002 招商银行 0*/
    ICBC("002", "工商银行", "0"),
    /**015 招商银行 0*/
    CMB("015", "招商银行", "0"),
    /**003 招商银行 0*/
    ABC("003", "农业银行", "0"),
    /**024 招商银行 0*/
    PAB("024", "平安银行", "0"),
    /**004 招商银行 0*/
    BOC("004", "中国银行", "0"),
    /**009 招商银行 0*/
    BCM("009", "交通银行", "0");

    private String code;
    private String name;
    private String isCardPwd;

    ESJSBankNo(String code, String name, String isCardPwd) {
        this.code = code;
        this.name = name;
        this.isCardPwd = isCardPwd;
    }

    public static ESJSBankNo findByCode(String code) {
        for (ESJSBankNo sCode : ESJSBankNo.values()) {
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

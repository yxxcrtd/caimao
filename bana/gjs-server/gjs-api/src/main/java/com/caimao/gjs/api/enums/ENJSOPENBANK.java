package com.caimao.gjs.api.enums;

/**
 * 中信异度开户银行
 */
public enum ENJSOPENBANK {
    BOC("BOC", "中国银行", "中国银行"),
    ICBC("ICBC", "中国工商银行", "工商银行"),
    ABC("ABC", "中国农业银行", "农业银行"),
    CCB("CCB", "中国建设银行", "建设银行"),
    PSBC("PSBC", "中国邮政储蓄银行", "邮政储蓄"),
    BOCM("BOCM", "交通银行", "交通银行"),
    CMB("CMB", "招商银行", "招商银行"),
    CNCB("CNCB", "中信银行", "中信银行"),
    CEB("CEB", "中国光大银行", "光大银行"),
    PAB("PAB", "平安银行", "平安银行"),
    CIB("CIB", "兴业银行", "兴业银行"),
    CMBC("CMBC", "民生银行", "民生银行"),
    HXB("HXB", "华夏银行", "华夏银行"),
    GDB("GDB", "广发银行", "广发银行"),
    SPDB("SPDB", "上海浦东发展银行", "浦发银行"),
    CZB("CZB", "浙商银行", "浙商银行");

    private String code;
    private String name;
    private String shortName;

    ENJSOPENBANK(String code, String name, String shortName) {
        this.code = code;
        this.name = name;
        this.shortName = shortName;
    }

    public static ENJSOPENBANK findByCode(String code) {
        for (ENJSOPENBANK sCode : ENJSOPENBANK.values()) {
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}

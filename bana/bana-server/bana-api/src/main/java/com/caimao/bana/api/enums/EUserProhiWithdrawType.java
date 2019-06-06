package com.caimao.bana.api.enums;

/**
 * 用户禁止提现的操作类型
 */
public enum EUserProhiWithdrawType {
    CUSTOM("1", "手动限制"),
    POLICE_LINE("2", "警戒线限制"),
    HOMS_ERROR("3", "HOMS流水错误");


    private String value;
    private String code;

    EUserProhiWithdrawType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static EUserProhiWithdrawType findByCode(String code) {
        for (EUserProhiWithdrawType type : EUserProhiWithdrawType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

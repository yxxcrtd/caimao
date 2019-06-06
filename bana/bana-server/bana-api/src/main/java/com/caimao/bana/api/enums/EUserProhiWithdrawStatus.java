package com.caimao.bana.api.enums;

/**
 * 用户禁止提现的状态
 */
public enum EUserProhiWithdrawStatus {
    YES("1", "禁止提现"),
    NO("0", "可提现");

    private String value;
    private String code;

    EUserProhiWithdrawStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static EUserProhiWithdrawStatus findByCode(String code) {
        for (EUserProhiWithdrawStatus type : EUserProhiWithdrawStatus.values()) {
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

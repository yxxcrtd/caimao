package com.caimao.bana.api.enums;

/**
 * P2P借贷的状态
 * Created by WangXu on 2015/4/23.
 */
public enum EP2PLoanStatus {
    INIT(0, "认购中"),
    REPAYMENT(1, "还款中"),
    END(2, "已还款"),
    FAIL(3, "流标"),
    CANCEL(4, "撤销"),
    FAIL_ERROR(5, "流标失败"),
    FULL(6,"满标"),
    COMMIT_LOAN_APPLY(7,"已经发出融资申请");

    private Integer code;
    private String value;

    private EP2PLoanStatus(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static EP2PLoanStatus findByCode(int status) {
        for (EP2PLoanStatus ep2PLoanStatus : EP2PLoanStatus.values()) {
            if (ep2PLoanStatus.getCode() == status) {
                return ep2PLoanStatus;
            }
        }
        return null;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

}
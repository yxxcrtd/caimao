package com.caimao.bana.api.enums;

/**
 * 账户变更枚举信息
 * Created by WangXu on 2015/4/23.
 */
public enum EAccountBizType {
    OTHER("00", "其它"),
    CHARGE("01", "充值"),
    WITHDRAW("02", "取现"),
    LOAN("03", "借款"),
    REPAY("05", "还款"),
    SETTLE("06", "结息"),
    P2PSETTLE("07", "P2P结息"),
    P2P_PRINCIPAL("08", "P2P本金"),
    HOMS_IN("09", "划入Homs子账号"),
    HOMS_OUT("10", "划出Homs子账号"),
    INVS_IN("13", "P2P标-划入"),
    INVS_OUT("14", "P2P标-划出"),
    SCORE_EXCHANGE("15", "积分换钱"),
    LOAN_INTEREST("16", "借贷利息"),
    LOAN_MANAGE_FEE("17", "管理费"),
    FAILED_TARGET("18", "流标返冻结"),
    OLD_USER_RED_PACKAGE("19", "老用户红包"),
    P2P_AUTO_EXT("20", "P2P自动展期"),
    ACTIVITY_RED_PACKAGE("21", "活动红包"),
    MORTGAGE_IN("22", "抵押充值"),
    MORTGAGE_OUT("23", "抵押还款"),
    RED_OUT("24", "红冲"),
    BLUE_IN("25", "蓝补"),
    MARGIN_FROZEN("26", "保证金冻结"),
    MORTGAGE_IN_HUOBI("26", "抵押充值huobi"),
    MORTGAGE_OUT_HUOBI("27", "抵押还款huobi"),
    MORTGAGE_IN_BITVC("28", "抵押充值bitvc"),
    MORTGAGE_OUT_BITVC("29", "抵押还款bitvc");

    private final String code;
    private final String value;

    private EAccountBizType(String code, String value) { this.code = code;
        this.value = value;
    }

    public static EAccountBizType findByCode(String code) {
        for (EAccountBizType eAccountBizType : EAccountBizType.values()) {
            if (eAccountBizType.getCode().equals(code)) {
                return eAccountBizType;
            }
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}

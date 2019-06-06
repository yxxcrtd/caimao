package com.caimao.gjs.server.trade.protocol.sjs.enums;

public enum SJSErrorCode {
    ERROR_CODE_CUSTOM("999999", "系统异常!"),
    ERROR_CODE_HJ2501("HJ2501","没有登录快付通"),
    ERROR_CODE_HJ2502("HJ2502","无效黄金账号"),
    ERROR_CODE_HJ2503("HJ2503","无效商户号"),
    ERROR_CODE_HJ2504("HJ2504","此黄金账号和财猫账号无转账签约关系"),
    ERROR_CODE_HJ2505("HJ2505","内部处理失败，多为数据库问题"),
    ERROR_CODE_HJ2506("HJ2506","原交易不存在"),
    ERROR_CODE_HJ2507("HJ2507","流水号不能为空"),
    ERROR_CODE_HJ2508("HJ2508","快付通代码不能为空"),
    ERROR_CODE_HJ2509("HJ2509","工作日期不能为空"),
    ERROR_CODE_HJ2511("HJ2511","原交易失败（通用）"),
    ERROR_CODE_HJ2512("HJ2512","不是合法的发起方"),
    ERROR_CODE_HJ2513("HJ2513","不是合法的交易类型"),
    ERROR_CODE_HJ2514("HJ2514","不是允许的业务类型"),
    ERROR_CODE_HJ2591("HJ2591","没有权限，不允许此银行向黄金系统发送此交易代码"),
    ERROR_CODE_HJ2592("HJ2592","没有权限，不允许黄金系统向此银行发送此交易代码"),
    ERROR_CODE_HJ2601("HJ2601","黄金账号、财猫账号已经存在正常的签约关系"),
    ERROR_CODE_HJ2602("HJ2602","黄金账号已经存在签约关系，但财猫账号不一致"),
    ERROR_CODE_HJ2603("HJ2603","财猫账号已经存在签约关系，但黄金账号不一致"),
    ERROR_CODE_HJ2604("HJ2604","黄金系统账号不能为空"),
    ERROR_CODE_HJ2605("HJ2605","客户类型不能为空	"),
    ERROR_CODE_HJ2606("HJ2606","币种不能为空	"),
    ERROR_CODE_HJ2701("HJ2701","冲正交易原交易不存在"),
    ERROR_CODE_HJ2702("HJ2702","财猫交易流水号重复"),
    ERROR_CODE_HJ2703("HJ2703","执行冲正成功"),
    ERROR_CODE_HJ2704("HJ2704","转账已成功，不允许银行再次发送响应"),
    ERROR_CODE_HJ2705("HJ2705","转账已处理，不允许银行再次发送响应"),
    ERROR_CODE_HJ2706("HJ2706","已执行冲正失败"),
    ERROR_CODE_HJ2801("HJ2801","执行过银行对账，不能再对账"),
    ERROR_CODE_HJ1001("HJ1001","所要查询的客户号不存在！"),
    ERROR_CODE_HJ5002("HJ5002","该用户当前状态不允许进行此项操作!"),
    ERROR_CODE_HJ5003("HJ5003","您无权对该用户进行此操作！"),
    ERROR_CODE_HJ1000("HJ1000","该客户不存在!"),
    ERROR_CODE_HJ3001("HJ3001","银联验证不通过"),
    ERROR_CODE_HJ3002("HJ3002","短信验证不通过");

    private String code;
    private String value;

    SJSErrorCode(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SJSErrorCode findByCode(String code) {
        for (SJSErrorCode errorCode : SJSErrorCode.values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return ERROR_CODE_CUSTOM;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}


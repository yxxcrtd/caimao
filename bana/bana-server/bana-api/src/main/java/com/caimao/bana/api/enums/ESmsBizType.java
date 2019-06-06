package com.caimao.bana.api.enums;

/**
 * 短信验证码类型
 */
public enum ESmsBizType {
    ALARM("0", "报警"),
    REGISTER("1", "注册"),
    WITHDRAW("2", "取现"),
    SETTRADEPWD("3", "设置交易密码"),
    CHANGEMOBILE("4", "修改手机号码"),
    FINDLONGINPWD("5", "找回登陆密码"),
    FINDTRADEPWD("6", "找回交易密码"),
    RESETPWDANSWER("7", "重置密保问题"),
    LOAN("8", "借款"),
    YBKAPPLY("9", "邮币卡开通"),
    GJSRESETFUNDS("10", "贵金属重置资金密码"),
    GJSRESETTRADE("11", "贵金属重置交易密码"),
    CUSTOM("12", "普通");

    private String code;
    private String value;

    private ESmsBizType(String code, String value) { this.code = code;
        this.value = value;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
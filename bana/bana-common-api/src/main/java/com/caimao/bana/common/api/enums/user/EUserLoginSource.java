package com.caimao.bana.common.api.enums.user;

/**
 * <p>
 *     用户账户登陆来源
 * </p>
 * Created by Administrator on 2015/10/10.
 */
public enum EUserLoginSource {
    YBK_APP("1", "邮币卡APP"),
    YBK_WEB("2", "邮币卡网站"),
    GJS_APP("3", "贵金属APP"),
    GJS_WEB("4", "贵金属网站");

    private String code;
    private String name;

    EUserLoginSource(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EUserLoginSource findByCode(String code) {
        for (EUserLoginSource sCode : EUserLoginSource.values()) {
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
}

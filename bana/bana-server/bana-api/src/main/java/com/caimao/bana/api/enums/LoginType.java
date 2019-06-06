package com.caimao.bana.api.enums;
/**
 * @author yanjg
 * 2015年3月26日
 */
public enum LoginType {


    LOGIN(1),//登录
    REGISTER(2),//注册
    LOGOUT(4);//退出

    private int value;

    LoginType(int value) {
        this.value = value;
    }

    public static LoginType findByValue(int typeValue) {
        for (LoginType sysType : LoginType.values()) {
            if (sysType.getValue() == typeValue) {
                return sysType;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

package com.caimao.bana.api.enums.getui;

/**
 * 个推的消息模板类型
 */
public enum EGetuiActionType {
    TYPE_OPENAPP("1", "打开应用"),
    TYPE_OPENURL("2", "打开URL链接"),
    TYPE_TRANS("3", "透传消息");

    private String value;
    private String name;

    EGetuiActionType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static EGetuiActionType findByValue(String status) {
        for (EGetuiActionType getuiActionType : EGetuiActionType.values()) {
            if (getuiActionType.getValue().equals(status)) {
                return getuiActionType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

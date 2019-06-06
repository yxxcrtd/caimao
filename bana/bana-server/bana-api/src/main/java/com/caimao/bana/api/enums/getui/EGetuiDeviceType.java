package com.caimao.bana.api.enums.getui;

/**
 * 个推的设备类型
 */
public enum EGetuiDeviceType {
    ANDROID("1", "安卓"),
    IPHONE("2", "苹果");

    private String value;
    private String name;

    EGetuiDeviceType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static EGetuiDeviceType findByValue(String status) {
        for (EGetuiDeviceType getuiDeviceType : EGetuiDeviceType.values()) {
            if (getuiDeviceType.getValue().equals(status)) {
                return getuiDeviceType;
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

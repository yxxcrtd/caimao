package com.caimao.bana.api.enums.getui;

/**
 * 个推的发送状态
 */
public enum EGetuiSendStatus {
    STATUS_INIT("1", "待发送"),
    STATUS_SEND("2", "正在发送"),
    STATUS_OK("3", "发送成功"),
    STATUS_FAIL("4", "发送失败");

    private String value;
    private String name;

    EGetuiSendStatus(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static EGetuiSendStatus findByValue(String status) {
        for (EGetuiSendStatus getuiSendStatus : EGetuiSendStatus.values()) {
            if (getuiSendStatus.getValue().equals(status)) {
                return getuiSendStatus;
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

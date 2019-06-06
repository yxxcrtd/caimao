package com.caimao.bana.api.enums;

/**
 * 来源
 */
public enum FromTypeEnum {
    PHP(1,"php"),API(2,"api"),MOBILE(5,"mobile");

    private int value;
    private String name;

    FromTypeEnum(int value,String name) {
        this.value = value;
        this.name=name;
    }

    public FromTypeEnum findByValue(int orderSourceValue) {
        for (FromTypeEnum orderSource : FromTypeEnum.values()) {
            if (orderSource.getValue() == orderSourceValue) {
                return orderSource;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

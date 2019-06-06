package com.caimao.bana.api.enums;

/**
 * 
 * @author yanjg
 * 2015年1月1日
 */
public enum RedisIndex {

    PRICE(0, "price"),COMMON(6, "common");

    private int value;

    private String name;

    RedisIndex(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static RedisIndex findByValue(int coinTypeValue) {
        for (RedisIndex coinType : RedisIndex.values()) {
            if (coinType.getValue() == coinTypeValue) {
                return coinType;
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

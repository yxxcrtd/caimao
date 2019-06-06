package com.caimao.bana.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 资金流向
 * Created by WangXu on 2015/4/23.
 */
public enum ESeqFlag {
    COME("2", "进账"),
    GO("1", "出账");

    private final String code;
    private final String value;

    public static Map<String, ESeqFlag> getSeqFlagMap() { Map map = new HashMap();
        for (ESeqFlag seq : values()) {
            map.put(seq.getCode(), seq);
        }
        return map; }

    private ESeqFlag(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
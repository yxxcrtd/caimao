/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.gate.utils;

import com.huobi.commons.utils.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Administrator $Id$
 */
@Component
public class ValidUtils {

    private Logger logger = LoggerFactory.getLogger(ValidUtils.class);

    /**
     * 验证内部调用接口的签名验证
     * @param hashMap
     * @return
     */
    public static String insiderApiSign(Map<String, Object> hashMap) {
        Map<String, Object> treeMap = new TreeMap<String, Object>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        return obj1.compareTo(obj2);
                    }
                }
        );
        Set<String> keySet = hashMap.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            treeMap.put(key, hashMap.get(key));
        }

        // 拼接加密串
        String signStr = "";
        Set<String> treeKeySet = treeMap.keySet();
        Iterator<String> treeIter = treeKeySet.iterator();
        while (treeIter.hasNext()) {
            String key = treeIter.next();
            signStr += key + "=" + treeMap.get(key).toString() + "&";
        }
        signStr += "key=" + Constants.INSIDER_API_KEY;
        System.out.println(signStr);
        return EncryptUtil.MD5(signStr);
    }
}

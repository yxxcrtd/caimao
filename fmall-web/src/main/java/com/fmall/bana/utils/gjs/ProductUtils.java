package com.fmall.bana.utils.gjs;

import com.alibaba.dubbo.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/27.
 */
public class ProductUtils {


    public static Map<String, String> convertProductSingle(String prodCode) {
        Map<String, String> mapPara = new HashMap<>();
        if (!StringUtils.isBlank(prodCode)) {
            if (prodCode.contains(".")) {
                int index = prodCode.lastIndexOf(".");

                mapPara.put("prodCode", prodCode.substring(0, index));
                mapPara.put("exchange", prodCode.substring(index + 1, prodCode.length()));
            }
        }
        return mapPara;
    }

    public static Map<String, String> convertProductMore(String prodCode) {//prodCode 格式为 AG.SJS,ABK.SJS,==

        Map<String, String> mapPara = new HashMap<>();
        int index = 0;
        if (!StringUtils.isBlank(prodCode)) {
            String[] str = prodCode.split(",");
            String finaceMicT = "";
            String prodCodeT = "";
            if (null != str) {
                for (String temp : str) {
                    if (!StringUtils.isBlank(temp) && temp.contains(".")) {
                        index = temp.lastIndexOf(".");
                        finaceMicT = temp.substring(index + 1, temp.length());
                        prodCodeT = temp.substring(0, index);
                        if (mapPara.containsKey(finaceMicT)) {
                            mapPara.put(finaceMicT, mapPara.get(finaceMicT) + "," + prodCodeT);
                        } else {
                            mapPara.put(finaceMicT, prodCodeT);
                        }
                    }
                }
            }
        }
        return mapPara;
    }
}

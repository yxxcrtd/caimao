package com.caimao.bana.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserFinanceUtils {
    public static HashMap<String, List<HashMap<String, String>>> getAccountBizTypeMap(){
        HashMap<String, List<HashMap<String, String>>> accountBizTypeMap = new HashMap<>();
        //入账
        List<HashMap<String, String>> accountIn = new ArrayList<>();
        accountIn.add(new HashMap<String, String>(){{put("typeCode", "00");put("typeName", "其它");}});
        accountIn.add(new HashMap<String, String>(){{put("typeCode", "01");put("typeName", "充值");}});
        accountIn.add(new HashMap<String, String>(){{put("typeCode", "03");put("typeName", "借款");}});
        accountIn.add(new HashMap<String, String>(){{put("typeCode", "07");put("typeName", "P2P结息");}});
        accountIn.add(new HashMap<String, String>(){{put("typeCode", "08");put("typeName", "P2P本金");}});
        accountIn.add(new HashMap<String, String>(){{put("typeCode", "10");put("typeName", "划出Homs子账号");}});
        accountIn.add(new HashMap<String, String>(){{put("typeCode", "15");put("typeName", "积分换钱");}});
        accountIn.add(new HashMap<String, String>(){{put("typeCode", "18");put("typeName", "流标返冻结");}});
        accountIn.add(new HashMap<String, String>(){{put("typeCode", "19");put("typeName", "老用户红包");}});
        accountBizTypeMap.put("accountIn", accountIn);

        //出账
        List<HashMap<String, String>> accountOut = new ArrayList<>();
        accountOut.add(new HashMap<String, String>(){{put("typeCode", "00");put("typeName", "其它");}});
        accountOut.add(new HashMap<String, String>(){{put("typeCode", "02");put("typeName", "取现");}});
        accountOut.add(new HashMap<String, String>(){{put("typeCode", "05");put("typeName", "还款");}});
        accountOut.add(new HashMap<String, String>(){{put("typeCode", "06");put("typeName", "结息");}});
        accountOut.add(new HashMap<String, String>(){{put("typeCode", "09");put("typeName", "划入Homs子账号");}});
        accountOut.add(new HashMap<String, String>(){{put("typeCode", "14");put("typeName", "P2P标-划出");}});
        accountOut.add(new HashMap<String, String>(){{put("typeCode", "16");put("typeName", "借贷利息");}});
        accountOut.add(new HashMap<String, String>(){{put("typeCode", "17");put("typeName", "管理费");}});
        accountBizTypeMap.put("accountOut", accountOut);

        return accountBizTypeMap;
    }
}

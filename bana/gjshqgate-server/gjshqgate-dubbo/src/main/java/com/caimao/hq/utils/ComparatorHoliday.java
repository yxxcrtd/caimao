package com.caimao.hq.utils;

import com.caimao.hq.api.entity.TradeTime;

import java.util.Comparator;

/**
 * Created by yzc on 2015/11/24.
 */
public class ComparatorHoliday  implements Comparator {

    public int compare(Object arg0, Object arg1) {
        TradeTime tradeTime0=(TradeTime)arg0;
        TradeTime tradeTime1=(TradeTime)arg1;
        int flag=0;
        if(tradeTime0.getOpentime()>=tradeTime1.getOpentime()){
            flag=1;
        }else{
            flag=-1;
        }
        return flag;
    }
}

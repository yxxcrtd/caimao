package com.caimao.bana.server.utils;

import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 邮币卡通用的工具方法
 * Created by Administrator on 2015/9/10.
 */
@Service
public class YbkUtil {

    /**
     * 检查这个交易所有没有开盘
     * 开盘返回 true
     * 收盘返回 false
     * @param entity
     * @return
     */
    public static Boolean checkExchangeOpen(YbkExchangeEntity entity) throws Exception {
        Date nowDate = new Date();
        String nowDateStr = new SimpleDateFormat("yyyy-MM-dd").format(nowDate);

        // 全天交易的
        if (entity.getTradeDayType() == 3) {
            return true;
        }

        // 判断是否是节假日
        String isHoliday = DateUtil.isHoliday(nowDate);

        // 先弄个周一至周五的吧
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        Integer weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        // 周一到周五
        if (entity.getTradeDayType() == 1) {
            if (weekDay == 1 || weekDay == 7) {
                if (!isHoliday.equals("0")) {
                    return false;
                }
            }
        }
        // 周一到周六
        if (entity.getTradeDayType() == 4) {
            if (weekDay == 1) {
                if (!isHoliday.equals("0")) {
                    return false;
                }
            }
        }
        try {
            Date amBeginDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(nowDateStr + " " + entity.getAmBeginTime());
            Date amEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(nowDateStr + " " + entity.getAmEndTime());
            Date pmBeginDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(nowDateStr + " " + entity.getPmBeginTime());
            Date pmEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(nowDateStr + " " + entity.getPmEndTime());

            return (amBeginDate.before(nowDate) && amEndDate.after(nowDate)) || (pmBeginDate.before(nowDate) && pmEndDate.after(nowDate));
        } catch (Exception e) {
            //throw e;
            return false;
        }
    }
}

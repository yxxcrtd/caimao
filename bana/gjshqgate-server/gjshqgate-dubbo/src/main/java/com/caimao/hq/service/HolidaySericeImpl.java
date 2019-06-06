package com.caimao.hq.service;

import com.caimao.hq.api.service.IHolidayService;
import org.springframework.stereotype.Service;

/**
 * Created by huobi on 2015/11/10.
 */
@Service("holidayService")
public class HolidaySericeImpl implements IHolidayService {

    @Override
    public Boolean isTrade(String exchange, long time) {

        String str = String.format("%tF %<tT", time);
        System.out.println(str);

        return true;
    }

}

package com.caimao.hq.junit.hq;

import com.caimao.gjs.api.service.ITradeService;
import com.caimao.hq.api.entity.CandleCycle;
import com.caimao.hq.junit.BaseTest;
import com.caimao.hq.utils.GJSProductUtils;
import com.caimao.hq.utils.HolidayUtil;
import com.caimao.hq.utils.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yzc on 2015/11/24.
 */
public class HolidayTest extends BaseTest {

    @Autowired
    public HolidayUtil holidayUtil;

    @Autowired
    public GJSProductUtils gjsProductUtils;
    @Test
    public void getTimeSub(){


        holidayUtil.fillData("SJS", "Au(T+D)", DateUtils.getNoTime("yyyyMMddHHmm"), "2");

    }


    @Test
    public void fillData(){
        gjsProductUtils.fillData();
    }

}

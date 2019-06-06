package ybk;

import com.caimao.bana.api.entity.ybk.YBKTimeLineEntity;
import com.caimao.bana.api.service.ybk.IYBKLineService;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

public class TestYBKLine extends BaseTest {
    @Resource
    private IYBKLineService ybkLineService;

    @Test
    public void TestUpdateKLine() throws Exception{
        for (int i = 1; i <= 1000; i++){
            Random random = new Random();
            YBKTimeLineEntity ybkTimeLineEntity = new YBKTimeLineEntity();
            ybkTimeLineEntity.setExchangeName("nanjing");
            ybkTimeLineEntity.setCode("100001");
            ybkTimeLineEntity.setDatetime(new Date(1441848600000L + 10800000L * i));
            ybkTimeLineEntity.setYesterBalancePrice(360844L + random.nextInt(1000) * i);
            ybkTimeLineEntity.setOpenPrice(359977L + random.nextInt(1000) * i);
            ybkTimeLineEntity.setCurPrice(368693L + random.nextInt(1000) * i);
            ybkTimeLineEntity.setCurrentGains(218L + 2 * i);
            ybkTimeLineEntity.setTotalAmount(10283545L + 200 * i);
            ybkTimeLineEntity.setTotalMoney(2794440960L + 200 * i);
            ybkTimeLineEntity.setHighPrice(374694L + random.nextInt(1000) * i);
            ybkTimeLineEntity.setLowPrice(359861L + random.nextInt(1000) * i);
            ybkLineService.updateKLine(ybkTimeLineEntity);
        }
    }

    @Test
    public void testMACD() throws Exception{
        int i = 1000;
        YBKTimeLineEntity ybkTimeLineEntity = new YBKTimeLineEntity();
        ybkTimeLineEntity.setExchangeName("nanjing");
        ybkTimeLineEntity.setCode("100001");
        ybkTimeLineEntity.setDatetime(new Date(1441848600000L + 10800000L * i));
        ybkTimeLineEntity.setYesterBalancePrice(360844L + 200 * i);
        ybkTimeLineEntity.setOpenPrice(359977L + 200 * i);
        ybkTimeLineEntity.setCurPrice(368693L + 200 * i);
        ybkTimeLineEntity.setCurrentGains(218L + 2 * i);
        ybkTimeLineEntity.setTotalAmount(10283545L + 200 * i);
        ybkTimeLineEntity.setTotalMoney(2794440960L + 200 * i);
        ybkTimeLineEntity.setHighPrice(374694L + 200 * i);
        ybkTimeLineEntity.setLowPrice(359861L + 200 * i);
        ybkLineService.updateKLine(ybkTimeLineEntity);

    }
}

package other;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.SysParameterService;
import com.caimao.bana.server.utils.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import parent.BaseTest;

import com.caimao.bana.api.entity.res.FIndexPZRankingRes;
import com.caimao.bana.api.entity.res.FIndexRealtimePZRes;
import com.caimao.bana.server.service.othder.OtherDataServiceImpl;

/**
 * 数据测试
 * Created by WangXu on 2015/5/28.
 */
public class TestOtherDataService extends BaseTest {

    @Autowired
    private OtherDataServiceImpl otherDataService;

    @Test
    public void StringTrim() {
//        String a = "   as ";
//        System.out.println(a.trim());
//        String startDate = com.huobi.commons.utils.DateUtil.toString(new Date(), com.huobi.commons.utils.DateUtil.DEFAULT_SHORT_FORMAT);
//        System.out.println(startDate);
        Date interestTime = new Date();
        Integer n1 = com.huobi.commons.utils.DateUtil.getDayOfYear(com.huobi.commons.utils.DateUtil.addDays(interestTime, 1));
        Integer n2 = com.huobi.commons.utils.DateUtil.getDayOfYear(new Date());
        System.out.println(n1);
        System.out.println(n2);
        System.out.println(n1 <= n2);
    }

    @Test
    public void isMobile() throws Exception {
        String phone = "186128392152";
        Pattern p = Pattern.compile("^[1][0-9]{10}$"); // 验证手机号
        Matcher m = p.matcher(phone);
        boolean b = m.matches();
        if (b == false) {
            throw new CustomerException("手机号码格式错误", 888888);
        }
    }

    @Test
    public void TestDate() throws Exception {
        // 获取明天的日期
        Date jintianDate = new Date();
        System.out.println("今天日期：" + jintianDate);
        Date mingtianDate = DateUtil.addDays(jintianDate, -1);
        System.out.println("明天日期：" + mingtianDate);

        // 进行格式化
        String jintianStr = DateUtil.convertDateToString(DateUtil.DATA_TIME_PATTERN_1, jintianDate);
        System.out.println("今天日期 字符串：" + jintianStr);

        String dateStr = "20150618";
        Date date = DateUtil.convertStringToDate(DateUtil.DATE_FORMAT_STRING, dateStr);
        System.out.println("字符串转换成日期：" + date);
    }

    @Test
    public void TestindexRealtimePZList() throws Exception {
        List<FIndexPZRankingRes> fIndexPZRankingReses = this.otherDataService.indexPzRankingList(10);
        System.out.println("查询到：" + fIndexPZRankingReses.size());
        for (int i = 0; i < fIndexPZRankingReses.size(); i++) {
            System.out.println(ToStringBuilder.reflectionToString(fIndexPZRankingReses.get(i)));
        }
    }

    @Test
    public void TestindexPzRankingList() throws Exception {
        List<FIndexRealtimePZRes> fIndexRealtimePZReses = this.otherDataService.indexRealtimePZList(10);
        System.out.println("查询到：" + fIndexRealtimePZReses.size());
        for (int i = 0; i < fIndexRealtimePZReses.size(); i++) {
            System.out.println(ToStringBuilder.reflectionToString(fIndexRealtimePZReses.get(i)));
        }
    }


    @Test
    public void TestLoanInterestFix() throws Exception {
        this.otherDataService.prodLoanInterestFix();
    }

    @Test
    public void testSettlePZInterestNotice() throws Exception {
        this.otherDataService.settlePZInterestNotice();
    }
}

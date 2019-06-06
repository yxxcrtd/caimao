package account;

import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.api.enums.EUserProhiWithdrawType;
import com.caimao.bana.api.service.SmsService;
import com.caimao.bana.server.dao.AccruedInterestBillDAO;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.service.sms.SmsServiceImpl;
import com.caimao.bana.server.utils.AccountManager;
import com.caimao.bana.server.utils.BigMath;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.omg.DynamicAny._DynAnyFactoryStub;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import javax.annotation.Resource;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by WangXu on 2015/4/23.
 */
public class TestTpzAccountDao extends BaseTest {

    @Resource
    private TpzAccountDao tpzAccountDao;

    @Autowired
    private MemoryDbidGenerator memoryDbidGenerator;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private SmsServiceImpl smsService;

    @Test
    public void sendSms() throws Exception {
        String text = "wangxu@huobi.com,ninghao@huobi.com";
        String[] emails = text.split(",");
        for (String e:emails) {
            System.out.println(e);
        }
//        Long l = new BigDecimal(19000L).subtract(new BigDecimal(20000L).multiply(new BigDecimal(1.2))).longValue();
//        System.out.println(l);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String beginDate = "2015-06-17 12:00:00";
//        String compareDate = "2015-06-17 12:00:00";
//
//        // ? 开始时间小于比较时间  -1
//        System.out.println(sdf.parse(beginDate).compareTo(sdf.parse(compareDate)));
//
//        // 比较时间大于比较时间 1
//        System.out.println(sdf.parse(compareDate).compareTo(sdf.parse(beginDate)));
//
//        System.out.println(new Date().getTime());
//
//        //this.smsService.sendSms("18210046820", "我是中文内容");
//        BigDecimal minRate = new BigDecimal(0.12);
//        BigDecimal maxRate = new BigDecimal(0.15);
//        BigDecimal manageFee = new BigDecimal(0.033);
//        BigDecimal yearRatre = new BigDecimal(0.16);
//        // 最小的年利率为：10.2
//        if (BigMath.compareTo(BigMath.subtract(yearRatre, manageFee), minRate) == -1) {
//            manageFee = BigMath.subtract(yearRatre, minRate);
//        }
//        // 如果之后的利率比最大的大，年利率减去最大利率
//        if (BigMath.compareTo(BigMath.subtract(yearRatre, manageFee), maxRate) == 1) {
//            manageFee = BigMath.subtract(yearRatre, maxRate);
//        }
//        if (BigMath.compareTo(manageFee, 0) == -1) {
//            manageFee = new BigDecimal(0);
//        }
//        System.out.println(manageFee.doubleValue());
//
//
//        System.out.println(EUserProhiWithdrawType.findByCode("1").getValue());
    }

    @Test
    public void testGeneratorId() {
        System.out.println(this.memoryDbidGenerator.getNextId());
    }


    @Test
    /**
     * 获取用户的资产信息
     */
    public void testGetAccountByUserId() {
        Long userId = 799436825427971L;
        TpzAccountEntity a = this.tpzAccountDao.getAccountByUserId(userId);
        System.out.println(ToStringBuilder.reflectionToString(a));
    }

    @Test
    /**
     * 加钱的方法
     */
    public void testDoUpdateAvaiAmount() throws Exception {
        Long userId = 799869644046338L;
        Long amount = 100L;
        TpzAccountEntity a = this.tpzAccountDao.getAccountByUserId(userId);
        this.accountManager.doUpdateAvaiAmount("", a.getPzAccountId(), amount, EAccountBizType.CHARGE.getCode(), ESeqFlag.COME.getCode());
    }


    @Test
    public void testList() {



//        List<String> list = new ArrayList();
//        list.add("1");
//        list.add("2");
//        list.add("3");
//        list.add("4");
//        for (int i = 0; i < list.size(); i++) {
//            String s = list.get(i);
//            s = s + "--";
//            System.out.println(s);
//        }
//
//        for (int i = 0; i < list.size(); i++) {
//            String s = list.get(i);
//            System.out.println(s);
//        }
//
//        Iterator ite = list.iterator();
//        while (ite.hasNext()) {
//            String s = (String)ite.next();
//            s += "++";
//        }
//        for (int i = 0; i < list.size(); i++) {
//            String s = list.get(i);
//            System.out.println(s);
//        }



//        System.out.println(list.size());
//
//        list.remove(2);
//
//        System.out.println(list.size());
//
//        ConcurrentLinkedDeque<String> strQueue = new ConcurrentLinkedDeque<>();
//        for (int i = 0; i < 10; i++) {
//            strQueue.offer("" + i);
//        }
//        String value = null;
//        while (!strQueue.isEmpty()) {
//            value = strQueue.poll();
//            try {
//                if ("5".equals(value)) {
//                    throw new Exception("5");
//                }
//                System.out.println(value);
//            } catch (Exception e) {
//                strQueue.offer("5");
//                 try {
//                     Thread.sleep(2000);
//                 } catch (Exception e1) {
//                 }
//            }
//            System.out.println("size:" + strQueue.size());
//        }

//        for (int i = 0; i < list.size(); i++) {
//            try {
//                if (i == 2) {
//                    throw new Exception();
//                }
//                System.out.println(i + " " + list.get(i));
//            } catch (Exception e) {
//                continue;
//            }
//            list.remove(i);
//        }
    }
}

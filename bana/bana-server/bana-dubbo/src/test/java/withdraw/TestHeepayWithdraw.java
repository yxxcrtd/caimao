package withdraw;

import com.caimao.bana.server.service.account.AccountServiceImpl;
import com.caimao.bana.server.service.withdraw.HeepayWithdrawImpl;
import com.caimao.bana.server.utils.ChannelUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.List;
import java.util.Map;

/**
 * Created by WangXu on 2015/5/15.
 */
public class TestHeepayWithdraw extends BaseTest {

    @Autowired
    private HeepayWithdrawImpl heepayWithdraw;
    @Autowired
    private AccountServiceImpl accountService;

    @Test
    public void TestAnalysisHeepayBatchDetailData() {
        String str = "商户流水号^收款人帐号^收款人姓名^付款金额^付款状态|商户流水号^收款人帐号^收款人姓名^付款金额^付款状态";
        List<Map<String, Object>> list = ChannelUtil.analysisHeepayBatchDetailData(str);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(
                    String.format(
                            "orderId %s bankCardNo %s bankCardName %s amount %s status %s",
                            list.get(i).get("orderId"), list.get(i).get("bankCardNo"), list.get(i).get("bankCardName"), list.get(i).get("amount"), list.get(i).get("status"))
            );
        }
    }


    @Test
    public void TestDoApplyWithdraw() throws Exception {
        Long userId = 802233016909825L;
        String tradePwd = "wangxu123,";
        Long orderAmoutn = 10000L;
        String orderName = "测试用例提现";
        String orderAbstract = "测试用例提现说明";
    }


    @Test
    public void TestDoWithdraw() throws Exception {
        Long orderId = 801372479946753L;
        boolean res = this.heepayWithdraw.doWithdraw(orderId);
        System.out.println("订单 " + orderId + " 提现请求结果 " + res);
    }

    @Test
    public void TestRestunXml() throws Exception{
        String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root><ret_code>0000</ret_code><ret_msg>创建单据成功</ret_msg><sign>0368aba6b6b2889629ee72566f60a627</sign></root>";
        Document document = null;
        document = DocumentHelper.parseText(xmlStr);
        Element rootElement = document.getRootElement();
        String retCode = rootElement.elementTextTrim("ret_code");
        System.out.println(retCode);
    }

    @Test
    public void TestDoWithdrawQuery() throws Exception{
        Long orderId = 802549133213697L;
        Map<String, Object> res = this.heepayWithdraw.doWithdrawQuery(orderId);
        // 返回值
        System.out.println("结果：" + res.get("result"));
        System.out.println("消息：" + res.get("msg"));
    }


}

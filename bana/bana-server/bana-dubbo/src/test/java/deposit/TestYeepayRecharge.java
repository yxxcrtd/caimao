package deposit;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caimao.bana.api.service.IYeepayRecharge;
import com.huobi.commons.utils.HttpUtil;
import com.huobi.commons.utils.JsonUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.List;
import java.util.Map;

/**
 * 易付宝支付测试
 * Created by WangXu on 2015/5/21.
 */
public class TestYeepayRecharge extends BaseTest {

    @Autowired
    private IYeepayRecharge yeepayRecharge;

    @Test
    public void TestCreateMobilePayUrl() throws Exception {
        Long userId = 799869644046338L;
        Long orderAmount = 100L;
        String userIp = "1.202.14.86";
        String userUA = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36";

        String payUrl = this.yeepayRecharge.doPreMobileCharge(userId, orderAmount, userIp, userUA);

        System.out.println("支付URL： "+payUrl);

        // 创建一个URI实例
        java.net.URI uri = java.net.URI.create(payUrl);

        // 获取当前系统桌面扩展
        java.awt.Desktop dp = java.awt.Desktop.getDesktop();
        // 判断系统桌面是否支持要执行的功能
        if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
            // 获取系统默认浏览器打开链接
            dp.browse(uri);
        }
    }

    @Test
    public void TestCheckMobilePayRes() throws Exception {
        Long orderNo = 800942547009537L;
        boolean res = this.yeepayRecharge.checkMobilePayRes(orderNo);

        System.out.println(res);
    }

    @Test
    public void TestGetUrl() throws Exception {
        String payUrl = "https://www.caimao.com";
        String payRes = HttpUtil.doGet(payUrl);
        System.out.println(payRes);
    }


    @Test
    public void TestJson() throws Exception {
        String jsonStr = "{\"data\":\"cGrS8p4SKSbkqjiUAUOC6A8/OrTHVClFMUWaqo0uIBl2XXIq8tJy3zSJU0yDdqiXco3IXhEUCrHOwf+DO5fKM0rNdI/wdpc80ewzmoK50w0/8BL2lQ8w669rx3DuZtymU6rrQSUn2cfruXWN5DhYKgRdLCvVNxY9mscKP1D/O8NDj6tRA9HOAkxHrWNMJrLzi9FuWWX0m8rAZM+1zSgzcvJv3rmC8zN3NFvClqxQQK08M7IFeC0tIAA1fkvAC8Y8SwUUhHteiflzJYyTCRLfZYcAu5u6HlyPvsHh01AYo7U6Q2k7jfwoItyccXZkO+neYm+A6b1IQqKzkxO9nAzGOVHbJRnGA7q6Jv+HpPggUg+ePC5LOzOl/6Cmn7/o/p9Xl9njqyL7c4alAlkXp8EzyNZsTrLjfNXJ/BGWLdv/II+Et/+nHjKu9yUCgq74IGpP81Fe9C/UDOKmyqVlqsjxuXBmdEqrAtV9mfgQNlZdM3HmSCR6mRR+e+mQfZy6e81OrzNyPNzJM6fiigP1aUh0d4TYUW150NHwf/vAAAalStIK6J0gzhcqvgpI0XNIY9HlJhDZjO9RhQtEAXTDRsRBX99RWGaqD7PUBX1yO+tuwG9kOK+pjRrOGkt4WjYRYqT71b4AjzIgjpcabNxmGoeVKPypTkBjxQ8UYMHwpFZSIvh85UAnbpBfFJGMqlETLOjrD2SFBtWg/5+2xWfDnqcos1iACB07zir8iBReD73qUPFKSuDlDiTRUo0Z4OO8tPut4HOczkByUi6fnoVk2L5QXh9Wqu4lalSsqxChgh5G2Xg=\",\"encryptkey\":\"Dc+xfXnrbeFOmGoWeriJv40WALh8CD8TemOhAV7/clkovevJQ7p0vaZASVFwRZHBCYkXl97a/sI62vtQtljVteebF/9KvZ0PNFp5jeqdBwZhDiD9l6sTwh7xmki/jXcink6F2niCjxWCUiI8Nppqe5seA0UtdUeoL7ogJrx4D5k=\"}";

        Map<String , Object> json = JsonUtil.toObject(jsonStr, Map.class);

        System.out.println(json.get("data"));
        System.out.println(json.get("encryptkey"));
    }
}

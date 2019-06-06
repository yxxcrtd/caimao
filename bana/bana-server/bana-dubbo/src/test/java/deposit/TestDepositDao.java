package deposit;

import com.caimao.bana.api.entity.TpzChargeOrderEntity;
import com.caimao.bana.api.entity.TpzPayChannelEntity;
import com.caimao.bana.server.dao.depositDao.TpzChargeOrderDao;
import com.caimao.bana.server.dao.depositDao.TpzPayChannelDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.Date;

/**
 * Created by WangXu on 2015/4/23.
 */
public class TestDepositDao extends BaseTest {

    @Autowired
    private TpzChargeOrderDao tpzChargeOrderDao;
    @Autowired
    private TpzPayChannelDao tpzPayChannelDao;

    @Test
    /**
     * 保存充值记录
     */
    public void testChargeSave() {
        Date date = new Date();
        TpzChargeOrderEntity o = new TpzChargeOrderEntity();
        o.setBankCardName("1");
        o.setBankCardNo("1");
        o.setBankResultCode("1");
        o.setBankResultCodeNote("1");
        o.setBankSuccessDatetime(date);
        o.setBankCode("1");
        o.setBankNo("1");
        o.setChannelServiceCharge(1L);
        o.setChannelId(1L);
        o.setCheckStatus("1");
        o.setCreateDatetime(date);
        o.setCurrencyType("1");
        o.setCheckStatus("1");
        o.setOrderAbstract("1");
        o.setOrderAmount(1L);
        o.setOrderStatus("1");
        o.setOrderNo(1L);
        o.setOrderName("1");
        o.setPaySubmitDatetime(date);
        o.setPayType("1");
        o.setPzAccountId(1L);
        o.setPayId("1");
        o.setPayResultDatetime(date);
        o.setPayResultNote("1");
        o.setUpdateDatetime(date);
        o.setUserId(1L);
        o.setVerifyDatetime(date);
        o.setVerifyUser("1");
        o.setWorkDate("1");
        o.setRemark("1");

        this.tpzChargeOrderDao.save(o);
    }

    @Test
    /**
     * 根据订单ID获取订单信息
     */
    public void testGetChargeOrderByOrderNo() {
        Long orderNo = 1L;
        TpzChargeOrderEntity o = this.tpzChargeOrderDao.getChargeOrderByOrderNo(orderNo);
        System.out.println(o);
    }

    @Test
    /**
     * 测试更新充值记录的方法
     */
    public void testUpdate() {
        Date date = new Date();
        TpzChargeOrderEntity o = new TpzChargeOrderEntity();
        o.setBankCardName("2");
        o.setBankCardNo("2");
        o.setBankResultCode("2");
        o.setBankResultCodeNote("2");
        o.setBankSuccessDatetime(date);
        o.setBankCode("2");
        o.setBankNo("2");
        o.setChannelServiceCharge(2L);
        o.setChannelId(2L);
        o.setCheckStatus("2");
        o.setCreateDatetime(date);
        o.setCurrencyType("2");
        o.setCheckStatus("2");
        o.setOrderAbstract("2");
        o.setOrderAmount(2L);
        o.setOrderStatus("2");
        o.setOrderNo(1L);
        o.setOrderName("2");
        o.setPaySubmitDatetime(date);
        o.setPayType("2");
        o.setPzAccountId(2L);
        o.setPayId("2");
        o.setPayResultDatetime(date);
        o.setPayResultNote("2");
        o.setUpdateDatetime(date);
        o.setUserId(2L);
        o.setVerifyDatetime(date);
        o.setVerifyUser("2");
        o.setWorkDate("2");
        o.setRemark("2");

        this.tpzChargeOrderDao.update(o);
    }

    @Test
    /**
     * 删除指定的充值记录
     */
    public void testDelete() {
        this.tpzChargeOrderDao.delete(1L);
    }

    @Test
    /**
     * 获取指定的充值渠道信息
     */
    public void testGetPayById() {
        Long channelId = 1L;
        TpzPayChannelEntity p = this.tpzPayChannelDao.getPayById(channelId);
        System.out.println(p);
    }

}

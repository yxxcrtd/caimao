package homs;

import com.caimao.bana.api.entity.TpzHomsAccountLoanEntity;
import com.caimao.bana.api.entity.req.FZeusHomsAccountAssetsReq;
import com.caimao.bana.api.entity.res.other.FHomsNeedUpdateAssetsRes;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountAssetsEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountHoldEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsRepaymentExcludeEntity;
import com.caimao.bana.server.service.homs.HomsAccountServiceImpl;
import com.caimao.bana.server.utils.DateUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.Date;
import java.util.List;

/**
 * homs
 * Created by WangXu on 2015/6/12.
 */
public class TestHomsAccountLoanService extends BaseTest {

    @Autowired
    private HomsAccountServiceImpl homsAccountService;

    @Test
    public void valideUserHomsAccount() throws Exception {
        Long userId = 799444006076419L;
        String homsCombineId = "17032";
        String homsFundAccount = "28570001";

        this.homsAccountService.valideUserHomsAccount(userId, homsCombineId, homsFundAccount);
    }

    @Test
    public void saveZeusHomsAssets() throws Exception {
        ZeusHomsAccountAssetsEntity entity = new ZeusHomsAccountAssetsEntity();
        entity.setUserId(802185872932865L);
        entity.setUserName("王旭");
        entity.setMobile("18612839215");
        entity.setContractNo(800173311655937L);
        entity.setHomsCombineId("1");
        entity.setHomsFundAccount("2");
        entity.setBeginAmount(3L);
        entity.setCurAmount(4L);
        entity.setCurrentCash(5L);
        entity.setEnableRatio(1.1);
        entity.setEnableWithdraw(6L);
        entity.setExposureRatio(1.2);
        entity.setLoanAmount(7L);
        entity.setTotalAsset(8L);
        entity.setTotalMarketValue(9L);
        entity.setTotalNetAssets(10L);
        entity.setTotalProfit(-11L);
        entity.setUpdateDate(DateUtil.convertDateToString(DateUtil.DISPLAY_DATE_FORMAT_STRING, new Date()));
        Integer res = this.homsAccountService.saveZeusHomsAssets(entity);
        System.out.println(res);
    }

    @Test
    public void searchZeusHomsAssetsList() throws Exception {
        FZeusHomsAccountAssetsReq req = new FZeusHomsAccountAssetsReq();
        req.setMatch("王");
        req.setUpdateDate(DateUtil.convertDateToString(DateUtil.DISPLAY_DATE_FORMAT_STRING, new Date()));
        //req.setType(1);

        req = this.homsAccountService.searchZeusHomsAssetsList(req);
        System.out.println("查找符合条件的记录条数：" + req.getItems().size());
        for (ZeusHomsAccountAssetsEntity entity : req.getItems()) {
            System.out.println(ToStringBuilder.reflectionToString(entity));
        }
    }

    @Test
    public void getZeusHomsAssets() throws Exception {
        ZeusHomsAccountAssetsEntity entity = new ZeusHomsAccountAssetsEntity();
        entity.setUserName("王旭");

        entity = this.homsAccountService.getZeusHomsAssets(entity);

        System.out.println(ToStringBuilder.reflectionToString(entity));
    }

    @Test
    public void queryNeedUpdateAssetsList() throws Exception {
        String updateDate = DateUtil.convertDateToString(DateUtil.DISPLAY_DATE_FORMAT_STRING, new Date());
        List<FHomsNeedUpdateAssetsRes> resList = this.homsAccountService.queryNeedUpdateAssetsList(updateDate);
        System.out.println(resList.size());
        for (FHomsNeedUpdateAssetsRes res : resList) {
            System.out.println(ToStringBuilder.reflectionToString(res));
        }
    }

    @Test
    public void testSaveZeusHomsAccountHold() throws Exception{
        ZeusHomsAccountHoldEntity zeusHomsAccountHoldEntity = new ZeusHomsAccountHoldEntity();
        zeusHomsAccountHoldEntity.setHomsFundAccount("111");
        zeusHomsAccountHoldEntity.setHomsCombineId("222");
        zeusHomsAccountHoldEntity.setExchangeType(new Byte("1"));
        zeusHomsAccountHoldEntity.setStockCode("333333");
        zeusHomsAccountHoldEntity.setStockName("44444");
        zeusHomsAccountHoldEntity.setCurrentAmount("2000");
        zeusHomsAccountHoldEntity.setEnableAmount("2000");
        zeusHomsAccountHoldEntity.setCostBalance("2000");
        zeusHomsAccountHoldEntity.setMarketValue("2000");
        zeusHomsAccountHoldEntity.setBuyAmount("2000");
        zeusHomsAccountHoldEntity.setSellAmount("2000");
        zeusHomsAccountHoldEntity.setUpdated("2015-02-01");
        homsAccountService.saveZeusHomsAccountHold(zeusHomsAccountHoldEntity);
    }

    @Test
    public void testQueryTpzHomsAccountLoan() throws Exception{
        TpzHomsAccountLoanEntity tpzHomsAccountLoanEntity = homsAccountService.queryTpzHomsAccountLoan("11", "22552203");
        System.out.println(ToStringBuilder.reflectionToString(tpzHomsAccountLoanEntity));

    }

    @Test
    public void testQueryUpdated() throws Exception{
        List<String> result = homsAccountService.queryUpdated("2015-02-01");
        System.out.println(result);
    }

    @Test
    public void testQ() throws Exception {
        List<ZeusHomsRepaymentExcludeEntity> excludeEntityList = this.homsAccountService.queryHomsRepaymentExcludeList("22123", "22552204");
        System.out.println(excludeEntityList.size());
    }

    @Test
    public void testCheckAbnormalRepayAccountJour() throws Exception{
        homsAccountService.checkAbnormalRepayAccountJour();
    }
}

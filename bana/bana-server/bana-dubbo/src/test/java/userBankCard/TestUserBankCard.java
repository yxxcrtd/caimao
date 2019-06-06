package userBankCard;

import com.caimao.bana.api.entity.TpzUserBankCardEntity;
import com.caimao.bana.api.enums.EBankCardStatus;
import com.caimao.bana.api.service.IUserBankCardService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.List;

/**
 * Created by WangXu on 2015/5/14.
 */
public class TestUserBankCard extends BaseTest {

    @Autowired
    private IUserBankCardService userBankCardService;

    @Test
    public void TestQueryUserBankList() {
        Long userId = 799869644046338L;
        String bankCardStatus = EBankCardStatus.YES.getCode();
        System.out.println("银行卡状态：" + bankCardStatus);
        List<TpzUserBankCardEntity> userBankCardEntityList = this.userBankCardService.queryUserBankList(userId, bankCardStatus);

        System.out.println("查询出 : " + userBankCardEntityList.size() + " 条银行卡信息");

        for(int i = 0; i < userBankCardEntityList.size(); i++) {
            TpzUserBankCardEntity userBankCardEntity = userBankCardEntityList.get(i);
            System.out.println(ToStringBuilder.reflectionToString(userBankCardEntity));
        }
    }

    @Test
    public void TestDoBindBankCard() throws Exception {
        Long userId = 799869644046338L;
        String bankNo = "001";
        String bankCardNo = "6226333214452542";
        String province = "北京省";
        String city = "北京市";
        String openBank = "上地支行";

        boolean res = this.userBankCardService.doBindBankCard(userId, bankNo, bankCardNo, province, city, openBank);
        System.out.println("绑定银行卡成功 : " + res);
    }
}

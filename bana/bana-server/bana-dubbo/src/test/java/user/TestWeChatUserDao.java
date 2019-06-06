package user;

import com.caimao.bana.api.entity.WeChatForecastCountEntity;
import com.caimao.bana.api.entity.WeChatForecastEntity;
import com.caimao.bana.api.entity.WeChatUserEntity;
import com.caimao.bana.api.service.WeChatUserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestWeChatUserDao extends BaseTest {

    @Resource
    private WeChatUserService weChatUserService;

    @Test
    public void testCreateWeChatUser() throws Exception{
        WeChatUserEntity weChatUserEntity = new WeChatUserEntity();
        weChatUserEntity.setOpenid("o4BrVsjqjAIqH2SvNeX772AXBK_8");
        weChatUserEntity.setNickname("宁大爷");
        weChatUserEntity.setSex(new Byte("1"));
        weChatUserEntity.setCity("朝阳");
        weChatUserEntity.setProvince("北京");
        weChatUserEntity.setCountry("中国");
        weChatUserEntity.setHeadImgUrl("http://wx.qlogo.cn/mmopen/PiajxSqBRaEJcr5AKO8JkGHICAVAAYA2KQxCJtbbZg46Q7UAibhruiahxaoOT0QEC4u70mUIdJXXt15zAIMqBltSQ/0");
        weChatUserEntity.setCreated(System.currentTimeMillis() / 1000);
        this.weChatUserService.createWeChatUser(weChatUserEntity);
    }

    @Test
    public void testGetWeChatUserByOpenid() throws Exception{
        WeChatUserEntity weChatUserEntity = this.weChatUserService.getWeChatUserByOpenid("o4BrVsjqjAIqH2SvNeX772AXBK_8");
        System.out.println(weChatUserEntity);
    }

    @Test
    public void testUpdateWeChatUser() throws Exception{
        String openid = "o4BrVsjqjAIqH2SvNeX772AXBK_8";
        Map<String, Object> updateParamMap = new HashMap<>();
        updateParamMap.put("nickname", "宁大爷修改");
        updateParamMap.put("sex", new Byte("2"));
        updateParamMap.put("city", "宁大爷修改");
        updateParamMap.put("province", "宁大爷修改");
        updateParamMap.put("country", "宁大爷修改");
        updateParamMap.put("headImgUrl", "宁大爷修改");
        this.weChatUserService.updateWeChatUser(openid, updateParamMap);
    }

    @Test
    public void testCreateMarketForecast() throws Exception{
        WeChatForecastEntity weChatForecastEntity = new WeChatForecastEntity();
        weChatForecastEntity.setForecastDate(33333L);
        weChatForecastEntity.setWeChatUserId(44444L);
        weChatForecastEntity.setForecastType(new Byte("1"));
        weChatForecastEntity.setCreated(System.currentTimeMillis() / 1000);
        this.weChatUserService.createMarketForecast(weChatForecastEntity);
    }

    @Test
    public void testGetUserMarketForecast() throws Exception{
        WeChatForecastEntity weChatMarketForecastEntity = this.weChatUserService.getUserMarketForecast(111111L, 111111L);
        System.out.println(weChatMarketForecastEntity);
    }

    @Test
    public void testGetMarketForecastByDate() throws Exception{
        Map<String,String> result = this.weChatUserService.getMarketForecastByDate(111111L);
        System.out.println(result);
    }

    @Test
    public void testCreateUserMarketForecastCount() throws Exception{
        WeChatForecastCountEntity weChatForecastCountEntity = new WeChatForecastCountEntity();
        weChatForecastCountEntity.setWeChatUserId(1L);
        weChatForecastCountEntity.setCorrectDays(10);
        weChatForecastCountEntity.setBeatRate(new BigDecimal("89.75"));
        weChatForecastCountEntity.setCreated(System.currentTimeMillis() / 1000);
        weChatForecastCountEntity.setUpdated(System.currentTimeMillis() / 1000);
        this.weChatUserService.createUserMarketForecastCount(weChatForecastCountEntity);
    }

    @Test
    public void testUpdateUserMarketForecastCount() throws Exception{
        Long weChatUserId = 1L;
        Map<String, Object> updateParamMap = new HashMap<>();
        updateParamMap.put("correctDays", 11L);
        updateParamMap.put("beatRate", new BigDecimal("99.99"));
        updateParamMap.put("updated", System.currentTimeMillis() / 1000);
        this.weChatUserService.updateUserMarketForecastCount(weChatUserId, updateParamMap);
    }

    @Test
    public void testGetUserMarketForecastCount() throws Exception{
        WeChatForecastCountEntity weChatForecastCountEntity = this.weChatUserService.getUserMarketForecastCount(1L);
        System.out.println(ToStringBuilder.reflectionToString(weChatForecastCountEntity));
    }

    @Test
    public void testGetTodayForecastSuccess() throws Exception{
        List<WeChatForecastEntity> weChatForecastEntityList = this.weChatUserService.getTodayForecastSuccess(new Byte("1"));
        System.out.println(weChatForecastEntityList);
    }

    @Test
    public void testGetAllWeChatForecastCount() throws Exception{
        List<WeChatForecastCountEntity> weChatForecastCountEntityList = this.weChatUserService.getAllWeChatForecastCount();
        System.out.println(weChatForecastCountEntityList);
    }

    @Test
    public void testGetUserBeatCnt() throws Exception{
        Integer cnt = this.weChatUserService.getUserBeatCnt(20);
        System.out.println(cnt);
    }

    @Test
    public void testGetWeChatForecastCountCnt() throws Exception{
        Integer cnt = this.weChatUserService.getWeChatForecastCountCnt();
        System.out.println(cnt);
    }
}

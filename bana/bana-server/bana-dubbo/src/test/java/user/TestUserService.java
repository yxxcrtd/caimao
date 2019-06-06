package user;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.entity.req.user.FUserProhiWithdrawReq;
import com.caimao.bana.api.entity.req.user.FUserQueryProhiWithdrawLogReq;
import com.caimao.bana.api.entity.res.user.FUserQueryProhiWithdrawLogRes;
import com.caimao.bana.api.enums.EUserInit;
import com.caimao.bana.api.enums.EUserProhiWithdrawStatus;
import com.caimao.bana.api.enums.EUserProhiWithdrawType;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.server.utils.ApplicationContextUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 用户服务单元测试
 * Created by WangXu on 2015/5/25.
 */
public class TestUserService extends BaseTest {

    @Autowired
    private IUserService userService;


    @Test
    public void TestGetUserIdByPhone() throws Exception {
        String phone = "18612839815";
        Long userId = this.userService.getUserIdByPhone(phone);
        System.out.println("根据手机号查询用户ID，返回：" + userId);
    }

    @Test
    public void TestGetById() throws Exception {
        Long userId = 810457443074049L;
        TpzUserEntity userEntity = this.userService.getById(userId);
        System.out.println("根据ID获取用户信息返回：" + ToStringBuilder.reflectionToString(userEntity));
    }

    @Test
    public void TestgetByCaimaoId() throws Exception {
        Long caimaoId = 56L;
        TpzUserEntity userEntity = this.userService.getByCaimaoId(caimaoId);
        Validator validator = ApplicationContextUtils.getBean("paramValidator");
        Set<ConstraintViolation<FUserLoginReq>> errors = validator.validate(new FUserLoginReq());
        for (ConstraintViolation<FUserLoginReq> error : errors) {
            System.out.println(error.getMessage());
        }
        System.out.println("根据财猫ID获取用户信息返回：" + ToStringBuilder.reflectionToString(userEntity));
    }

    @Test
    public void testDoLogin() throws Exception {
        String loginName = "1864521547";
        String loginPwd = "wx123456";
        String loginIp = "192.168.1.1";
        try {
            FUserLoginReq userLoginReq = new FUserLoginReq();
            userLoginReq.setLoginName(loginName);
            userLoginReq.setLoginPwd(loginPwd);
            userLoginReq.setLoginIP(loginIp);
            userLoginReq.setSource("1");
            Long userId = this.userService.doLogin(userLoginReq);
            System.out.println("登陸后用戶ID ：" + userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @Test
    public void testDoRegister() {
        String mobile = "18645215427";
        String loginPwd = "wangxu123";
        String smsCode = "888888";
        String registerIp = "192.168.1.1";
        Long refUserId = 799869644046338L;
        String userInit = EUserInit.WEB.getCode();

        FUserRegisterReq userRegisterReq = new FUserRegisterReq();
        userRegisterReq.setMobile(mobile);
        userRegisterReq.setUserPwd(loginPwd);
        userRegisterReq.setCheckCode(smsCode);
        userRegisterReq.setRegisterIp(registerIp);
        userRegisterReq.setRefUserId(null);
        userRegisterReq.setUserInit(userInit);

        try {
            this.userService.doRegister(userRegisterReq);
            System.out.println("注册成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void TestDoBindOrChangeEmail() throws Exception {
        FUserBindOrChangeEmailReq req = new FUserBindOrChangeEmailReq();
        req.setUserId(802184463646721L);
        req.setEmail("wangxu@huobi.com");
        req.setTradePwd("123456");

        this.userService.doBindOrChangeEmail(req);
    }

    @Test
    public void TestdoFindLoginPwd() throws Exception {
        FUserFindLoginPwdReq req = new FUserFindLoginPwdReq();
        req.setMobile("18612839815");
        req.setCheckCode("888888");
        req.setUserLoginPwd("123456");
        //req.setMobile("sd<script>dfa<html><sld></sd>dfa<alert>");
        this.userService.doFindLoginPwd(req);
    }

    @Test
    public void TestQueryLoginErrorCount() throws Exception {
//        Date nowDate = new Date();
//        Date searchDate = new Date(System.currentTimeMillis() - (5 * 60 * 60 * 1000));
//        System.out.println(searchDate);
//        System.out.println(nowDate);
//        System.out.println(searchDate.before(nowDate));


        Long userId = 799869644046338L;
        Integer res = this.userService.queryLoginErrorCount(userId, 5);
        System.out.println(res);
    }

    @Test
    public void TestResetLoginpwd() throws Exception {
        FUserResetLoginPwdReq userResetLoginPwdReq = new FUserResetLoginPwdReq();
        userResetLoginPwdReq.setUserId(810992132947969L);
        userResetLoginPwdReq.setNewPwd("wx123456");
        userResetLoginPwdReq.setOldPwd("123456");
        this.userService.resetLoginpwd(userResetLoginPwdReq);
    }


    @Test
    public void TestUserProhiWithdraw() throws Exception {
        // 设置用户的提现状态
        FUserProhiWithdrawReq userProhiWithdrawReq = new FUserProhiWithdrawReq();
        userProhiWithdrawReq.setUserId(802184463646721L);
        userProhiWithdrawReq.setType(EUserProhiWithdrawType.CUSTOM.getValue());
        userProhiWithdrawReq.setStatus(EUserProhiWithdrawStatus.YES.getValue());
        userProhiWithdrawReq.setMemo("我就是这么酷炫");
        this.userService.setUserWithdrawStatus(userProhiWithdrawReq);

        // 查询用户的提现状态变更历史
        FUserQueryProhiWithdrawLogReq req = new FUserQueryProhiWithdrawLogReq();
        req.setType(EUserProhiWithdrawType.CUSTOM.getValue());
        req = this.userService.queryUserWithdrawStatusLog(req);
        if (req.getItems().size() > 0) {
            for (FUserQueryProhiWithdrawLogRes entity : req.getItems()) {
                System.out.println(ToStringBuilder.reflectionToString(entity));
            }
        }
    }

    @Test
    public void doRefreshUserExtText() throws Exception {
        FUserRefreshUserExtReq userRefreshUserExtReq = new FUserRefreshUserExtReq();
        userRefreshUserExtReq.setUserId(809250154610689L);
        userRefreshUserExtReq.setNickName("XXX");

        this.userService.doRefreshUserExt(userRefreshUserExtReq);
    }


    @Test
    public void updateCardPathTest() throws Exception {
        userService.updateCardPath(802184463646721L, "122", "234");
    }

}

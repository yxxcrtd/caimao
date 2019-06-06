/**
 * @Title DictionaryController.java
 * @Package com.hsnet.pz.controller.other
 * @Description
 * @author miyb
 * @date 2014-12-22 下午1:14:40 
 * @version V1.0
 */
package com.hsnet.pz.controller.other;

import com.caimao.bana.api.entity.TpzUserBankCardEntity;
import com.caimao.bana.api.entity.res.product.FProductDetailRes;
import com.caimao.bana.api.entity.res.product.FProductRes;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IUserBankCardService;
import com.caimao.bana.api.service.product.IProductService;
import com.hsnet.common.utils.DateUtil;
import com.hsnet.pz.ao.infrastructure.IBasicDataAO;
import com.hsnet.pz.biz.account.dto.res.F830101Res;
import com.hsnet.pz.biz.base.dto.req.F830902Req;
import com.hsnet.pz.biz.base.dto.res.*;
import com.hsnet.pz.biz.base.entity.PwdQuestion;
import com.hsnet.pz.biz.user.dto.req.F830014Req;
import com.hsnet.pz.biz.user.dto.res.F830016Res;
import com.hsnet.pz.biz.user.dto.res.F830017Res;
import com.hsnet.pz.biz.user.dto.res.F830018Res;
import com.hsnet.pz.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: miyb
 * @since: 2014-12-22 下午1:14:40
 * @history:
 */
@Controller
public class BasicDataController extends BaseController {
    @Autowired
    IBasicDataAO basicDataAO;
    @Resource
    IProductService productService;
    @Resource
    private IUserBankCardService userBankCardService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public F830018Res getUserDetail() {
        return getUser();
    }

    @RequestMapping(value = "/userextra", method = RequestMethod.GET)
    @ResponseBody
    public F830016Res getUserExtra() {
        return basicDataAO.getUserExtra(this.getSessionUser().getUser_id());
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    @ResponseBody
    public F830101Res getRZAccount() {
        return basicDataAO.getPZAccount(this.getSessionUser().getUser_id());
    }

    @RequestMapping(value = "/top/bar", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getTopBar() {
        Map<String, Object> map = new HashMap<String, Object>();
        F830018Res user = getUser();
        // map.put("userPhoto", user.getPhoto());
        if (StringUtils.isBlank(user.getUserNickName())) {
            map.put("userName", user.getUserName());
        } else {
            map.put("userName", user.getUserNickName());
        }
        map.put("messageCount",
                basicDataAO.getUnreadMessageCount(user.getUserId()));
        return map;
    }

    @RequestMapping(value = "/home/user", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> gethomeUser() {
        Map<String, Object> map = new HashMap<String, Object>();
        F830018Res user = getUser();
        map.put("user", user);
        int sl = basicDataAO.getAccountSecurityLevel(user);
        map.put("accountSecurityLevel", sl);
        return map;
    }

    @RequestMapping(value = "/home/richhbi", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> queryRichHomsCombineId() {
        return basicDataAO.queryRichHomsCombineId(this.getSessionUser()
                .getUser_id());
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    @ResponseBody
    public FProductRes getProduct(@RequestParam("product_id") Long productId) throws CustomerException {
        return productService.getProduct(productId);
    }

    @RequestMapping(value = "/proddetail", method = RequestMethod.GET)
    @ResponseBody
    public List<FProductDetailRes> queryProdDetailList(
            @RequestParam("product_id") Long productId) throws CustomerException {
        return productService.queryProdDetailList(productId);
    }
    
//    @RequestMapping(value = "/product", method = RequestMethod.GET)
//    @ResponseBody
//    public F830911Res getProduct(@RequestParam("product_id") Long productId) {
//        return basicDataAO.getProduct(productId);
//    }
//
//    @RequestMapping(value = "/proddetail", method = RequestMethod.GET)
//    @ResponseBody
//    public List<F830912Res> queryProdDetailList(
//            @RequestParam("product_id") Long productId) {
//        return basicDataAO.queryProdDetailList(productId);
//    }

    @RequestMapping(value = "/dict", method = RequestMethod.GET)
    @ResponseBody
    public List<F830901Res> queryDictionary(@RequestParam("code") String code) {
        return basicDataAO.querySysDict(code);
    }

    @RequestMapping(value = "/bank", method = RequestMethod.GET)
    @ResponseBody
    public List<F830908Res> getBankList(
            @RequestParam("pay_company_no") Long payCompanyNo) {
        return basicDataAO.queryBankTypeList(payCompanyNo);
    }

//    @RequestMapping(value = "/bankcard", method = RequestMethod.GET)
//    @ResponseBody
//    public List<F830404Res> queryActiveBankCardList() {
//        return basicDataAO.queryActiveBankCardList(this.getSessionUser()
//                .getUser_id());
//    }

    @RequestMapping(value = "/bankcard", method = RequestMethod.GET)
    @ResponseBody
    public List<TpzUserBankCardEntity> queryActiveBankCardList() {
        return userBankCardService.queryUserBankList(this.getSessionUser()
                .getUser_id(), "1");
    }

    @RequestMapping(value = "/pwdquestion", method = RequestMethod.GET)
    @ResponseBody
    public List<PwdQuestion> queryPwdQuestionList() {
        return basicDataAO.queryPwdQuestionList();
    }

    @RequestMapping(value = "/userpwdquestion", method = RequestMethod.GET)
    @ResponseBody
    public F830014Req queryUserPwdQuestion() {
        return basicDataAO.queryUserPwdQuestion(getSessionUser().getUser_id());
    }

    @RequestMapping(value = "/tradepwd", method = RequestMethod.GET)
    @ResponseBody
    public F830017Res getTradePwd() {
        return basicDataAO.doGetTradePwd(getSessionUser().getUser_id());
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.GET)
    @ResponseBody
    public Long getDeposit(@RequestParam("contract_no") Long contractNo,
                           @RequestParam("loan_apply_action") String loanApplyAction) {
        return basicDataAO.getDeposit(getSessionUser().getUser_id(),
                loanApplyAction, contractNo);
    }

    @RequestMapping(value = "/contest/info", method = RequestMethod.GET)
    @ResponseBody
    public List<F830913Res> getContestInfo(@RequestParam("state") String state) {
        return basicDataAO.getFirmContest(state);
    }

    @RequestMapping(value = "/contest/reward", method = RequestMethod.GET)
    @ResponseBody
    public List<F830914Res> queryContestReward(
            @RequestParam("contest_id") Long contestId) {
        return basicDataAO.queryFirmContestReward(contestId);
    }

    @RequestMapping(value = "/contest/result", method = RequestMethod.GET)
    @ResponseBody
    public Map<Object, Object> queryContestResult(
            @RequestParam("product_id") Long productId) {
        return basicDataAO.queryContestsResultPage(productId);
    }

    @RequestMapping(value = "/time", method = RequestMethod.GET)
    @ResponseBody
    public String getTime() {
        return DateUtil.getDateTime(DateUtil.DATA_TIME_PATTERN_1, new Date());
    }

    /* 获取用户等级制度（利息折扣） */
    @RequestMapping(value = "/grades", method = RequestMethod.GET)
    @ResponseBody
    public List<F830915Res> queryGrades() {
        return basicDataAO.queryGrades();
    }

    /* 获取用户积分兑换规则 */
    @RequestMapping(value = "/score/rule", method = RequestMethod.GET)
    @ResponseBody
    public F830902Req querySysParameter() {
        return basicDataAO.querySysParameter("exchangeruleid");
    }
}

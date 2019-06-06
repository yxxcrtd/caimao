package com.caimao.bana.controller;

import com.caimao.bana.api.entity.*;
import com.caimao.bana.api.entity.res.FIndexPZRankingRes;
import com.caimao.bana.api.entity.res.FIndexRealtimePZRes;
import com.caimao.bana.api.entity.res.other.FHomsNeedUpdateAssetsRes;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountAssetsEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountHoldEntity;
import com.caimao.bana.api.service.IHomsAccountService;
import com.caimao.bana.api.service.IOtherDataService;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.SysParameterService;
import com.hsnet.pz.ao.account.IHomsAccountAO;
import com.hsnet.pz.ao.financing.IFinancingAO;
import com.hsnet.pz.ao.stock.IStockAO;
import com.hsnet.pz.ao.util.RSAUtils;
import com.hsnet.pz.biz.homs.dto.res.F830309Res;
import com.hsnet.pz.biz.homs.dto.res.F830312Res;
import com.hsnet.pz.biz.loan.service.IRepayService;
import com.hsnet.pz.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 其他相关的数据查询
 * Created by WangXu on 2015/5/28.
 */
@Controller
@RequestMapping(value = "/other/data")
public class OtherDataController {
    private static final Logger logger = LoggerFactory.getLogger(OtherDataController.class);

    @Resource
    private IOtherDataService otherDataService;
    @Resource
    private SysParameterService sysParameterService;
    @Resource
    private IHomsAccountService homsAccountService;
    @Resource
    private IHomsAccountAO homsAccountAO;
    @Resource
    private IUserService userService;
    @Autowired
    private IStockAO stockAO;
    @Autowired
    IRepayService repayService;

    // 首页的融资排行
    @RequestMapping(value = "/pz_ranking", method = RequestMethod.GET)
    @ResponseBody
    public List<FIndexPZRankingRes> indexPzRanking() throws Exception {
        return this.otherDataService.indexPzRankingList(5);
    }

    // 首页的融资动态
    @RequestMapping(value = "/pz_realtime")
    @ResponseBody
    public List<FIndexRealtimePZRes> indexRealtimePZ() throws Exception {
        return this.otherDataService.indexRealtimePZList(5);
    }

    // 返回后台系统参数设置的一个时间，判断是否为新注册用户，是否能够进行融资
    @RequestMapping(value = "/o20150715")
    @ResponseBody
    public long newRegisterDatetime() {
        TsysParameterEntity entity = this.sysParameterService.getSysparameterById("new_register_datetime");
        if (entity == null) {
            return -1;
        }
        Date date = DateUtil.convertStringToDate(DateUtil.DATA_TIME_PATTERN_1, entity.getParamValue());
        return date.getTime();
    }

    // 更新HOMS账户资产的方法
    @RequestMapping(value = "/homs_account_update")
    @ResponseBody
    public Map<String, Object> homsAccountUpdate() throws Exception {
        Map<String, Object> map = new HashMap<>();
        String updateDate =  DateUtil.convertDateToString(DateUtil.DISPLAY_DATE_FORMAT_STRING, new Date());
        // 获取需要更新的账户信息
        List<FHomsNeedUpdateAssetsRes> accountList = this.homsAccountService.queryNeedUpdateAssetsList(updateDate);
        // 记录一共多少条
        map.put("size", accountList.size());
        if (accountList.size() > 0) {
            for (FHomsNeedUpdateAssetsRes res : accountList) {
                try {
                    // 获取HOMS账户的资产信息
                    F830312Res assets = homsAccountAO.getHomsAssetsInfo(res.getUserId(), res.getHomsFundAccount(), res.getHomsCombineId());
                    if (assets != null) {
                        // 获取用户信息
                        TpzUserEntity user = this.userService.getById(res.getUserId());
                        // 写入数据库信息
                        ZeusHomsAccountAssetsEntity entity = new ZeusHomsAccountAssetsEntity();
                        entity.setUserId(user.getUserId());
                        entity.setUserName(user.getUserRealName());
                        entity.setMobile(user.getMobile());
                        entity.setContractNo(res.getContractNo());
                        entity.setHomsCombineId(res.getHomsCombineId());
                        entity.setHomsFundAccount(res.getHomsFundAccount());
                        entity.setBeginAmount(assets.getBeginAmount());
                        entity.setCurAmount(assets.getCurAmount());
                        entity.setCurrentCash(assets.getCurrentCash());
                        entity.setEnableRatio(assets.getEnableRatio());
                        entity.setEnableWithdraw(assets.getEnableWithdraw());
                        entity.setExposureRatio(assets.getExposureRatio());
                        entity.setLoanAmount(assets.getLoanAmount());
                        entity.setTotalAsset(assets.getTotalAsset());
                        entity.setTotalMarketValue(assets.getTotalMarketValue());
                        entity.setTotalNetAssets(assets.getTotalNetAssets());
                        entity.setTotalProfit(assets.getTotalProfit());
                        entity.setUpdateDate(updateDate);
                        this.homsAccountService.saveZeusHomsAssets(entity);
                    }
                } catch (Exception e) {
                    logger.error("更新资产错误，异常信息为{}", e);
                    //map.put("error", e.getMessage());
                }
            }
        }
        return map;
    }

    // 更新HOMS持仓
    @RequestMapping(value = "/homs_account_hold_update")
    @ResponseBody
    public Map<String, Object> homsAccountHoldUpdate() throws Exception {
        Map<String, Object> map = new HashMap<>();
        String updateDate =  DateUtil.convertDateToString(DateUtil.DISPLAY_DATE_FORMAT_STRING, new Date());
        List<TpzHomsAccountEntity> tpzHomsAccountEntityList = homsAccountService.queryHomsAccount();
        //查询列表
        List<String> queryUpdated = homsAccountService.queryUpdated(updateDate);

        if(tpzHomsAccountEntityList != null){
            map.put("homsAccountList", tpzHomsAccountEntityList.size());
            for(TpzHomsAccountEntity tpzHomsAccountEntity:tpzHomsAccountEntityList){
                List<F830309Res> holdList = stockAO.queryFatherHolding(800177807949826L, tpzHomsAccountEntity.getHomsFundAccount());
                if(holdList != null){
                    map.put("holdList" + tpzHomsAccountEntity.getHomsFundAccount(), holdList.size());
                    for (F830309Res holdDetail:holdList){
                        try{
                            String queryString = holdDetail.getHomsFundAccount() + holdDetail.getHomsCombineId() + holdDetail.getStockCode();
                            if(queryUpdated.contains(queryString)){
                                continue;
                            }
                            TpzHomsAccountLoanEntity tpzHomsAccountLoanEntity = homsAccountService.queryTpzHomsAccountLoan(holdDetail.getHomsFundAccount(), holdDetail.getHomsCombineId());

                            ZeusHomsAccountHoldEntity zeusHomsAccountHoldEntity = new ZeusHomsAccountHoldEntity();
                            if(tpzHomsAccountLoanEntity != null){
                                if(tpzHomsAccountLoanEntity.getUserId() != 0){
                                    TpzUserEntity user = this.userService.getById(tpzHomsAccountLoanEntity.getUserId());
                                    zeusHomsAccountHoldEntity.setUserId(user.getUserId());
                                    zeusHomsAccountHoldEntity.setRealName(user.getUserRealName());
                                    zeusHomsAccountHoldEntity.setMobile(user.getMobile());
                                }
                            }
                            zeusHomsAccountHoldEntity.setHomsFundAccount(holdDetail.getHomsFundAccount());
                            zeusHomsAccountHoldEntity.setHomsCombineId(holdDetail.getHomsCombineId());
                            zeusHomsAccountHoldEntity.setExchangeType(new Byte(holdDetail.getExchangeType()));
                            zeusHomsAccountHoldEntity.setStockCode(holdDetail.getStockCode());
                            zeusHomsAccountHoldEntity.setStockName(holdDetail.getStockName());
                            zeusHomsAccountHoldEntity.setCurrentAmount(holdDetail.getCurrentAmount().toString());
                            zeusHomsAccountHoldEntity.setEnableAmount(holdDetail.getEnableAmount().toString());
                            zeusHomsAccountHoldEntity.setCostBalance(new BigDecimal(holdDetail.getCostBalance()).setScale(0, BigDecimal.ROUND_DOWN).toString());
                            zeusHomsAccountHoldEntity.setMarketValue(new BigDecimal(holdDetail.getMarketValue()).setScale(0,BigDecimal.ROUND_DOWN).toString());
                            zeusHomsAccountHoldEntity.setBuyAmount(holdDetail.getBuyAmount().toString());
                            zeusHomsAccountHoldEntity.setSellAmount(holdDetail.getSellAmount().toString());
                            zeusHomsAccountHoldEntity.setUpdated(updateDate);
                            homsAccountService.saveZeusHomsAccountHold(zeusHomsAccountHoldEntity);
                        }catch(Exception e){
                            logger.error("更新持仓错误，异常信息为{}", e);
                        }
                    }
                }
            }
        }
        return map;
    }
}

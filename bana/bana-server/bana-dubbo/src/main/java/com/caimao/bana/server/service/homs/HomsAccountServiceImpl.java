package com.caimao.bana.server.service.homs;

import com.caimao.bana.api.entity.TpzHomsAccountEntity;
import com.caimao.bana.api.entity.TpzHomsAccountJourEntity;
import com.caimao.bana.api.entity.TpzHomsAccountLoanEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.req.FZeusHomsAccountAssetsReq;
import com.caimao.bana.api.entity.req.FZeusHomsAccountHoldReq;
import com.caimao.bana.api.entity.req.user.FUserProhiWithdrawReq;
import com.caimao.bana.api.entity.res.other.FHomsNeedUpdateAssetsRes;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountAssetsEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountHoldEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsRepaymentExcludeEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.EUserProhiWithdrawStatus;
import com.caimao.bana.api.enums.EUserProhiWithdrawType;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IHomsAccountService;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.homs.*;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.service.OperationServiceImpl;
import com.caimao.bana.server.service.user.UserServiceImpl;
import com.caimao.bana.server.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Homs 账户相关的接口方法
 * Created by WangXu on 2015/6/12.
 */
@Service("homsAccountService")
public class HomsAccountServiceImpl implements IHomsAccountService {

    @Autowired
    private TpzUserDao userDao;
    @Autowired
    private TpzHomsAccountLoanDao homsAccountLoanDao;
    @Autowired
    private ZeusHomsAccountAssetsDao zeusHomsAccountAssetsDao;
    @Autowired
    private TpzHomsAccountDao tpzHomsAccountDao;
    @Autowired
    private ZeusHomsAccountHoldDao zeusHomsAccountHoldDao;
    @Autowired
    private ZeusHomsRepaymentExcludeDao zeusHomsRepaymentExcludeDao;
    @Resource
    private TpzAccountDao tpzAccountDao;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private OperationServiceImpl operationService;
    @Resource
    RedisUtils redisUtils;

    // 验证HOMS账户是不是指定用户的方法
    @Override
    public void valideUserHomsAccount(Long userId, String homsCombineId, String homsFundAccount) throws Exception {
        TpzHomsAccountLoanEntity entity = new TpzHomsAccountLoanEntity();
        entity.setUserId(userId);
        entity.setHomsCombineId(homsCombineId);
        entity.setHomsFundAccount(homsFundAccount);
        List<TpzHomsAccountLoanEntity> accountLoanEntityList = this.homsAccountLoanDao.getHomsAccount(entity);
        if (accountLoanEntityList.size() <= 0) {
            throw new CustomerException("账户错误", 888888);
        }
    }
    //搜索使用的根据姓名或手机号模糊搜索
    @Override
    public FZeusHomsAccountAssetsReq searchZeusHomsAssetsList(FZeusHomsAccountAssetsReq req) throws Exception {
        req.setItems(this.zeusHomsAccountAssetsDao.searchUserHomsAssetsListWithPage(req));
        return req;
    }
    //保存homs操盘账户的资产信息
    @Override
    public int saveZeusHomsAssets(ZeusHomsAccountAssetsEntity entity) throws Exception {
        return this.zeusHomsAccountAssetsDao.save(entity);
    }
    //根据条件查找记录
    @Override
    public ZeusHomsAccountAssetsEntity getZeusHomsAssets(ZeusHomsAccountAssetsEntity entity) throws Exception {
        return this.zeusHomsAccountAssetsDao.getHomsAssets(entity);
    }
    //获取指定日期需要更新的资产的记录
    @Override
    public List<FHomsNeedUpdateAssetsRes> queryNeedUpdateAssetsList(String updateDate) throws Exception {
        return this.zeusHomsAccountAssetsDao.queryNeedUpdateAssetsList(updateDate);
    }

    //获取所有homs主账户
    @Override
    public List<TpzHomsAccountEntity> queryHomsAccount() throws Exception {
        return this.tpzHomsAccountDao.queryHomsAccount();
    }

    //保存用户持仓信息
    @Override
    public Integer saveZeusHomsAccountHold(ZeusHomsAccountHoldEntity zeusHomsAccountHoldEntity) throws Exception {
        return this.zeusHomsAccountHoldDao.saveZeusHomsAccountHold(zeusHomsAccountHoldEntity);
    }

    //获取持仓列表
    @Override
    public FZeusHomsAccountHoldReq searchZeusHomsHoldList(FZeusHomsAccountHoldReq req) throws Exception{
        req.setItems(this.zeusHomsAccountHoldDao.searchHomsHoldListWithPage(req));
        return req;
    }

    //获取持仓列表
    @Override
    public TpzHomsAccountLoanEntity queryTpzHomsAccountLoan(String homsFundAccount, String homsCombineId) throws Exception{
        return this.homsAccountLoanDao.queryTpzHomsAccountLoan(homsFundAccount, homsCombineId);
    }

    //获取已更新列表
    @Override
    public List<String> queryUpdated(String dateString) throws Exception {
        List<String> result = new ArrayList<>();
        List<HashMap<String, String>> queryList = this.zeusHomsAccountHoldDao.queryUpdated(dateString);
        if(queryList != null){
            for (HashMap<String, String> queryDetail:queryList){
                result.add(queryDetail.get("homsFundAccount") + queryDetail.get("homsCombineId") + queryDetail.get("stockCode"));
            }
        }
        return result;
    }

    // 获取指定的账户的排除列表（如果都为null，则查询所有的）
    @Override
    public List<ZeusHomsRepaymentExcludeEntity> queryHomsRepaymentExcludeList(String homsFundAccount, String homsCombineId) throws Exception {
        return this.zeusHomsRepaymentExcludeDao.queryList(homsFundAccount, homsCombineId);
    }

    // 保存有持仓能够还款的排除列表
    @Override
    public Integer saveHomsRepaymentExclude(String homsCombineId) throws Exception {
        TpzHomsAccountLoanEntity loanEntity = this.homsAccountLoanDao.getByCombineId(homsCombineId);
        if (loanEntity == null) {
            throw new CustomerException("未找到HOMS子账户信息", 888888);
        }
        TpzUserEntity userEntity = this.userDao.getUserById(loanEntity.getUserId());
        if (userEntity == null) {
            throw new CustomerException("为找到用户信息", 888888);
        }
        ZeusHomsRepaymentExcludeEntity excludeEntity = new ZeusHomsRepaymentExcludeEntity();
        excludeEntity.setUserId(userEntity.getUserId());
        excludeEntity.setUserRealName(userEntity.getUserRealName());
        excludeEntity.setMobile(userEntity.getMobile());
        excludeEntity.setHomsFundAccount(loanEntity.getHomsFundAccount());
        excludeEntity.setHomsCombineId(loanEntity.getHomsCombineId());
        excludeEntity.setCreated(new Date());
        return this.zeusHomsRepaymentExcludeDao.save(excludeEntity);
    }

    // 删除有持仓能够还款的排除列表
    @Override
    public void deleteHomsRepayMentExclude(String homsFundAccount, String homsCombineId) throws Exception {
        this.zeusHomsRepaymentExcludeDao.delete(homsFundAccount, homsCombineId);
    }

    //查询异常的还款记录
    @Override
    public void checkAbnormalRepayAccountJour() throws Exception{
        // 查询 3 分钟内的，扫描的是每 1 分钟扫描一次
        Date dateStart = new Date(System.currentTimeMillis() - 180 * 1000);
        List<TpzHomsAccountJourEntity> abnormalList = tpzHomsAccountDao.queryAbnormalAccountJour(dateStart);
        if(abnormalList != null){
            for (TpzHomsAccountJourEntity tpzHomsAccountJourEntity:abnormalList){
                TpzUserEntity userEntity = this.userDao.getById(tpzHomsAccountJourEntity.getUserId());
                //获取redis中相应的最后的记录
                Object lastPushIdRedis = redisUtils.hGet(0, "abnormalAccountHomsJour", tpzHomsAccountJourEntity.getUserId().toString());
                // 禁止用户提现
                FUserProhiWithdrawReq prohiWithdrawReq = new FUserProhiWithdrawReq();
                prohiWithdrawReq.setUserId(tpzHomsAccountJourEntity.getUserId());
                prohiWithdrawReq.setType(EUserProhiWithdrawType.HOMS_ERROR.getCode());
                prohiWithdrawReq.setStatus(EUserProhiWithdrawStatus.YES.getCode());
                prohiWithdrawReq.setMemo("HOMS流水错误");

                // 发送报警信息
                EAccountBizType eAccountBizType = EAccountBizType.findByCode(tpzHomsAccountJourEntity.getAccountBizType());
                String alarmKey = "homs_error";
                String subject = userEntity.getUserRealName() + " HOMS流水异常,操作账户:"+tpzHomsAccountJourEntity.getHomsCombineId();
                String content = String.format("%s 用户HOMS流水异常，交易金额 %s 交易时间 %s 交易类型 %s, 操作账户 %s , 错误的流水号：%s",
                        userEntity.getUserRealName(), tpzHomsAccountJourEntity.getTransAmount() / 100, tpzHomsAccountJourEntity.getTransDatetime(), eAccountBizType == null ? "未知" : eAccountBizType.getValue(), tpzHomsAccountJourEntity.getHomsCombineId(), tpzHomsAccountJourEntity.getRefSerialNo());
                if(lastPushIdRedis == null){
                    this.userService.setUserWithdrawStatus(prohiWithdrawReq);
                    try {
                        this.operationService.addAlarmTask(alarmKey, subject, content);
                    } catch (Exception e) {}
                    //tpzAccountDao.lockingAccount(tpzHomsAccountJourEntity.getPzAccountId());
                }else{
                    Long lastPushId = Long.parseLong(lastPushIdRedis.toString());
                    if(lastPushId.longValue() != tpzHomsAccountJourEntity.getId().longValue()){
                        this.userService.setUserWithdrawStatus(prohiWithdrawReq);
                        try {
                            this.operationService.addAlarmTask(alarmKey, subject, content);
                        } catch (Exception e) {}
                        //tpzAccountDao.lockingAccount(tpzHomsAccountJourEntity.getPzAccountId());
                    }
                }
                redisUtils.hSet(0, "abnormalAccountHomsJour", tpzHomsAccountJourEntity.getUserId().toString(), tpzHomsAccountJourEntity.getId().toString());
            }
        }
    }
}

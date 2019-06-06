package com.caimao.bana.server.service.activity;

import com.alibaba.druid.support.json.JSONUtils;
import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.TpzLoanApplyEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.activity.BanaActivityRecordEntity;
import com.caimao.bana.api.entity.activity.BanaActivityWxPaokuEntity;
import com.caimao.bana.api.entity.activity.BanaLoseRPEntity;
import com.caimao.bana.api.entity.activity.BanaLoseRPRecordEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.EActId;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IActivityService;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.server.dao.LoanApplyDao;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.activity.BanaActivityRecordDao;
import com.caimao.bana.server.dao.activity.BanaActivityWxPaokuDao;
import com.caimao.bana.server.dao.activity.BanaLoseRPDao;
import com.caimao.bana.server.utils.AccountManager;
import com.caimao.bana.server.utils.DateUtil;
import com.huobi.commons.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 活动数据
 * Created by WangXu on 2015/5/22.
 */
@Service("activityService")
public class ActivityServiceImpl implements IActivityService {

    @Autowired
    private BanaActivityWxPaokuDao activityWxPaokuDao;
    @Resource
    private LoanApplyDao loanApplyDao;
    @Resource
    private BanaLoseRPDao banaLoseRPDao;
    @Resource
    private AccountManager accountManager;
    @Resource
    private TpzAccountDao accountDao;
    @Resource
    private BanaActivityRecordDao banaActivityRecordDao;
    @Resource
    private IUserService userService;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public int saveWeixinPaoKu(String phone, String pzValue, String userIP) {
        Date curDate = new Date();
        // 先获取手机号是不是已经有了
        BanaActivityWxPaokuEntity activityWxPaokuEntity = this.activityWxPaokuDao.getByPhone(phone);
        if (activityWxPaokuEntity != null) {
            return 0;
        }
        activityWxPaokuEntity = new BanaActivityWxPaokuEntity();
        activityWxPaokuEntity.setPhone(phone);
        activityWxPaokuEntity.setPzValue(pzValue);
        activityWxPaokuEntity.setIp(userIP);
        activityWxPaokuEntity.setCreated(curDate);
        this.activityWxPaokuDao.save(activityWxPaokuEntity);
        return 1;
    }

    @Override
    public Boolean getShowRedPackage(Long userId) throws Exception {
        try{
            if(System.currentTimeMillis() > 1436544000000L) return false;
            //检测是否可以抽取
            TpzLoanApplyEntity tpzLoanApplyEntity = loanApplyDao.getUserHasLoan(userId, "2015-07-07 20:00:00");
            if(tpzLoanApplyEntity == null) return false;

            //检测是否已经抽取
            BanaLoseRPRecordEntity banaLoseRPRecordEntity = banaLoseRPDao.getRecordByUserId(userId);
            return banaLoseRPRecordEntity == null;
        }catch(Exception e){
            throw new Exception("系统繁忙");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Long openRedPackage(Long userId) throws Exception {
        Long money = null;
        //检测是否可以抽取
        if(System.currentTimeMillis() > 1436544000000L) throw new Exception("活动已经结束");
        TpzLoanApplyEntity tpzLoanApplyEntity = loanApplyDao.getUserHasLoan(userId, "2015-07-07 20:00:00");
        if(tpzLoanApplyEntity == null) throw new Exception("您不具备抽取资格");

        //检测是否已经抽取
        BanaLoseRPRecordEntity banaLoseRPRecordEntity = banaLoseRPDao.getRecordByUserId(userId);
        if(banaLoseRPRecordEntity != null) throw new Exception("您已经抽取过红包，请勿重复抽取");

        //获取红包数据
        BanaLoseRPEntity banaLoseRPEntity = banaLoseRPDao.getLoseRpDataForUpdate(1L);
        if(banaLoseRPEntity == null) throw new Exception("系统错误");
        String rpData = banaLoseRPEntity.getRpData();

        String packageData = "";
        if(rpData != null && !"".equals(rpData)){
            List packageDataGet = JsonUtil.toObject(rpData, List.class);
            if(packageDataGet != null){
                money = Long.parseLong(packageDataGet.get(0).toString());
                packageDataGet.remove(0);
                packageData = JSONUtils.toJSONString(packageDataGet);
            }
        }

        //更新红包数据
        Integer affectRow = banaLoseRPDao.updateBanaLoseRP(1L, packageData);
        if(affectRow != 1) throw new Exception("系统繁忙，请重新抽取");

        //插入红包数据
        if(money == null || money == 0) money = 8L;
        BanaLoseRPRecordEntity insertData = new BanaLoseRPRecordEntity();
        insertData.setUserId(userId);
        insertData.setMoney(money);
        insertData.setCreated(new Date());
        banaLoseRPDao.insertRecord(insertData);

        Long id = insertData.getId();
        if(id == null || id == 0) throw new Exception("系统繁忙，请重新抽取");

        // 查询用户的资产记录，并锁住它
        TpzAccountEntity accountEntity = this.accountDao.getAccountByUserId(userId);
        if(accountEntity == null) throw new Exception("未完成实名认证");
        try{
            this.accountManager.doUpdateAvaiAmount(id.toString(), accountEntity.getPzAccountId(), money * 100, EAccountBizType.OLD_USER_RED_PACKAGE.getCode(), ESeqFlag.COME.getCode());
        }catch(Exception e){
            throw new Exception("系统繁忙001");
        }

        return money;
    }

    // 获取用户参加的活动记录
    @Override
    public List<BanaActivityRecordEntity> getUserActivityRecord(Long userId, Integer actId) throws Exception {
        return this.banaActivityRecordDao.getUserActivityRecordList(userId, actId);
    }

    // 添加用户的活动记录
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void addActivityRecord(BanaActivityRecordEntity entity) throws Exception {
        // 添加记录
        this.banaActivityRecordDao.save(entity);
        TpzAccountEntity accountEntity = this.accountDao.getAccountByUserId(entity.getUserId());
        if (accountEntity == null) {
            throw new CustomerException("获取用户资产失败", 888888);
        }
        // 变更资产
        this.accountManager.doUpdateAvaiAmount(
                entity.getId().toString(),
                accountEntity.getPzAccountId(),
                entity.getAmount(), EAccountBizType.ACTIVITY_RED_PACKAGE.getCode(), ESeqFlag.COME.getCode());
    }

    // 二维码活动的脚本任务
    @Override
    public void jobsEWMActivity() throws Exception {
        // 获取用户邀请的人
        Long userId = 83L;
        Date date = DateUtil.convertStringToDate(DateUtil.DATA_TIME_PATTERN_1, "2015-07-31 17:00:00");
        List<TpzUserEntity> userList = this.userService.getUserRefList(userId, date);
        if (userList != null) {
            for (TpzUserEntity user : userList) {
                try {
                    // 获取用户是否有资金账户
                    TpzAccountEntity accountEntity = this.accountDao.getAccountByUserId(user.getUserId());
                    if (accountEntity == null) {
                        continue;
                    }
                    // 查询是否有发过了
                    List<BanaActivityRecordEntity> recordEntityList = this.getUserActivityRecord(user.getUserId(), EActId.EWM100.getCode());
                    if (recordEntityList.size() > 0) {
                        continue;
                    }
                    // 进行发钱
                    BanaActivityRecordEntity activityRecordEntity = new BanaActivityRecordEntity();
                    activityRecordEntity.setActId(EActId.EWM100.getCode());
                    activityRecordEntity.setUserId(user.getUserId());
                    activityRecordEntity.setAmount(10000L);
                    activityRecordEntity.setDatetime(new Date());
                    this.addActivityRecord(activityRecordEntity);
                } catch (Exception e) {

                }
            }
        }
    }
}

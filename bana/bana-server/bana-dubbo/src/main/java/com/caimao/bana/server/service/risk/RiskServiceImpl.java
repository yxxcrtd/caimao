package com.caimao.bana.server.service.risk;

import com.caimao.bana.api.entity.RiskRecordEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.TpzUserExtEntity;
import com.caimao.bana.api.entity.content.TpzPushMsgEntity;
import com.caimao.bana.api.entity.req.user.FUserProhiWithdrawReq;
import com.caimao.bana.api.entity.req.user.FUserQueryProhiWithdrawLogReq;
import com.caimao.bana.api.enums.EUserProhiWithdrawStatus;
import com.caimao.bana.api.enums.EUserProhiWithdrawType;
import com.caimao.bana.api.enums.RiskMsgSendType;
import com.caimao.bana.api.service.IRiskService;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.server.dao.message.TpzPushMsgDao;
import com.caimao.bana.server.dao.risk.RiskRecordDao;
import com.caimao.bana.server.utils.RedisUtils;
import com.caimao.bana.server.utils.SmsClientCaimaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 风控服务
 */
@Service("riskService")
public class RiskServiceImpl implements IRiskService {
    private static final Logger logger = LoggerFactory.getLogger(RiskServiceImpl.class);

    @Resource
    private TpzPushMsgDao tpzPushMsgDao;
    @Resource
    private RiskRecordDao riskRecordDao;
    @Resource
    private IUserService iUserService;
    @Resource
    RedisUtils redisUtils;

    @Override
    public void doSendRiskSms() throws Exception {
        //获取今日有风险短信的用户
        List<HashMap> todayPushRiskMsgList = tpzPushMsgDao.getTodayPushRiskMsg();
        if(todayPushRiskMsgList != null){
            for (HashMap riskUser:todayPushRiskMsgList){
                try{
                    Long userId = Long.parseLong(riskUser.get("push_user_id").toString());

                    //禁止提现部分
                    TpzUserExtEntity userExtEntity = this.iUserService.getUserExtById(userId);
                    // 先构建对象
                    FUserProhiWithdrawReq req = new FUserProhiWithdrawReq();
                    req.setUserId(userId);
                    req.setType(EUserProhiWithdrawType.POLICE_LINE.getCode());
                    req.setStatus(EUserProhiWithdrawStatus.YES.getCode());
                    req.setMemo("到达警戒线禁止提现");

                    //获取最后一条记录
                    TpzPushMsgEntity tpzPushMsgEntity = tpzPushMsgDao.getLastRiskMsg(userId);
                    Long lastId = tpzPushMsgEntity.getPushMsgId();
                    //获取redis中相应的最后的记录
                    Object lastPushIdRedis = redisUtils.hGet(0, "userLastMsg", userId.toString());
                    if(lastPushIdRedis == null){
                        if (userExtEntity.getProhiWithdraw() == 0) {
                            this.iUserService.setUserWithdrawStatus(req);
                        }
                    }else{
                        Long lastPushId = Long.parseLong(lastPushIdRedis.toString());
                        if(lastPushId.longValue() != lastId.longValue()){
                            if (userExtEntity.getProhiWithdraw() == 0) {
                                this.iUserService.setUserWithdrawStatus(req);
                            }
                        }
                    }
                    redisUtils.hSet(0, "userLastMsg", userId.toString(), lastId.toString());


                    //查找最近一条发送短信的日期
                    RiskRecordEntity lastSendMsg = riskRecordDao.getUserLastSendMsg(userId, RiskMsgSendType.RISK);
                    //发送爆仓短信
                    if(lastSendMsg == null || lastSendMsg.getCreated().getTime() - System.currentTimeMillis() > 43200000L){
                        String Msg = "您的账户已达到预警值，随时可能爆仓，请及时追加保证金。如发生强行平仓，请及时在财猫页面手动还款，以免多扣利息。";
                        //查询用户mobile
                        TpzUserEntity tpzUserEntity = iUserService.getById(userId);
                        if(tpzUserEntity != null){
                            //发送短信
                            SmsClientCaimaoUtil.postHttp(tpzUserEntity.getMobile(), Msg);
                            //插入发送记录
                            this.insertRiskRecord(userId, Msg, RiskMsgSendType.RISK);
                            Thread.sleep(1000);
                        }
                    }
                }catch(Exception e){
                    Thread.sleep(1000);
                    logger.error("发送警戒短信失败,{}", e);
                }
            }
        }
    }

    @Override
    public Integer insertRiskRecord(Long userId, String sendContent, RiskMsgSendType riskMsgSendType) throws Exception{
        RiskRecordEntity riskRecordEntity = new RiskRecordEntity();
        riskRecordEntity.setUserId(userId);
        riskRecordEntity.setSendContent(sendContent);
        riskRecordEntity.setSendType(riskMsgSendType.getCode());
        riskRecordEntity.setCreated(new Date());
        return riskRecordDao.insertRiskRecord(riskRecordEntity);
    }
}

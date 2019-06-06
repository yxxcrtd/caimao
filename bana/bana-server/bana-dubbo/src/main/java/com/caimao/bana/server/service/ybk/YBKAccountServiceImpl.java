package com.caimao.bana.server.service.ybk;

import com.caimao.bana.api.entity.TpzSmsOutEntity;
import com.caimao.bana.api.entity.req.ybk.FYBKQueryAccountListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkApiQueryAccountListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkExchangeQueryListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkNewAccountListReq;
import com.caimao.bana.api.entity.res.ybk.FYbkAccountSimpleRes;
import com.caimao.bana.api.entity.res.ybk.FYbkNewAccountListRes;
import com.caimao.bana.api.entity.ybk.YBKAccountEntity;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import com.caimao.bana.api.enums.ESmsBizType;
import com.caimao.bana.api.enums.ESmsStatus;
import com.caimao.bana.api.enums.EYbkExchangeStatus;
import com.caimao.bana.api.enums.ybk.EYbkApplyStatus;
import com.caimao.bana.api.service.ybk.IYBKAccountService;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.dao.sms.TpzSmsOutDao;
import com.caimao.bana.server.dao.ybk.YBKAccountDao;
import com.caimao.bana.server.dao.ybk.YBKExchangeDao;
import com.caimao.bana.server.utils.ChannelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service("ybkAccountService")
public class YBKAccountServiceImpl implements IYBKAccountService {

    @Resource
    private YBKAccountDao ybkAccountDao;
    @Autowired
    private TpzSmsOutDao smsOutDAO;
    @Resource
    private YBKExchangeDao exchangeDao;

    @Override
    public FYBKQueryAccountListReq queryAccountWithPage(FYBKQueryAccountListReq req) throws Exception {
        req.setItems(ybkAccountDao.queryAccountWithPage(req));
        return req;
    }

    @Override
    public void insert(YBKAccountEntity entity) throws Exception {
        entity.setCreateDate(new Date());
        ybkAccountDao.insert(entity);
    }

    /**
     * 更新邮币卡账户信息
     * @param entity 实体类
     * @throws Exception
     */
    public void update(YBKAccountEntity entity)throws Exception {
        ybkAccountDao.update(entity);
        if (entity.getStatus().equals(EYbkApplyStatus.OK.getCode())) {
            // 开通成功，发送短信验证码
            try {
                YBKAccountEntity ybkAccountEntity = this.ybkAccountDao.queryById(entity.getId());
                // 获取交易所信息
                YbkExchangeEntity exchangeEntity = this.exchangeDao.selectByPrimaryKey(ybkAccountEntity.getExchangeIdApply());
                String smsContent = String.format("%s交易账号%s已开通，初始密码为身份证号后6位数字。官方将在1-2工作日进行开户审核并下发短信通知。", exchangeEntity.getName(), ybkAccountEntity.getExchangeAccount());

                if(exchangeEntity.getShortName().equals("htybk") || exchangeEntity.getShortName().equals("bjybk")){
                    smsContent = String.format("%s交易账号%s已开通，初始密码为身份证号后6位逆序数字。官方将在1-2工作日进行开户审核并下发短信通知。", exchangeEntity.getName(), ybkAccountEntity.getExchangeAccount());
                }
                
                TpzSmsOutEntity smsOut = new TpzSmsOutEntity();
                smsOut.setMobile(ybkAccountEntity.getPhoneNo());
                smsOut.setSmsContent(smsContent);
                smsOut.setCreateDatetime(new Date());
                smsOut.setSmsStatus(ESmsStatus.SEND.getCode());
                smsOut.setSmsStatus(ESmsBizType.ALARM.getCode());
                this.smsOutDAO.save(smsOut);
            } catch (Exception e) {
                throw e;
            }
        }
        if (entity.getStatus().equals(EYbkApplyStatus.FAIL.getCode())) {
            // 开通失败的发送短信
            try {
                YBKAccountEntity ybkAccountEntity = this.ybkAccountDao.queryById(entity.getId());
                // 获取交易所信息
                YbkExchangeEntity exchangeEntity = this.exchangeDao.selectByPrimaryKey(ybkAccountEntity.getExchangeIdApply());
                String smsContent = String.format("%s由于%s，开户失败，请按提示重新提交您的资料。详询:4000-898-000", exchangeEntity.getName(), ybkAccountEntity.getReason());

                TpzSmsOutEntity smsOut = new TpzSmsOutEntity();
                smsOut.setMobile(ybkAccountEntity.getPhoneNo());
                smsOut.setSmsContent(smsContent);
                smsOut.setCreateDatetime(new Date());
                smsOut.setSmsStatus(ESmsStatus.SEND.getCode());
                smsOut.setSmsStatus(ESmsBizType.ALARM.getCode());
                this.smsOutDAO.save(smsOut);
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public void delete(Integer id)throws Exception {
        ybkAccountDao.delete(id);
    }

    public YBKAccountEntity queryById(Long id)throws Exception{
        return ybkAccountDao.queryById(id);
    }

    /**
     * 根据用户id获取个人开户信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<YBKAccountEntity> queryByUserId(Long userId) throws Exception {
        return this.ybkAccountDao.queryByUserId(userId);
    }

    /**
     * 查询用户开户申请列表
     * @param req
     * @throws Exception
     */
    @Override
    public FYbkApiQueryAccountListReq queryApiAccountApply(FYbkApiQueryAccountListReq req) throws Exception {
        List<FYbkAccountSimpleRes> list = this.ybkAccountDao.queryApiAccountApplyList(req);
        if (list != null) for (FYbkAccountSimpleRes res : list) {
            res.setBankNumAll(res.getBankNum());
            res.setBankNum(ChannelUtil.hide(res.getBankNum(), 4, 4));
        }
        req.setItems(list);
        return req;
    }

    /**
     * 查询这个用户还可以开通那些交易所
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<YbkExchangeEntity> mayOpenExchange(Long userId) throws Exception {
        // 查询我已经开了哪些交易所
        FYbkApiQueryAccountListReq req = new FYbkApiQueryAccountListReq();
        req.setUserId(userId);
        //req.setStatus(EYbkApplyStatus.OK.getCode());
        req.setItems(this.ybkAccountDao.queryApiAccountApplyList(req));

        if (req.getItems().size() == 0) {
            return new ArrayList<>();
        }
        List<String> openExchange = new ArrayList<>();
        List<String> openBank = new ArrayList<>();
        for (FYbkAccountSimpleRes accountSimpleRes : req.getItems()) {
            if (!accountSimpleRes.getStatus().equals(EYbkApplyStatus.FAIL.getCode().toString())) {
                openExchange.add(accountSimpleRes.getExchangeShortName());
            }
            if (accountSimpleRes.getStatus().equals(EYbkApplyStatus.OK.getCode().toString())) {
                openBank.add(accountSimpleRes.getBankCode());
            }
        }

        // 查询所有的交易所
        FYbkExchangeQueryListReq exchangeQueryListReq = new FYbkExchangeQueryListReq();
        exchangeQueryListReq.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> exchangeEntityList = this.exchangeDao.queryList(exchangeQueryListReq);

        List<YbkExchangeEntity> resList = new ArrayList<>();

        for (YbkExchangeEntity exchangeEntity : exchangeEntityList) {
            // 看看有没有这个交易所
            if (exchangeEntity.getNumber() == null || Objects.equals(exchangeEntity.getNumber(), "")) {
                continue;
            }
            if (openExchange.contains(exchangeEntity.getShortName())) {
                continue;
            }
            String[] supportBanks = exchangeEntity.getSupportBank().split(",");
            for (String bank : supportBanks) {
                if (openBank.contains(bank)) {
                    resList.add(exchangeEntity);
                    break;
                }
            }
        }
        return resList;
    }

    /**
     * 一步开户
     * @param userId
     * @param exchangeShortCode
     * @return
     * @throws Exception
     */
    @Override
    public YBKAccountEntity oneStepOpenExchange(Long userId, String exchangeShortCode) throws Exception {
        YbkExchangeEntity exchangeEntity = this.exchangeDao.selectByShortName(exchangeShortCode);
        if (exchangeEntity == null) {
            throw new CustomerException("开户的交易所未查到", 888888);
        }
        if (exchangeEntity.getNumber() == null || Objects.equals(exchangeEntity.getNumber(), "")) {
            throw new CustomerException("交易所不支持开户", 888888);
        }
        String[] supportBanks = exchangeEntity.getSupportBank().split(",");

        List<YBKAccountEntity> accountEntityList = this.ybkAccountDao.queryByUserId(userId);
        YBKAccountEntity accountEntity = null;
        for (YBKAccountEntity entity: accountEntityList) {
            if (!Objects.equals(entity.getStatus(), EYbkApplyStatus.OK.getCode())) {
                continue;
            }
            if (entity.getExchangeIdApply().equals(exchangeEntity.getId())) {
                throw new CustomerException("该交易所已经开户成功", 888888);
            }
            for (String bank : supportBanks) {
                if (Objects.equals(bank, entity.getBankCode())) {
                    accountEntity = entity;
                }
            }
        }
        if (accountEntity == null) {
            throw new CustomerException("信息不全，无法完成开户", 888888);
        }

        YBKAccountEntity newAccountEntity = new YBKAccountEntity();
        newAccountEntity.setUserId(userId);
        newAccountEntity.setUserName(accountEntity.getUserName());
        newAccountEntity.setPhoneNo(accountEntity.getPhoneNo());
        newAccountEntity.setStatus(EYbkApplyStatus.INIT.getCode());
        newAccountEntity.setBankCode(accountEntity.getBankCode());
        newAccountEntity.setBankNum(accountEntity.getBankNum());
        newAccountEntity.setBankPath(accountEntity.getBankPath());
        newAccountEntity.setCardNumber(accountEntity.getCardNumber());
        newAccountEntity.setCardOppositePath(accountEntity.getCardOppositePath());
        newAccountEntity.setCardPath(accountEntity.getCardPath());
        newAccountEntity.setCardType(accountEntity.getCardType());
        newAccountEntity.setExchangeIdApply(exchangeEntity.getId());
        newAccountEntity.setCreateDate(new Date());
        this.insert(newAccountEntity);

        return newAccountEntity;
    }

    /**
     * 查询最新的用户开户申请列表
     * @param req
     * @throws Exception
     */
    @Override
    public FYbkNewAccountListReq queryNewAccountApply(FYbkNewAccountListReq req) throws Exception {
        List<FYbkNewAccountListRes> resList = this.ybkAccountDao.queryNewAccountApplyList(req);
        if (resList != null && resList.size() > 0) {
            for (FYbkNewAccountListRes res : resList) {
                res.setUserName(ChannelUtil.hide(res.getUserName(), 1, 0));
                res.setPhoneNo(ChannelUtil.hide(res.getPhoneNo(), 3, 4));
            }
        }
        req.setItems(resList);
        return req;
    }
}
/*
*AccountManager.java
*Created on 2015/5/13 10:39
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server.utils;

import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.TpzAccountFreezeJourEntity;
import com.caimao.bana.api.entity.TpzAccountJourEntity;
import com.caimao.bana.api.enums.EAccountStatus;
import com.caimao.bana.api.enums.EAccountStatusReason;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.accountDao.TpzAccountFreezeJourDao;
import com.caimao.bana.server.dao.accountDao.TpzAccountJourDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0.1
 */
@Service
public class AccountManager {

    private static final Logger logger = LoggerFactory.getLogger(AccountManager.class);

    @Autowired
    private TpzAccountDao tpzAccountDao;
    @Autowired
    private TpzAccountFreezeJourDao tpzAccountFreezeJourDao;
    @Autowired
    private TpzAccountJourDao tpzAccountJourDao;
    @Autowired
    private MemoryDbidGenerator dbidGenerator;

    /**
     * 生成用户的账户信息
     * @param userId    用户ID
     * @param realName  姓名
     * @return  账户ID
     * @throws Exception
     */
    public Long doGenerateAccount(Long userId, String realName) throws Exception {
        if (userId == null) {
            throw new CustomerException("用户名为空", 888888);
        }
        TpzAccountEntity accountEntity = new TpzAccountEntity();
        Date date = new Date();
        accountEntity.setPzAccountId(this.dbidGenerator.getNextId());
        accountEntity.setUserId(userId);
        accountEntity.setUserRealName(realName);
        accountEntity.setCreateDatetime(date);
        accountEntity.setUpdateDatetime(date);
        accountEntity.setCurrencyType("CNY");
        accountEntity.setAccountStatus(EAccountStatus.NORMAL.getCode());

        accountEntity.setVersion(0L);
        accountEntity.setAvalaibleAmount(0L);
        long rand = System.currentTimeMillis();
        String hash = MD5Util.md5(new Long(rand).toString());
        accountEntity.setHash(hash);

        TpzAccountEntity dbacount = this.tpzAccountDao.getAccountByUserId(userId);
        if (dbacount != null) {
            throw new CustomerException("用户已有账户，不能进行实名认证。", 888888);
        }
        this.tpzAccountDao.save(accountEntity);
        long id = accountEntity.getPzAccountId();

        String random = id + 0L + hash;
        String md5code = MD5Util.md5(random);
        accountEntity.setMd5Code(md5code);
        this.tpzAccountDao.update(accountEntity);
        return id;
    }

    /**
     * 冻结用户资产的方法 方法需要添加在事务中
     * @param refSerialNo   关联的业务ID
     * @param pzAccountId   用户资产ID
     * @param amount        冻结数量
     * @param seqFlag       方向
     * @param accountBizType    来源
     * @return  返回记录的ID
     * @throws Exception
     */
    public long dofreezeAmount(String refSerialNo, Long pzAccountId, Long amount, String seqFlag, String accountBizType) throws Exception {
        TpzAccountEntity tpzAccountEntity = this.tpzAccountDao.getAccountByAccountId(pzAccountId);
        if (tpzAccountEntity == null) {
            throw new CustomerException(pzAccountId + "的账号不存在", 888888);
        }
        long preamount = tpzAccountEntity.getFreezeAmount();
        long postamount = 0L;
        if (seqFlag.equals(ESeqFlag.COME.getCode())) {
            if (tpzAccountEntity.getAvalaibleAmount() < preamount + amount) {
                logger.error("冻结金额 > 总金额,逻辑错误");
                throw new CustomerException("账户可用余额不足", 888888);
            }
            postamount = tpzAccountEntity.getFreezeAmount() + amount;
            tpzAccountEntity.setFreezeAmount(postamount);
            tpzAccountEntity.setUpdateDatetime(new Date());
            this.tpzAccountDao.update(tpzAccountEntity);
        } else if (seqFlag.equals(ESeqFlag.GO.getCode())) {
            postamount = tpzAccountEntity.getFreezeAmount() - amount;
            if (postamount < 0L) {
                logger.error("解冻金额>冻结金额");
                throw new CustomerException("解冻金额有误，请检查", 888888);
            }
            tpzAccountEntity.setFreezeAmount(postamount);
            tpzAccountEntity.setUpdateDatetime(new Date());
            this.tpzAccountDao.update(tpzAccountEntity);
        } else {
            logger.error(seqFlag + "不合法");
            throw new CustomerException(seqFlag + "不是合法类型", 888888);
        }

        TpzAccountFreezeJourEntity accountFreezeJour = new TpzAccountFreezeJourEntity();
        accountFreezeJour.setPzAccountId(pzAccountId);
        accountFreezeJour.setUserId(tpzAccountEntity.getUserId());
        accountFreezeJour.setTransAmount(amount);
        accountFreezeJour.setPreAmount(preamount);
        accountFreezeJour.setPostAmount(postamount);
        accountFreezeJour.setSeqFlag(seqFlag);
        accountFreezeJour.setRefSerialNo(refSerialNo);
        accountFreezeJour.setAccountBizType(accountBizType);
        accountFreezeJour.setTransDatetime(new Date());
        accountFreezeJour.setWorkDate(DateUtil.convertDateToString("yyyyMMdd", new Date()));
        this.tpzAccountFreezeJourDao.save(accountFreezeJour);

        return accountFreezeJour.getId();
    }

    /**
     * 变更用户的资产方法  方法需要包含在事务内
     * @param refSerialNo   关联的业务ID
     * @param pzAccountId   账户资产ID
     * @param amount        变动数量
     * @param accountBizType    资产变动来源
     * @param seqFlag       变动类型
     * @return
     * @throws Exception
     */
    public long doUpdateAvaiAmount(String refSerialNo, Long pzAccountId, Long amount, String accountBizType, String seqFlag) throws Exception {
        int retrycount = 3;
        long sleeptime = 2000L;
        List exceptions = new ArrayList();
        TpzAccountEntity tpzAccountEntity = this.tpzAccountDao.getAccountByAccountId(pzAccountId);
        if (tpzAccountEntity == null) {
            throw new CustomerException("账号不存在"+pzAccountId, 888888);
        }
        long preamount = tpzAccountEntity.getAvalaibleAmount();
        String hash = tpzAccountEntity.getHash();
        String md5Code = tpzAccountEntity.getMd5Code();
        String status = tpzAccountEntity.getAccountStatus();
        long version = tpzAccountEntity.getVersion();
        long posamount = 0L;

        if ((!seqFlag.equals(ESeqFlag.COME.getCode())) && (!seqFlag.equals(ESeqFlag.GO.getCode()))) {
            logger.error(seqFlag + "不合法");
            CustomerException exception0 = new CustomerException(seqFlag + "不合法", 88888);
            exceptions.add(exception0);
        }

        if (!status.equals(EAccountStatus.NORMAL.getCode())) {
            logger.error(pzAccountId + "账户已冻结");
            CustomerException exception1 = new CustomerException("账户已冻结", 888888);
            exceptions.add(exception1);
        }

        if ((StringUtils.isEmpty(hash)) || (StringUtils.isEmpty(md5Code))) {
            logger.error(pzAccountId + "不是合法账户");
            CustomerException exception2 = new CustomerException(pzAccountId + "不是合法账户", 888888);
            exceptions.add(exception2);
        }
        boolean flag = checkPass(pzAccountId, preamount, hash, md5Code);
        if (!flag) {
            logger.error(pzAccountId + "账户被非法篡改");
            CustomerException exception3 = new CustomerException(pzAccountId + "不是合法账户", 888888);
            exceptions.add(exception3);

            // 锁定账户
            tpzAccountEntity.setAccountStatus(EAccountStatus.BLOCK.getCode());
            tpzAccountEntity.setAccountStatusReason(EAccountStatusReason.AUTOBLOCK.getCode());
            this.tpzAccountDao.update(tpzAccountEntity);

            throw exception3;
        }

        if (exceptions.size() > 0) {
            String message = "";
            for (int i = 0; i < exceptions.size(); i++) {
                message = message + ((Throwable)exceptions.get(i)).getMessage() + "&&";
            }
            message = message.substring(0, message.length() - 2);
            CustomerException updateException = new CustomerException(message, 888888);

            //updateException.put("doUpdateAvaiAmount", exceptions);
            throw updateException;
        }
        if (seqFlag.equals(ESeqFlag.COME.getCode())) {
            posamount = preamount + amount;
            tpzAccountEntity.setAvalaibleAmount(posamount);
        } else if (seqFlag.equals(ESeqFlag.GO.getCode())) {
            posamount = preamount - amount;
            if (posamount - tpzAccountEntity.getFreezeAmount() < 0L) {
                logger.error("账户可用余额不足");
                throw new CustomerException("账户可用余额不足", 888888);
            }
            tpzAccountEntity.setAvalaibleAmount(posamount);
        }
        tpzAccountEntity.setUpdateDatetime(new Date());

        tpzAccountEntity = updateEntity(tpzAccountEntity, pzAccountId, posamount);

        // 不进行重试尝试了
        this.tpzAccountDao.update(tpzAccountEntity);
        //retrycount--;

        // 保存资产变动历史
        TpzAccountJourEntity accountJour = new TpzAccountJourEntity();
        accountJour.setPzAccountId(pzAccountId);
        accountJour.setUserId(tpzAccountEntity.getUserId());
        accountJour.setAccountBizType(accountBizType);
        accountJour.setTransAmount(amount);
        accountJour.setPreAmount(preamount);
        accountJour.setPostAmount(posamount);
        accountJour.setSeqFlag(seqFlag);
        accountJour.setRefSerialNo(refSerialNo);
        accountJour.setTransDatetime(new Date());
        accountJour.setWorkDate(DateUtil.convertDateToString("yyyyMMdd", new Date()));

        accountJour.setVersion(version);
        try {
            this.tpzAccountJourDao.save(accountJour);
        } catch (Exception e) {
            logger.error("保存资金流水异常 {}", e);
            this.tpzAccountJourDao.save(accountJour);
        }



        return accountJour.getId();
    }

    private boolean checkPass(Long pzAccountId, long perAmount, String hash, String md5Code) {
        boolean flag = false;
        String rand = pzAccountId.longValue() + perAmount + hash;
        String generate = MD5Util.md5(rand);
        if (generate.equals(md5Code)) {
            flag = true;
        }
        return flag;
    }

    private TpzAccountEntity updateEntity(TpzAccountEntity account, long pzid, long postamount) {
        long rand = System.currentTimeMillis();
        String hash = MD5Util.md5(new Long(rand).toString());
        account.setHash(hash);

        String random = pzid + postamount + hash;
        String md5code = MD5Util.md5(random);
        account.setMd5Code(md5code);

        return account;
    }


}

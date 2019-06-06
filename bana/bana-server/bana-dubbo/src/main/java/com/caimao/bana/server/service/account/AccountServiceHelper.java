package com.caimao.bana.server.service.account;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.TpzAccountFreezeJourEntity;
import com.caimao.bana.api.entity.req.F830104Req;
import com.caimao.bana.api.entity.res.F830101Res;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.accountDao.TpzAccountFreezeJourDao;
import com.hsnet.pz.core.util.DateUtil;

@Service("accountServiceHelper")
public class AccountServiceHelper {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceHelper.class);
    @Autowired
    private TpzAccountDao accountDAO;
    @Autowired
    private TpzAccountFreezeJourDao pzAccountFreezeJourDAO;

    public F830101Res getPZAccount(Long userId) throws CustomerException {
        F830101Res res = null;
        if ((StringUtils.isEmpty(String.valueOf(userId))) || (userId == null)) {
            this.logger.error("用户名" + userId + "为空");
            throw new CustomerException("用户名" + userId + "为空", 830101, "BizServiceException");
        }
        res = this.accountDAO.getByUserId(userId);
        if (res == null) {
            throw new CustomerException("用户名" + userId + "融资账户不存在", 830101, "BizServiceException");
        }

        return res;
    }

    public void doFundTransfer(F830104Req dto)
    {
//      String prodType = this.loanManager.getProdTypeByHoms(dto.getHomsFundAccount(), dto.getHomsCombineId());
//
//      if ((prodType.equals(EProdType.FREE.getCode())) || (prodType.equals(EProdType.FIRM_CONTEST.getCode())))
//      {
//        throw new BizServiceException("83010400", "该子账户不允许转入转出");
//      }
//      Long userId = dto.getUserId();
//      F830101Res res = getPZAccount(userId);
//      long transAmount = dto.getTransAmount().longValue();
//      String seqFlag = dto.getSeqFlag();
//      String accountBizType = null;
//      HomsAccountJour jour = new HomsAccountJour();
//      if (seqFlag.equals(ESeqFlag.COME.getCode()))
//      {
//        F830312Req f830312Req = new F830312Req();
//        f830312Req.setUserId(userId);
//        f830312Req.setHomsFundAccount(dto.getHomsFundAccount());
//        f830312Req.setHomsCombineId(dto.getHomsCombineId());
//        F830312Res f830312Res = this.accountHoms.getHomsAssetsInfo(f830312Req);
//        long totalProfit = f830312Res.getTotalProfit().longValue();
//        long curAmount = f830312Res.getCurAmount().longValue();
//        String rate = this.parameterManager.getValue("homsoutrate");
//        if ((rate != null) && (!"".equals(rate))) {
//          totalProfit = ()(totalProfit * Double.parseDouble(rate));
//        }
//        long canTrans = Math.min(totalProfit, curAmount);
//        if ((totalProfit <= 0L) || (transAmount > canTrans)) {
//          throw new BizServiceException("83010411", "超出可转出金额");
//        }
//        accountBizType = EAccountBizType.HOMS_OUT.getCode();
//        jour.setRemark("提盈");
//      } else if (seqFlag.equals(ESeqFlag.GO.getCode()))
//      {
//        if (transAmount > res.getAvalaibleAmount().longValue() - res.getFreezeAmount().longValue()) {
//          throw new BizServiceException("83010401", "转入超出可用余额");
//        }
//        accountBizType = EAccountBizType.HOMS_IN.getCode();
//        jour.setRemark("追保");
//      }
//      jour.setUserId(userId);
//      jour.setPzAccountId(res.getPzAccountId());
//      jour.setHomsFundAccount(dto.getHomsFundAccount());
//      jour.setHomsCombineId(dto.getHomsCombineId());
//      jour.setTransAmount(Long.valueOf(transAmount));
//      jour.setSeqFlag(seqFlag);
//      String refSerialNo = this.homsManager.transferfunds(jour).toString();
//      this.pzManager.doUpdateAvaiAmount(refSerialNo, res.getPzAccountId(), Long.valueOf(transAmount), accountBizType, seqFlag);
    }
    /**
     * @param userId
     * @return
     * @throws CustomerException 
     */
    public Long getPZAccountIdByUserId(Long userId) throws CustomerException {
          if (userId == null) {
            throw new CustomerException("用户编号为空",830101,"BizServiceException");
          }
          return this.getPZAccount(userId).getPzAccountId();
    }

    /**
     * @param string
     * @param pzAccountId
     * @param valueOf
     * @param code
     * @param code2
     * @throws CustomerException 
     */
    public long dofreezeAmount(String refSerialNo, Long pzAccountId, Long amount, String seqFlag, String accountBizType) throws CustomerException {
        TpzAccountEntity pzAccount = (TpzAccountEntity)this.accountDAO.getById(pzAccountId);
        if (pzAccount == null) {
            throw new CustomerException(pzAccountId + "的账号不存在", 111, "BizServiceException");
        }

        long preamount = pzAccount.getFreezeAmount().longValue();
        long postamount = 0L;

        if (seqFlag.equals(ESeqFlag.COME.getCode()))
        {
          if (pzAccount.getAvalaibleAmount().longValue() < preamount + amount.longValue()) {
            this.logger.error("冻结金额>总金额,逻辑错误");
            throw new CustomerException("账户可用余额不足",111,"BizServiceException");
          }
          postamount = pzAccount.getFreezeAmount().longValue() + amount.longValue();
          pzAccount.setFreezeAmount(Long.valueOf(postamount));
          pzAccount.setUpdateDatetime(new Date());
          this.accountDAO.update(pzAccount);
        } else if (seqFlag.equals(ESeqFlag.GO.getCode())) {
          postamount = pzAccount.getFreezeAmount().longValue() - amount.longValue();
          if (postamount < 0L) {
            this.logger.error("解冻金额>冻结金额");
            throw new CustomerException("解冻金额有误，请检查",111,"BizServiceException");
          }
          pzAccount.setFreezeAmount(Long.valueOf(postamount));
          pzAccount.setUpdateDatetime(new Date());

          this.accountDAO.update(pzAccount);
        }
        else
        {
          this.logger.error(seqFlag + "不合法");
          throw new CustomerException(seqFlag + "不是合法类型", 111, "BizServiceException");
        }

        TpzAccountFreezeJourEntity accountJour = new TpzAccountFreezeJourEntity();
        accountJour.setPzAccountId(pzAccountId);
        accountJour.setUserId(pzAccount.getUserId());
        accountJour.setTransAmount(amount);
        accountJour.setPreAmount(Long.valueOf(preamount));
        accountJour.setPostAmount(Long.valueOf(postamount));
        accountJour.setSeqFlag(seqFlag);
        accountJour.setRefSerialNo(refSerialNo);
        accountJour.setAccountBizType(accountBizType);
        accountJour.setTransDatetime(new Date());
        accountJour.setWorkDate(DateUtil.convertDateToString("yyyyMMdd", new Date()));
        this.pzAccountFreezeJourDAO.save(accountJour);
        long id = accountJour.getId().longValue();
        return id;
        
    }
}

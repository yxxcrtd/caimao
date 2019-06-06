/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.service.helper;

/**
 * @author yanjg
 * 2015年5月14日
 */
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.bana.api.entity.GradeDicountEntity;
import com.caimao.bana.api.entity.res.F831938Res;
import com.caimao.bana.server.dao.GradeDicountDAO;
import com.hsnet.pz.core.exception.BizServiceException;
@Service("gradeDicountHelper")
public class GradeDicountHelper {
      @Autowired
      private GradeDicountDAO gradeDicountDAO;

      public BigDecimal getDiscountRate(Short userGrade)
      {
        GradeDicountEntity gd = (GradeDicountEntity)this.gradeDicountDAO.getById(userGrade);
        if (gd != null) {
          return gd.getDiscountRate();
        }
        return new BigDecimal(1);
      }

      public void doSaveGradeDicount(Short userGrade,Double discountRate)
      {
        if (userGrade == null) {
          throw new BizServiceException("831935", "等级不能为空");
        }
        if (discountRate == null) {
          throw new BizServiceException("831935", "折扣率不能为空");
        }
        if (this.gradeDicountDAO.getById(userGrade) != null) {
          throw new BizServiceException("831935", "已存在该等级折扣率");
        }
        GradeDicountEntity gd = new GradeDicountEntity();
        gd.setUserGrade(userGrade);
        gd.setDiscountRate(new BigDecimal(discountRate.doubleValue()));
        this.gradeDicountDAO.save(gd);
      }

      public void doRefreshGradeDicount(Short userGrade,Double discountRate)
      {
        if (userGrade == null) {
          throw new BizServiceException("831935", "等级不能为空");
        }
        if (discountRate == null) {
          throw new BizServiceException("831935", "折扣率不能为空");
        }
        if (this.gradeDicountDAO.getById(userGrade) == null) {
          throw new BizServiceException("831935", "不存在该等级折扣率");
        }
        GradeDicountEntity gd = new GradeDicountEntity();
        gd.setDiscountRate(new BigDecimal(discountRate.doubleValue()));
        gd.setUserGrade(userGrade);
        this.gradeDicountDAO.update(gd);
      }

      public void doRemoveGradeDicount(Short userGrade)
      {
        if (userGrade == null) {
          throw new BizServiceException("831935", "等级不能为空");
        }
        if (this.gradeDicountDAO.getById(userGrade) == null) {
          throw new BizServiceException("831935", "不存在该等级折扣率");
        }
        this.gradeDicountDAO.deleteById(userGrade);
      }

      public List<F831938Res> queryGradeDicount()
      {
        return this.gradeDicountDAO.queryF831938Res();
      }

    /**
     * @param userGrade
     * @return
     */
    public BigDecimal getDiscountRate(BigInteger userGrade) {
        GradeDicountEntity gd = (GradeDicountEntity)this.gradeDicountDAO.getById(userGrade);
        if (gd != null) {
          return gd.getDiscountRate();
        }
        return new BigDecimal(1);
    }
}

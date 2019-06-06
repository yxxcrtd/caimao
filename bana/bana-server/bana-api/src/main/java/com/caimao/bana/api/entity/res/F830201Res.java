/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.res;
import java.math.BigDecimal;

import com.huobi.commons.utils.HsDateUtil;
/**
 * @author yanjg
 * 2015年6月5日
 */
public class F830201Res {
      private Long orderNo;
      private Long orderAmount;
      private String billAbstract;
      private Long loanAmount;
      private BigDecimal interestRate;
      private String interestSettleFlag;
      private String workDate;

      public Long getOrderNo()
      {
        return this.orderNo;
      }

      public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
      }

      public Long getOrderAmount() {
        return this.orderAmount;
      }

      public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
      }

      public String getBillAbstract() {
        return this.billAbstract;
      }

      public void setBillAbstract(String billAbstract) {
        this.billAbstract = billAbstract;
      }

      public Long getLoanAmount() {
        return this.loanAmount;
      }

      public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
      }

      public BigDecimal getInterestRate() {
        return this.interestRate;
      }

      public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
      }

      public String getInterestSettleFlag() {
        return this.interestSettleFlag;
      }

      public void setInterestSettleFlag(String interestSettleFlag) {
        this.interestSettleFlag = interestSettleFlag;
      }

      public String getWorkDate() {
        return this.workDate;
      }

      public void setWorkDate(String workDate) {
        this.workDate = HsDateUtil.convertDateNumToStr(workDate);
      }
}

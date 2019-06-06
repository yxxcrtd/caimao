/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.service.helper;

import org.springframework.beans.factory.annotation.Autowired;

import com.caimao.bana.api.entity.TpzProdDetailEntity;
import com.caimao.bana.api.entity.ProdDicountEntity;
import com.caimao.bana.api.entity.TpzProdEntity;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.dao.ProdDicountDAO;
import org.springframework.stereotype.Service;

/**
 * @author yanjg
 * 2015年5月14日
 */
@Service("prodDicountHelper")
public class ProdDicountHelper {
    @Autowired
    private ProdDicountDAO prodDicountDAO;
//    private ILoanManager loanManager;
    public void checkUse(Long prodId, long amount) throws CustomerException
    {
        ProdDicountEntity pd = prodDicountDAO.getByProd(prodId);
        long hasAmount = pd.getDicountTotalAmount().longValue() - pd.getDicountUseAmount().longValue();
        if(amount > hasAmount)
            throw new CustomerException("优惠金额不足",83194102,"BizServiceException");
        else
            return;
    }
    /**
     * @param prodId
     * @param amount
     */
    public void doRefreshUset(Long prodId, long amount) {
        ProdDicountEntity pd = new ProdDicountEntity();
        pd.setProdId(prodId);
        pd.setDicountUseAmount(Long.valueOf(amount));
        this.prodDicountDAO.updateUse(pd);
    }
    /**
     * @param prod
     * @param valueOf
     * @param valueOf2
     * @param valueOf3
     * @return
     */
    public TpzProdDetailEntity getProdDetail(TpzProdEntity prod, Integer valueOf, Long valueOf2, Integer valueOf3) {
        // TODO Auto-generated method stub
        return null;
    }
}

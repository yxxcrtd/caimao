/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.server.service.product;

import com.caimao.bana.api.entity.TpzProdEntity;
import com.caimao.bana.api.entity.req.product.FQueryProductReq;
import com.caimao.bana.api.entity.res.F830910Res;
import com.caimao.bana.api.entity.res.product.FProductDetailRes;
import com.caimao.bana.api.entity.res.product.FProductRes;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.product.IProductService;
import com.caimao.bana.server.dao.prod.TpzProdDao;
import com.caimao.bana.server.dao.prod.TpzProdDetailDao;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zxd $Id$
 * 
 */
@Service("productService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private TpzProdDao prodDAO;
    @Autowired
    private TpzProdDetailDao prodDetailDAO;

    @Override
    public FQueryProductReq queryProductList(FQueryProductReq req) {
        List list = this.prodDAO.queryProductList(req);
        req.setItems(list);
        return req;
    }

    @Override
    public FProductRes getProduct(Long prodId) throws CustomerException {
        if (prodId == null) {
            throw new CustomerException("产品编号不能为空", 83091101, "BizServiceException");
        }
        TpzProdEntity prod = this.prodDAO.getProd(prodId);
        if (prod == null) {
            throw new CustomerException("该产品不存在", 83091102, "BizServiceException");
        }
        FProductRes res = new FProductRes();
        try {
            BeanUtils.copyProperties(res, prod);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomerException("产品详情转换出错", 83091103, "BizServiceException");
        }
        return res;
    }

    @Override
    public List<FProductDetailRes> queryProdDetailList(Long prodId) throws CustomerException {
        if (prodId == null) {
            throw new CustomerException("产品编号不能为空", 83091201, "BizServiceException");
        }
        return this.prodDetailDAO.queryProdDetailList(prodId);
    }

    @Override
    public List<F830910Res> getProdListByStatusWithPage(FQueryProductReq req)
            throws CustomerException {
        return prodDAO.getProdListByStatusWithPage(req);
    }

}

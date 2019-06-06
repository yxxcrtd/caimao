package com.caimao.bana.api.service.product;

import com.caimao.bana.api.entity.req.product.FQueryProductReq;
import com.caimao.bana.api.entity.res.F830910Res;
import com.caimao.bana.api.entity.res.product.FProductDetailRes;
import com.caimao.bana.api.entity.res.product.FProductRes;
import com.caimao.bana.common.api.exception.CustomerException;

import java.util.List;

public interface IProductService {
    public abstract FQueryProductReq queryProductList(FQueryProductReq paramF830910Req) throws CustomerException;

    public abstract FProductRes getProduct(Long paramLong) throws CustomerException;

    public abstract List<FProductDetailRes> queryProdDetailList(Long paramLong) throws CustomerException;

    public abstract List<F830910Res> getProdListByStatusWithPage(FQueryProductReq req) throws CustomerException;
}

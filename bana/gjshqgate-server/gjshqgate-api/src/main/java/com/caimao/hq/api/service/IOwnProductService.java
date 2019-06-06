/**
 *
 */
package com.caimao.hq.api.service;


import com.caimao.hq.api.entity.OwnProduct;

import java.util.List;

/**
 * @author yzc
 */
public interface IOwnProductService {

    public List<OwnProduct> query(Long userId);

    public List<OwnProduct>  query(Long userId, String financeMic, String prodCode);

    public int insert(OwnProduct ownProduct);

    public int delete(String userId,String ownProductId);
    public int delete(List<OwnProduct> list) ;
    public List  wizard(String field);
    public int update(List<OwnProduct> ownProduct);
    public void sysGJSProduct();
}

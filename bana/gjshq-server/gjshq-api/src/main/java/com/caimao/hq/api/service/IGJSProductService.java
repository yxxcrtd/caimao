/**
 *
 */
package com.caimao.hq.api.service;


import com.caimao.hq.api.entity.Product;
import com.caimao.hq.api.entity.Snapshot;

import java.util.List;

/**
 * @author yzc
 */
public interface IGJSProductService {

    public List<Product> query(String finance_mic);
    public List<Product> wizard(String field);
    public List<Product> queryAll();
    public List<Product> query(Product product);
    public int insert(Product product);
    public Product convert(Snapshot snapshot);
}

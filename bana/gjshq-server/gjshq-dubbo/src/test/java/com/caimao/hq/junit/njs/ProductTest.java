package com.caimao.hq.junit.njs;

import com.caimao.hq.junit.BaseTest;
import com.caimao.hq.api.entity.Product;
import com.caimao.hq.utils.GJSProductUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductTest extends BaseTest {

    @Autowired
    private GJSProductUtils gjsProductUtils;

    @Test
    public void testProductAll(){

        Map map=new HashMap();
        List<Product> list= gjsProductUtils.getProductList("SJS");
        System.out.println(list);
    }


}

package com.caimao.hq.junit.hq;

import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.api.service.IOwnProductService;
import com.caimao.hq.junit.BaseTest;
import com.caimao.hq.api.entity.OwnProduct;
import com.caimao.hq.utils.GJSProductUtils;
import com.caimao.hq.utils.DateUtils;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.RedisUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2015/10/21.
 */
public class OwnProductTest extends BaseTest {

    @Autowired
    private IHQService hqService;

    @Autowired
    private IOwnProductService ownProductService;


    @Autowired
    private JRedisUtil jredisUtil;
    @Autowired
    private GJSProductUtils gjsProductUtils;

    @Resource
    RedisUtils redisUtils;
    @Test
    public void testInsert(){

        OwnProduct ownProduct=new OwnProduct();
        ownProduct.setProdCode("AG");
        ownProduct.setExchange("NJS");
        ownProduct.setCategory("0");
        ownProduct.setOptDate(DateUtils.getNoTime(""));
        ownProduct.setSort(0);
        ownProduct.setUserId(Long.parseLong("808330528292865"));
      //  ownProductService.insert(ownProduct);

    }

    @Test
    public void testQuery(){

        OwnProduct ownProduct=new OwnProduct();
        ownProduct.setProdCode("AG");
        ownProduct.setExchange("SJS");
        ownProduct.setCategory("0");
        ownProduct.setName("自选股1");
        ownProduct.setOptDate(DateUtils.getNoTime(""));
        ownProduct.setSort(0);
        ownProduct.setUserId(123123L);
        List list=hqService.queryOwnProductByUserId(809740686852097L);
        System.out.println(list);
    }
    @Test
    public void testQueryByUserIDAndExchange(){


        List list=ownProductService.query(809304177246209L, "SJS", "Ag(T+D)");
        System.out.println(list);
    }

    @Test
   public void testProduct(){


       System.out.println(jredisUtil.get(0,"njs_goods"));

   }
    @Test
    public void testDelete(){

        System.out.println(hqService.deleteOwnProduct("808330528292865", "93,94"));


        OwnProduct ownProduct=new OwnProduct();
        ownProduct.setProdCode("EU2O3");
        ownProduct.setExchange("NJS");
        ownProduct.setUserId(Long.parseLong("808330528292865"));

        OwnProduct ownProduct1=new OwnProduct();
        ownProduct1.setProdCode("IN");
        ownProduct1.setExchange("NJS");
        ownProduct1.setUserId(Long.parseLong("808330528292865"));


        List list=new ArrayList();
        list.add(ownProduct);
        list.add(ownProduct1);


        System.out.println(hqService.deleteOwnProduct(list));
    }


    @Test
    public void testDeleteList(){

        List<OwnProduct> ownProduct  =new ArrayList();
        OwnProduct  ownProduct1=new OwnProduct();
        ownProduct1.setUserId(Long.parseLong("808330528292865"));
        ownProduct1.setExchange("SJS");
        ownProduct1.setProdCode("iAu100g");
        ownProduct.add(ownProduct1);


        OwnProduct  ownProduct2=new OwnProduct();
        ownProduct2.setUserId(Long.parseLong("808330528292865"));
        ownProduct2.setExchange("SJS");
        ownProduct2.setProdCode("CEO2");
        ownProduct.add(ownProduct2);


        OwnProduct  ownProduct3=new OwnProduct();
        ownProduct3.setUserId(Long.parseLong("808330528292865"));
        ownProduct3.setExchange("SJS");
        ownProduct3.setProdCode("PR6O11");
        ownProduct.add(ownProduct3);

        String jsonString = JSON.toJSONString(ownProduct);
        System.out.println(jsonString);
        List paraList= JSON.parseArray(jsonString, OwnProduct.class);


        System.out.println(paraList);
       // System.out.println(ownProductService.delete(ownProduct));
    }

    @Test
    public void testUpdate(){

        OwnProduct ownProduct=new OwnProduct();
        ownProduct.setOwnProductId(90);
        ownProduct.setProdCode("CEO2");
        ownProduct.setSort(222);
        ownProduct.setUserId(Long.parseLong("808330528292865"));
        ownProduct.setExchange("NJS");



        OwnProduct ownProduct1=new OwnProduct();
        ownProduct1.setOwnProductId(91);
        ownProduct1.setSort(220);
        ownProduct1.setUserId(Long.parseLong("808330528292865"));
        ownProduct1.setExchange("NJS");
        ownProduct1.setProdCode("PR6O11");


        OwnProduct ownProduct2=new OwnProduct();
        ownProduct2.setOwnProductId(91);
        ownProduct2.setSort(110);
        ownProduct2.setUserId(Long.parseLong("808330528292865"));
        ownProduct2.setExchange("NJS");
        ownProduct2.setProdCode("PT");

        List list= new ArrayList();
        list.add(ownProduct);
        list.add(ownProduct1);
        list.add(ownProduct2);
        System.out.println(hqService.updateOwnProduct(list));

    }

    @Test
    public void testWizard(){
        List list= hqService.wizard("s");
        System.out.println(list);
    }

    @Test
    public void getGoodsCache(){

        Object goodsCacheObj = redisUtils.get(0, "njs_hq_goods");
        if (goodsCacheObj != null) {
            String goodsCacheStr = goodsCacheObj.toString();

            try {
                TreeMap<String, Map<String, Object>> obj= JSON.parseObject(goodsCacheStr, TreeMap.class);
                System.out.println(obj);
            } catch (Exception e) {

            }
        } else {
        }

    }
    @Test
    public void GJSProductUtils(){
        //List list= ownProductService.wizard("hj");
        gjsProductUtils.init();
    }


}

package com.caimao.hq.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.hq.api.entity.OwnProduct;
import com.caimao.hq.api.entity.Product;
import com.caimao.hq.api.entity.WizardRes;
import com.caimao.hq.api.service.IOwnProductService;
import com.caimao.hq.dao.OwnProductDao;
import com.caimao.hq.utils.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2015/9/30.
 */

@Service("ownProductService")
public class OwnProductServiceImpl implements IOwnProductService {
    @Autowired
    private GJSProductUtils gjsProductUtils;
    private Logger logger = Logger.getLogger(OwnProductServiceImpl.class);

    @Autowired
    private OwnProductDao ownProductDao;


    @Autowired
    private JRedisUtil jredisUtil;


    @Override
    public List<OwnProduct> query(Long userId) {
        return ownProductDao.query(userId);
    }


    @Override
    public void sysGJSProduct() {

        gjsProductUtils.init();
    }

    @Override
    public List<OwnProduct> query(Long userId, String financeMic, String prodCode) {
        return ownProductDao.query(userId, financeMic, prodCode);
    }

    @Override
    public int insert(OwnProduct ownProduct) {

        int result = 0;
        if (null != ownProduct) {
            Product productRedis = gjsProductUtils.getProduct(ownProduct.getExchange(), ownProduct.getProdCode());
            if (null != productRedis) {

                List<OwnProduct> isExist = ownProductDao.query(ownProduct.getUserId(), ownProduct.getExchange(), ownProduct.getProdCode());
                if (null == isExist||isExist.size()<1) {

                    ownProduct.setProdName(productRedis.getProdName());
                    ownProduct.setOptDate(DateUtils.getNoTime(""));
                    result = ownProductDao.insert(ownProduct);
                } else {
                    throw new RuntimeException("添加失败，该产品已经存在");
                }

            } else {
                throw new RuntimeException("添加失败，该产品不存在");
            }
        } else {
            throw new RuntimeException("添加失败，传入对象为空");
        }

        return result;
    }

    @Override
    public int delete(String userId, String ownProductId) {

        ownProductId = convert(ownProductId);
        return ownProductDao.delete(userId, ownProductId);
    }

    @Override
    public int delete(List<OwnProduct> list) {
        return ownProductDao.delete(list);
    }

    @Override
    public List wizard(String field) {

        TreeMap<String, Object> wizard = new TreeMap<>();
        wizardNJS(field, wizard);
        wizardSJS(field, wizard);
        return convertTreeMapToList(wizard);
    }

    public void wizardNJS(String field, TreeMap<String, Object> wizard) {

        String pinyin = PingYinUtil.getFirstSpell(field);
        try {
            LinkedHashMap<String, Map<String, Object>> treeMap = gjsProductUtils.getGoodsCache("NJS");
            match(treeMap, field, wizard);
            match(treeMap, pinyin, wizard);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void wizardSJS(String field, TreeMap<String, Object> wizard) {

        String pinyin = PingYinUtil.getFirstSpell(field);
        try {
            LinkedHashMap<String, Map<String, Object>> treeMap = gjsProductUtils.getGoodsCache("SJS");
            match(treeMap, field, wizard);
            match(treeMap, pinyin, wizard);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int update(List<OwnProduct> list) {

        return ownProductDao.update(list);

    }


    private List convertTreeMapToList(TreeMap<String, Object> wizard) {

        List listWizard = new ArrayList();
        listWizard.clear();
        if (null != wizard) {
            Iterator titer = wizard.entrySet().iterator();

            while (titer.hasNext()) {
                try {
                    Map.Entry ent = (Map.Entry) titer.next();
                    String keyt = ent.getKey().toString();
                    Map<String, Object> valuet = (Map) ent.getValue();
                    if (null != valuet) {
                        WizardRes wizardRes = new WizardRes();
                        ClassUtil.setFieldValue(valuet, wizardRes);
                        wizardRes.setExchange((String) valuet.get("exchange"));
                        listWizard.add(wizardRes);
                    }
                } catch (Exception ex) {
                    logger.error("Map to Bean error" + ex.getMessage());
                }

            }
        }
        return listWizard;
    }


    private void match(LinkedHashMap<String, Map<String, Object>> treeMap, String field, TreeMap<String, Object> wizard) {

        if (null != treeMap && null != wizard) {

            Iterator titer = treeMap.entrySet().iterator();
            String mapValue = "";
            while (titer.hasNext()) {

                Map.Entry ent = (Map.Entry) titer.next();
                String keyt = ent.getKey().toString();
                Map<String, Object> valuet = (Map) ent.getValue();
                if (null != valuet) {
                    mapValue = (String) valuet.get("pinyinJ");// 比较简拼
                    if (!StringUtils.isBlank(mapValue) && mapValue.toLowerCase().contains(field.toLowerCase())) {
                        wizard.put((String) valuet.get("exchange") + (String) valuet.get("prodCode"), valuet);
                    }

                    mapValue = (String) valuet.get("pinyinQ");// 比较全拼
                    if (!StringUtils.isBlank(mapValue) &&  mapValue.toLowerCase().contains(field.toLowerCase())) {
                        wizard.put((String) valuet.get("exchange") + (String) valuet.get("prodCode"), valuet);
                    }

                    mapValue = (String) valuet.get("prodCode");// 比较产品code
                    if (!StringUtils.isBlank(mapValue) && mapValue.toLowerCase().contains(field.toLowerCase())) {
                        wizard.put((String) valuet.get("exchange") + (String) valuet.get("prodCode"), valuet);
                    }



                    mapValue = (String) valuet.get("prodName");// 比较产品名称
                    if (!StringUtils.isBlank(mapValue) && mapValue.contains(field)) {
                        wizard.put((String) valuet.get("exchange") + (String) valuet.get("prodCode"), valuet);
                    }

                }

            }
        }


    }

    private String convert(String ownProductId) {

        if (!StringUtils.isBlank(ownProductId)) {
            if (ownProductId.substring(ownProductId.length() - 1, ownProductId.length()).equalsIgnoreCase(",")) {
                ownProductId = ownProductId.substring(0, ownProductId.length() - 1);
            }
        }
        return ownProductId;
    }
}

package com.caimao.hq.service;

import com.caimao.hq.api.service.IGJSHqJobService;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.utils.FileUtils;
import com.caimao.hq.utils.GJSProductUtils;
import com.caimao.hq.utils.ImportDataNJS;
import com.caimao.hq.utils.ImportDataSJS;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Properties;

/**
 * Created by yzc on 2015/11/9.
 */
@Service("gjsHqJobService")
public class GJSHqJobServiceImpl implements IGJSHqJobService{
    private Logger logger = Logger.getLogger(GJSHqJobServiceImpl.class);
    @Autowired
    private ImportDataSJS importDataSJS;

    @Autowired
    private ImportDataNJS importDataNJS;
    @Autowired
    public GJSProductUtils gjsProductUtils;
    @Autowired
    private IHQService hqService;

    /**
     * 同步产品
     */
    @Override
    public void updateProduct() {

        gjsProductUtils.init();

    }





    @Override
    public void reportData() {

        try {

            //Redis数据导入+修复（上金所）
            importDataSJS.importRedisCandle();
            importDataSJS.importRedisSnap();

            //上金所 candle,snapshot导入
//            importDataSJS.importDBCandle();
//            importDataSJS.importDBSnap();
            //南交所 candle  导入
//            importDataNJS.importDBCandle();
            importDataNJS.importRedisCandle();

        } catch (Exception ex) {

            logger.error("行情数据导入异常：" + ex.getMessage());
        }
    }
    @Override
    public void fillData() {

        try{
            gjsProductUtils.fillData();

        }catch (Exception ex){

            logger.error("填充数据失败：" + ex.getMessage());
        }


    }

}

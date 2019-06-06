package com.caimao.hq.junit.hq;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.api.entity.CandleReq;
import com.caimao.hq.junit.BaseTest;
import com.caimao.hq.core.NJSDataHandleThread;
import com.caimao.hq.dao.NJSCandleDao;
import com.caimao.hq.utils.FileUtils;
import com.caimao.hq.utils.ImportDataNJS;
import com.caimao.hq.utils.JRedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

/**
 * Created by yzc on 2015/11/12.
 */
public class NJSImportData extends BaseTest {


    @Autowired
    private NJSCandleDao njsCandleDao;
    @Autowired
    private NJSDataHandleThread njsDataHandleThread;
    @Autowired
    private JRedisUtil jredisUtil;

    @Autowired
    private ImportDataNJS importDataNJS;


    public Boolean isDiv(String prodCode){

           Boolean isDiv=false;
           if("AG".equalsIgnoreCase(prodCode)||"DY2O3".equalsIgnoreCase(prodCode)||"LU2O3".equalsIgnoreCase(prodCode)||"TM2O3".equalsIgnoreCase(prodCode)
                   ||"TB4O7".equalsIgnoreCase(prodCode)||"EU2O3".equalsIgnoreCase(prodCode)||"IN".equalsIgnoreCase(prodCode)){
               isDiv=true;
           }
        return  isDiv;
    }
    @Test
    public void importData(){

      //  importDataNJS.importDBCandle();
        importDataNJS.importRedisCandle();
    }




    @Test
    public void writeFileFromDB() {
        CandleReq candleReq=new CandleReq();
        candleReq.setExchange("NJS");
        candleReq.setCycle("7");
        candleReq.setBeginDate("2015112700");
        List<Candle> list=njsCandleDao.selectList(candleReq);
        for(Candle candle:list){
            importDataNJS.writeFile(candle.getCycle(),JSON.toJSONString(candle));
        }

    }
    public static double parseDouble(String value) {
        if (StringUtils.isBlank(value)) {
            // This is an invalid value.
            return 0.00;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {

        }
        // This is an invalid value.
        return 0.00;
    }
}

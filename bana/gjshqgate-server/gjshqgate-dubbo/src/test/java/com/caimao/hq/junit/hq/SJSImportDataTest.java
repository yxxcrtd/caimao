package com.caimao.hq.junit.hq;

import com.caimao.hq.junit.BaseTest;
import com.caimao.hq.utils.FileUtils;
import com.caimao.hq.utils.ImportDataSJS;
import com.caimao.hq.utils.JRedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by yzc on 2015/11/11.
 */

@Service("sjsImportData")
public class SJSImportDataTest extends BaseTest {


    @Autowired
    private JRedisUtil jredisUtil;

    @Autowired
    private ImportDataSJS importDataSJS;

    @Test
    public void  importData(){

//        importDataSJS.importDBCandle();
//        importDataSJS.importDBSnap();
        importDataSJS.importRedisCandle();
//        importDataSJS.importRedisSnap();
    }

}

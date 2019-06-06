package com.caimao.hq.utils;

import com.caimao.jserver.mina.MinaServer;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by yzc on 2015/12/17.
 */
public class ConfigUtils {


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
     public static Properties   getApplicationProperties(){
         Properties pps = new Properties();
        try {

            InputStream is = ConfigUtils.class.getResourceAsStream("/META-INF/conf/application.properties");
            pps.load(is);

        } catch (IOException e) {
            logger.error("读取配置文件错误:"+e.getMessage());
        }
        return   pps;
    }

    public  static void main(String args[]){

        Properties  ptt=ConfigUtils.getApplicationProperties();
        logger.info(ptt.getProperty("is_open_WriteCandle"));
    }

}

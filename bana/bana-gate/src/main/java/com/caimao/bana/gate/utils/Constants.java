package com.caimao.bana.gate.utils;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author 任培伟
 * @version 1.0.0
 */
public class Constants {
    private static ResourceBundle appBundle = PropertyResourceBundle.getBundle("conf/application");

    public static final String INSIDER_API_KEY = appBundle.getString("insider_api_key");
    
}

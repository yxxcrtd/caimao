package com.caimao.bana.server.utils;

import com.caimao.bana.common.api.exception.CustomerException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtils {
    /**
     * 检查密码规则
     * @param password 密码
     * @throws Exception
     */
    public static void checkPassword(String password) throws Exception{
        if (password == null || password.equals("")) throw new CustomerException("密码不能为空", 530001);
        //检测空格(不允许有空格)
        if(password.contains(" ")) throw new CustomerException("密码不能包含空格", 530002);
        //检测位数(8-20位)
        if(password.length() < 8 || password.length() > 20) throw new CustomerException("密码长度为8-20位", 530003);
        //检测是否是纯数字
        Matcher isNum = Pattern.compile("[0-9]*").matcher(password);
        if(isNum.matches()) throw new CustomerException("密码不能为纯数字", 530004);
    }

    /**
     * 检测密码强度
     * @param password 密码
     * @return
     * @throws Exception
     */
    public static String GetPwdSecurityLevel(String password) throws Exception{
        checkPassword(password);
        Integer pwdLength = password.length();
        //检测种类
        Integer pwdType = 0;
        Boolean isDigit = false;
        Boolean isLetter = false;
        Boolean isOther = false;
        for(int i = 0; i < password.length(); i++){
            if(Character.isDigit(password.charAt(i))){
                if(!isDigit) pwdType += 1;
                isDigit = true;
            }else if(Character.isLetter(password.charAt(i))){
                if(!isLetter) pwdType += 1;
                isLetter = true;
            }else{
                if(!isOther) pwdType += 1;
                isOther = true;
            }
        }
        //计算密码强度
        String pwdStrength;
        if(pwdType == 1){ //单一种类 纯数字不允许 低
            pwdStrength = "1";
        }else if(pwdType == 2){ //任意两种 大于10位 中 其他 低
            pwdStrength = pwdLength > 10?"2":"1";
        }else if(pwdType == 3){ //三种类型 大于10位 高 其他 低
            pwdStrength = pwdLength > 10?"3":"2";
        }else{
            throw new CustomerException("密码不合法，请输入8-20位 字母、数字或特殊字符，不能为纯数字", 350004);
        }
        return pwdStrength;
    }
}

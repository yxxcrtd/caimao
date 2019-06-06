package com.caimao.bana.common.api.base.validated;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验密码格式
 */
public class CheckPwdValidator implements ConstraintValidator<CheckPwd, String> {
    public void initialize(CheckPwd checkPwd) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        try{
            if (value == null || value.equals("")) return false;
            //检测空格(不允许有空格)
            if(value.contains(" ")) return false;
            //检测位数(8-20位)
            if(value.length() < 8 || value.length() > 20) return false;
            //检测是否是纯数字
            Matcher isNum = Pattern.compile("[0-9]*").matcher(value);
            return !isNum.matches();
        }catch(Exception e){
            return false;
        }
    }
}

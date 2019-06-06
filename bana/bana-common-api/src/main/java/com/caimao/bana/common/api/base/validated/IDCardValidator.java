package com.caimao.bana.common.api.base.validated;

import com.caimao.bana.common.api.utils.IdCardUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证手机号格式是否正确
 * Created by WangXu on 2015/6/4.
 */
public class IDCardValidator implements ConstraintValidator<IDCard, String> {
    public void initialize(IDCard idCard) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        try{
            if (value == null || value.equals("")) {
                return false;
            }
            return IdCardUtils.IDCardValidate(value);
        }catch(Exception e){
            return false;
        }
    }
}

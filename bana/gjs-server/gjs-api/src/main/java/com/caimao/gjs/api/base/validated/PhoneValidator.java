package com.caimao.gjs.api.base.validated;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证手机号格式是否正确
 * Created by WangXu on 2015/6/4.
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private String regexp;

    public void initialize(Phone constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        Pattern p = Pattern.compile(this.regexp);
        Matcher m = p.matcher(value);
        return m.matches();
    }
}

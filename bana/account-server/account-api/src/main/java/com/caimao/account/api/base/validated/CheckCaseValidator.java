package com.caimao.account.api.base.validated;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证参数是否是大小写
 * Created by WangXu on 2015/6/4.
 */
public class CheckCaseValidator implements ConstraintValidator<CheckCase, String> {
    private int caseMode;

    @Override
    public void initialize(CheckCase constraintAnnotation) {
        this.caseMode = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        } if (caseMode == 1) {
            return object.equals(object.toUpperCase());
        } else {
            return object.equals(object.toLowerCase());
        }
    }
}

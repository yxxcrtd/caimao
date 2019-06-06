package com.caimao.bana.common.api.base.validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验密码格式
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckPwdValidator.class)
@Documented
public @interface CheckPwd {
    String message() default "密码格式不正确，请输入8-20位字母、数字或特殊字符，不能为纯数字。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
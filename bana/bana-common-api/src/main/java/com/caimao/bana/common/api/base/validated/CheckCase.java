package com.caimao.bana.common.api.base.validated;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

/**
 * 自定义约束，大小写的
 * Created by WangXu on 2015/6/4.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckCaseValidator.class)
@Documented
public @interface CheckCase {
    String message() default "不符合大小写要求";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value();
}

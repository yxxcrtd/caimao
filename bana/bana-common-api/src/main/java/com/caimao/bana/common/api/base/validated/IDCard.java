package com.caimao.bana.common.api.base.validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 身份证号码
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = IDCardValidator.class)
@Documented
public @interface IDCard {
    String message() default "身份证号码不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

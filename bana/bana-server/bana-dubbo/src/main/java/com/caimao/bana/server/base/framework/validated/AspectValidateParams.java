package com.caimao.bana.server.base.framework.validated;

import com.caimao.bana.common.api.exception.CustomerException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 参数验证切面
 * Created by WangXu on 2015/6/3.
 */
@Component
@Aspect
public class AspectValidateParams {

    private static final Logger logger = LoggerFactory.getLogger(AspectValidateParams.class);

    @Autowired
    Validator paramValidator;

    @Pointcut("execution(* com.caimao.bana.server.service.*.*.*(..))")
    public void servicePackeg() {
    }

    @Pointcut("args(java.io.Serializable)")
    public void actionArgs() {

    }

    // 在执行方法前，验证所有服务包中的方法，并且参数是实现了 Serializable 的方法
    @Before("servicePackeg() && actionArgs()")
    public void doBefore(JoinPoint jp) throws Exception {
        this.logger.info("切面验证 {} 类中 {} 方法参数 {}", jp.getTarget().getClass(), jp.getSignature().getName(), jp.getArgs());
        if (jp.getArgs().length != 0) { // 有参数
            String errorMsg = null;
            for (int i = 0; i < jp.getArgs().length; i++) { // 验证所有参数
                this.logger.info("验证第 {} 个参数值 {}", i, ToStringBuilder.reflectionToString(jp.getArgs()[i]));
                Set<ConstraintViolation<Object>> set = this.paramValidator.validate(jp.getArgs()[i]);
                for (ConstraintViolation<Object> constraintValidator : set) {
                    if (errorMsg == null) {
                        errorMsg = constraintValidator.getMessage();
                    }
                    this.logger.warn("参数验证 {} 验证错误 : {}", jp.getArgs()[i], constraintValidator.getMessage());
                }
            }
            if (errorMsg != null) {
                // 抛出第一个就可以了
                throw new CustomerException(errorMsg, 666666);
            }
        }
    }
}

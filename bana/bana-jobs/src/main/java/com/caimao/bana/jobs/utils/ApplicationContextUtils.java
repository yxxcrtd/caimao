package com.caimao.bana.jobs.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author 任培伟
 * @version 1.0
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ApplicationContextUtils.applicationContext;
    }

    public static DefaultListableBeanFactory getDefaultListableBeanFactory() {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        return (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
    }

    public static boolean containsBean(String className) {
        return applicationContext.containsBean(className);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T getBean(Class clazz) {
        return (T) applicationContext.getBean(clazz);
    }
}

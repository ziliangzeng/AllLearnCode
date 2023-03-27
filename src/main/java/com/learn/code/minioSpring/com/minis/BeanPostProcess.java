package com.learn.code.minioSpring.com.minis;

public interface BeanPostProcess {

    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}

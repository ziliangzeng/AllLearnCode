package com.learn.code.minioSpring.com.minis;

public interface BeanPostProcessor {

    void setBeanFactory(AbstractAutowireCapableBeanFactory beanFactory);

    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}

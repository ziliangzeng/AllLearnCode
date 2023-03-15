package com.learn.code.minioSpring.com.minis;

public interface BeanFactory {

    Object getBean(String beanName) throws BeansException;
    void registerBeanDefinition(BeanDefinition beanDefinition);
}

package com.learn.code.minioSpring.com.minis;

public interface BeanDefinitionRegistry {

    void registryBeanDefinition(String beanName,BeanDefinition beanDefinition);
    void removeBeanDefinition(String beanName);
    BeanDefinition getBeanDefinition(String beanName);
    boolean containsBeanDefinition(String beanName);

}

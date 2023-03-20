package com.learn.code.minioSpring.com.minis;

public interface BeanFactory {

    Object getBean(String beanName) throws BeansException;
    void registerBeanDefinition(BeanDefinition beanDefinition);

    Boolean containsBean(String beanName);
    void registerBean(String beanName,Object obj);

    boolean isSingleton(String beanName);
    boolean isPrototype(String beanName);

    Class<?> getType(String beanName);

}

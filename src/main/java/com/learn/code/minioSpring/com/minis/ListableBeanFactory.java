package com.learn.code.minioSpring.com.minis;

import java.util.Map;

/**
 * 数组bean工厂,获取bean数组
 */
public interface ListableBeanFactory extends BeanFactory {
    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();

    String[] getBeanNamesForType(Class<?> type);

    <T> Map<String, T> getBeansOfType(Class<T> type);

}

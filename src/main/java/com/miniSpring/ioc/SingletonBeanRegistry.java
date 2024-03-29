package com.miniSpring.ioc;

/**
 * 单例bean注册表
 * 猜测应该是用来对beanDefinition的属性然后进行注册到beanFactory中.
 */
public interface SingletonBeanRegistry {

    void registrySingleton(String beanName,Object singletonObject);

    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    String[] getSingletonNames();

}

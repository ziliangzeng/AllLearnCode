package com.miniSpring.ioc;

public interface BeanFactory {

    Object getBean(String beanName) throws BeansException;

    //TODO 这里不应该是注册beanDefinition 后面删除
//    void registerBeanDefinition(BeanDefinition beanDefinition);

    Boolean containsBean(String beanName);
    void registerBean(String beanName,Object obj);

    boolean isSingleton(String beanName);
    boolean isPrototype(String beanName);

    Class<?> getType(String beanName);

//    void refresh();

}

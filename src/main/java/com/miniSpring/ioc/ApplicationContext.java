package com.miniSpring.ioc;

import com.miniSpring.ioc.factory.BeanFactoryPostProcessor;
import com.miniSpring.ioc.env.Environment;
import com.miniSpring.ioc.event.ApplicationEventPublisher;

/**
 * 主要目的：支持上下文环境和事件发布
 *
 * 实现了ListableBeanFactory 那么就是实现了取单例bean，数组bean
 * 实现了ApplicationEventPublisher 那么就是实现了事件发布管理\
 * ConfigurableBeanFactory->管理bean依赖关系？ TODO 还没有这个逻辑吧 */
public interface ApplicationContext extends Environment,
        ListableBeanFactory,
        ConfigurableBeanFactory,
        ApplicationEventPublisher {

    String getApplicationName();
    long getStartupDate();

    ConfigurableListableBeanFactory getBeanFactory();

    void setEnvironment(Environment environment);

    Environment getEnvironment();
    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor);
    //TODO beanFactory的refresh()应该要关闭？
    void refresh();
    void close();
    boolean isActive();


}

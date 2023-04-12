package com.miniSpring.ioc.factory;

/**
 * 有没有可能他的作用就是bean资源的加载?
 * 假如就是将@Bean @Service @Component 等注解加入到beanFactory中
 * 是通过什么手段实现的呢?
 * 通过BeanFactoryPostProcessor?
 * 好像不是,refresh()是里面才调用这个beanFactoryPostProcessor的
 *
 * TODO 后面要看一下这个BeanFactoryPostProcessor是在那里作用的
 */
public interface BeanFactoryPostProcessor {
}

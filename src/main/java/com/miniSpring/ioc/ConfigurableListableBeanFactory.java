package com.miniSpring.ioc;

/**
 * 继承了注解注入，数组bean，维护bean依赖关系，bean后置处理器管理?
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowiredCapableBeanFactory, ConfigurableBeanFactory {

}

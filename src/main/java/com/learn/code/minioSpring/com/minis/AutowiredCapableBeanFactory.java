package com.learn.code.minioSpring.com.minis;

/**
 * 注解注入接口能力抽象化
 *
 * TODO 我觉得这里也太抽象了? 这里Autowired体现在哪里? 实现细节全是DefaultListableBeanFactory
 * 主要是提供BeanPostProcessor的管理能力?
 * 并且DefaultListableBeanFactory也实现了这个接口的,那么还需要用一个ConfigurableListableBeanFactory来继承这个接口吗? 不冗余吗
 * 还说其他地方用到?
 */
public interface AutowiredCapableBeanFactory extends BeanFactory{
    int AUTOWIRE_NO = 0;
    int AUTOWIRE_BY_NAME = 1;
    int AUTOWIRE_BY_TYPE = 2;

    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;


}

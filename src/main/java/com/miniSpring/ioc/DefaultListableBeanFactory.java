package com.miniSpring.ioc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 在这里已经有点晕了...
 * 还有就是假如子类和父类同时实现了一个接口,并且父类提供了实现,那么子类就不需要再实现了
 * 是这样子吗? 应该是要抽象父类才行吧?
 * {@link src/main/resources/inheritance system.png}
 *
 * @Auther: qsx00
 * @Date: 2023/4/3 22:54
 * @Description:
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements ConfigurableListableBeanFactory {


    /**
     * TODO 为什么parentBeanFactory是ConfigurableListableBeanFactory类型的？
     *
     * 但是 ConfigurableListableBeanFactory 没有实现所有AbstractAutowireCapableBeanFactory的全部接口吧？
     *
     * TODO
     * 我觉得我为什么提出这个问题，就是我传入ConfigurableListableBeanFactory的接口，那么调用例如getBean()方法
     * 是否会调用父类的getBean方法的？
     * 并且应该是不能调用接口没有定义的方法的吧？！！！
     *
     * bean = this.parentBeanFactory.getBean(beanName);
     * 这里是不是有问题？ 这里是调用那个getBean方法呢？
     *
     *
     *
     * 按照我的理解，首先就是MVC的applicationContext AnnotationConfigWebApplicationContext
     * 他是自定义了DefaultListableBeanFactory
     * 然后调用下面的getBean方法。
     * 第一步 super.getBean()
     * 应该是拿的MVC容器的bean
     * 如果没有拿到的话，
     * 再去调用注入的BeanFactory
     *
     * AnnotationConfigWebApplicationContext#this.beanFactory.setParent(this.parentApplicationContext.getBeanFactory());
     * 他也是一个DefaultListableBeanFactory，然后又是调用下面的getBean方法
     * 然后就是从IOC里面去拿IOC容器的bean，如果还拿不到，直接报错不存在bean。
     * 因为他没有父容器，所以就是直接报错了。
     *
     *
     *
     */
    ConfigurableListableBeanFactory parentBeanFactory;


    public Object getBean(String beanName){
        //super. 调用AbstractBeanFactory的getBean方法,通用方法
        /**
         * 来做一下假设，
         * 首先 应该是先拿到WebApplicationContext的实例
         * 拿不到，再去拿IOC的实例
         */
        Object bean = super.getBean(beanName);
        if(bean == null){
            try {
                bean = this.parentBeanFactory.getBean(beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
        return bean;
    }



    public void setParent(ConfigurableListableBeanFactory beanFactory) {
        this.parentBeanFactory = beanFactory;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {

    }

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {

    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return new String[0];
    }

    @Override
    public String[] getDependenciesForBean(String beanName) {
        return new String[0];
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionNames.toArray(new String[0]);
    }

    /**
     * TODO 为什么不存起来的?
     * @param type
     * @return
     */
    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> result = new ArrayList<>();
        for (String beanName : beanDefinitionNames) {
            boolean matchFound = false;
            BeanDefinition beanDefinition = this.getBeanDefinition(beanName);
            //TODO 这里不对吧?为什么拿到的是BeanDefinition类型的?
            Class<?> aClass = beanDefinition.getClass();

            /**
             * 个人认为应该是这样子才对
             * String className = beanDefinition.getClassName();
             *             Class<?> aClass = null;
             *             try {
             *                 aClass = Class.forName(className);
             *             } catch (ClassNotFoundException e) {
             *                 throw new RuntimeException(e);
             *             }
             */



            if(type.isAssignableFrom(aClass)){
                matchFound = true;
            }
            else {
                matchFound = false;
            }
            if(matchFound){
                result.add(beanName);
            }

        }
        return result.toArray(new String[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {

        String[] beanNamesForType = getBeanNamesForType(type);
        Map<String, T> result = new HashMap<>();
        for (String beanName : beanNamesForType) {
            Object bean = getBean(beanName);
            result.put(beanName, (T) bean);
        }
        return result;
    }
}

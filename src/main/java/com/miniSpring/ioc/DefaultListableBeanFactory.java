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


    ConfigurableListableBeanFactory parentBeanFactory;

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

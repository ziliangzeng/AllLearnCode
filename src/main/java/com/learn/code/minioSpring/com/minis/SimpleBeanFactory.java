package com.learn.code.minioSpring.com.minis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 根据继承的DefaultSingletonBeanRegistry,可以看出这个类是一个单例注册表,用于存储bean
 * beanFactory,同时也是一个创建bean的工厂
 * BeanDefinitionRegistry,那么也是存储bean定义的地方罗
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private List<String> beanDefinitionNames = new ArrayList<>();

    /**
     * TODO 应该是待删除,跟上面的重复了
     */
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();
    //为什么用List？ 为什么不用set？
//   private List<String> beanNames = new ArrayList<String>();
//
//   private Map<String,Object> singletons = new HashMap<>();


    public SimpleBeanFactory() {
    }


    /**
     * 首先判断是否存在这个beanId？ 存在然后找bean定义，之后创建，存储进map 后面直接拿？
     * 这是懒加载？
     *
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        //尝试获取
        Object singleton = this.getSingleton(beanName);
        //获取不到bean，进行尝试获取bean定义，然后创建bean
        if (singleton == null) {

            if (!beanDefinitions.containsKey(beanName)) {
                throw new BeansException("No bean named " + beanName + " is defined");
            }
            BeanDefinition beanDefinition = beanDefinitions.get(beanName);
            try {
                Class<?> aClass = Class.forName(beanDefinition.getClassName());
                singleton = aClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            //注册bean
            registerBean(beanName, singleton);
        }
        return singleton;
    }

    /**
     * 这里需要外部的资源reader调用，将bean定义注册进来
     *
     * @param beanDefinition
     */
    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitions.put(beanDefinition.getId(), beanDefinition);
    }

    @Override
    public Boolean containsBean(String beanName) {
        return this.containsSingleton(beanName);
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        this.registrySingleton(beanName, obj);
    }

    @Override
    public boolean isSingleton(String beanName) {
        //TODO 这里有问题,暂时根据意思创建好方法,看后面的发展
        return beanDefinitionMap.get(beanName).isSingleton();
    }

    @Override
    public boolean isPrototype(String beanName) {
        return beanDefinitionMap.get(beanName).isPrototype();
    }

    @Override
    public Class<?> getType(String beanName) {
        return beanDefinitionMap.get(beanName).getClass();
    }

    @Override
    public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
        beanDefinitionNames.add(beanName);
        if (!beanDefinition.isLazyInit()) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionMap.remove(beanName);
        beanDefinitionNames.remove(beanName);
        removeSingleton(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }
}

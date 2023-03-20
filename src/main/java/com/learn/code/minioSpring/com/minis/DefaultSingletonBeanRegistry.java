package com.learn.code.minioSpring.com.minis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认bean注册表实现
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * 相对于BeanFactory，SimpleBeanFactory是一个更加简单的实现，它只是简单的将beanDefinition注册到beanFactory中
     * 将beanDefinition 给独立开来？，并没有集成到这里，更加解耦,这里只是单纯的注册bean(存储bean),并且提供获取bean 的能力
     */

    private List<String> beanNames = new ArrayList<>();
    private Map<String, Object> singletons = new ConcurrentHashMap<>(256);


    @Override
    public void registrySingleton(String beanName, Object singletonObject) {
        synchronized (this.singletons) {
            //这里是实现单例的关键，直接替换之前的bean
            this.singletons.put(beanName, singletonObject);
            this.beanNames.add(beanName);
        }

    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletons.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletons.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return (String[]) this.beanNames.toArray();
    }

    protected void removeSingleton(String beanName) {
        synchronized (this.singletons) {
            this.singletons.remove(beanName);
            this.beanNames.remove(beanName);
        }
    }


}

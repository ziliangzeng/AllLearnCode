package com.learn.code.minioSpring.com.minis;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我继承了BeanFactory，但是实际作用的是SimpleBeanFactory，
 * 这不就是组合吗---> DefaultListableBeanFactory
 */
public class ClassPathXmlApplicationContext implements BeanFactory {

    BeanFactory beanFactory;


    public ClassPathXmlApplicationContext(String fileName) {
        //获取资源
        Resource resource = new ClassPathXmlResource(fileName);
        //创建beanFactory
        BeanFactory beanFactory = new SimpleBeanFactory();
        //创建资源读取器，读取并且解析资源，插入到beanFactory中
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
    }


    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        //获取资源
        Resource resource = new ClassPathXmlResource(fileName);
        //创建beanFactory
        BeanFactory beanFactory = new SimpleBeanFactory();
        //创建资源读取器，读取并且解析资源，插入到beanFactory中
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if(isRefresh) {
            //TODO 没有看到BeanFactory的refresh方法，先加上了.
            refresh();
        }
    }




    @Override
    public Object getBean(String beanName) {
        try {
            return this.beanFactory.getBean(beanName);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanDefinition);
    }

    @Override
    public Boolean containsBean(String beanName) {
        return beanFactory.containsBean(beanName);
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        beanFactory.registerBean(beanName, obj);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return false;
    }

    @Override
    public boolean isPrototype(String beanName) {
        return false;
    }

    @Override
    public Class<?> getType(String beanName) {
        return null;
    }

    @Override
    public void refresh() {
        this.beanFactory.refresh();
    }

}

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
        return null;
    }

    @Override
    public void registerBean(String beanName, Object obj) {

    }

}

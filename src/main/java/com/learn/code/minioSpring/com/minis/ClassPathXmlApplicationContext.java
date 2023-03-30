package com.learn.code.minioSpring.com.minis;

import com.learn.code.minioSpring.com.minis.beans.factory.BeanFactoryPostProcessor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

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

    List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();


    @Override
    public Object getBean(String beanName) {
        try {
            return this.beanFactory.getBean(beanName);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }


    public ClassPathXmlApplicationContext(String fileName) {
//        //获取资源
//        Resource resource = new ClassPathXmlResource(fileName);
//        //创建beanFactory
//        BeanFactory beanFactory = new SimpleBeanFactory();
//        //创建资源读取器，读取并且解析资源，插入到beanFactory中
//        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
//        reader.loadBeanDefinitions(resource);
//        this.beanFactory = beanFactory;

        this(fileName, true);

    }


    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        //获取资源
        Resource resource = new ClassPathXmlResource(fileName);
        //创建beanFactory
        AutowireCapableBeanFactory beanFactory = new AutowireCapableBeanFactory();
//        BeanFactory beanFactory = new SimpleBeanFactory();
        //创建资源读取器，读取并且解析资源，插入到beanFactory中
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        /**
         * 这个isRefresh是不是有点多余,如果是false 的话,那么后置处理都不加载了?
         * 但是getbean的整体流程又都是会调用到beanPostProcessor的
         * TODO 这个后面需要关注一下
         */
        if (isRefresh) {
            //TODO 没有看到BeanFactory的refresh方法，先加上了.
            refresh();
        }
    }

    /**
     * TODO BeanFactoryPostProcessor 待扩展
     *
     * @return
     */
    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return beanFactoryPostProcessors;
    }

    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
        this.beanFactoryPostProcessors.add(beanFactoryPostProcessor);
    }


    @Override
    public void refresh() {
        //register bean processors that intercept bean creation
        registerBeanPostProcessors((AutowireCapableBeanFactory) beanFactory);
        // initialize other special beans in specific context subclasses
        onRefresh();
    }


    private void onRefresh() {
        this.beanFactory.refresh();
    }

    private void registerBeanPostProcessors(AutowireCapableBeanFactory beanFactory) {
        //这个是否需要向上多态？ 固定一个beanFactory类型的吗？
        //好像也没问题 因为就是这个beanFactory专门处理注解的.
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
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


}

package com.miniSpring.ioc;

import com.miniSpring.ioc.env.Environment;
import com.miniSpring.ioc.event.ApplicationEventPublisher;
import com.miniSpring.ioc.factory.BeanFactoryPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractApplicationContext implements ApplicationContext {

    private Environment environment;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
    private long startupDate;
    private final AtomicBoolean active = new AtomicBoolean();
    private final AtomicBoolean closed = new AtomicBoolean();
    private ApplicationEventPublisher applicationEventPublisher;

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * getBean()
     *
     * 两个方向，BeanFactory是实际上的作用，
     * ApplicationContext是组合beanFactory然后获取到组合的beanFactory，再去进行getBean()
     *
     * 所以追踪源码的时候要根据实际情况进行分析
     *
     *
     *
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        return getBeanFactory().getBean(beanName);
    }

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

    @Override
    public void refresh() {
        //BeanFactoryPostProcessor? 处理Bean以及对Bean的状态进行一些操作
        postProcessBeanFactory(getBeanFactory());
        //注册beanPostProcessor？
        registerBeanPostProcessors(getBeanFactory());
        //事件发布
        initApplicationEventPublisher();
        onRefresh();
        //事件监听者
        registerListeners();
        finishRefresh();
    }


    public abstract void registerListeners();
    public abstract void initApplicationEventPublisher();
    public abstract void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);
    public abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory);
    public abstract void onRefresh();
    public abstract void finishRefresh();

    @Override
    public String getApplicationName() {
        return null;
    }
    @Override
    public long getStartupDate() {
        return this.startupDate;
    }

    /**
     * 具体的实现交给specific class
     * @return
     */
    @Override
    public abstract ConfigurableListableBeanFactory getBeanFactory();

    /**
     * 先删后加
     * @param beanFactoryPostProcessor
     */
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
        beanFactoryPostProcessors.add(beanFactoryPostProcessor);
    }

    @Override
    public void close(){

    }
    @Override
    public boolean isActive(){
        return true;
    }





}

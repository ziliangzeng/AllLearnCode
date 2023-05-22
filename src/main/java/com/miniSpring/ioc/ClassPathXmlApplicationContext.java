package com.miniSpring.ioc;

import com.miniSpring.ioc.env.Environment;
import com.miniSpring.ioc.event.*;
import com.miniSpring.ioc.factory.BeanFactoryPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 这里就很像源代码的那样了，ClassPathXmlApplicationContext是BeanFactory
 * 但是实际作用的是组合进来的DefaultListableBeanFactory
 *
 *但是BeanFactoryPostProcessor是不在BeanFactory的功能之内的，
 * 因为他是在BeanFactory初始化之前就要执行的，所以这里就是在ApplicationContext中实现的
 *
 * 实现了什么抽象方法？
 * 他继承了AbstractApplicationContext
 * 1.首先进行了对ApplicationEventPublisher的初始化和注入，
 * 然后就是对其管理的ApplicationEvent和ApplicationListener也进行了管理
 * 2.对BeanPostProcessor进行管理
 * 3.对BeanFactoryPostProcessor进行管理
 *
 * 管理指对象注入（set init）
 *
 *
 * 并且还有一个很重要的组合（设计模式？），
 * 将DefaultListableBeanFactory给组合进来，由其实现BeanFactory的功能，
 * ApplicationContext更多的是扩展BeanFactory的能力（如事件发布）？
 *
 *
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {


    DefaultListableBeanFactory beanFactory;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    public ClassPathXmlApplicationContext(String fileName){
        this(fileName,true);
    }


    public ClassPathXmlApplicationContext(String fileName,boolean isRefresh){
        Resource resource = new ClassPathXmlResource(fileName);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if(isRefresh){
            try {
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        //TODO
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
         this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    public void onRefresh() {
       this.beanFactory.refresh();
    }

    @Override
    public void finishRefresh() {
        publishEvent(new ContextRefreshEvent("finishRefresh"));
    }

    /**
     * 具体的实现交给specific class
     *
     * @return
     */
    @Override
    public ConfigurableListableBeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {

    }

    @Override
    public Environment getEnvironment() {
        return null;
    }

    @Override
    public Boolean containsBean(String beanName) {
        return null;
    }

    @Override
    public void registerBean(String beanName, Object obj) {

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
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {

    }

    @Override
    public int getBeanPostProcessorCount() {
        return 0;
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
    public boolean containsBeanDefinition(String beanName) {
        return false;
    }

    @Override
    public int getBeanDefinitionCount() {
        return 0;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return new String[0];
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return new String[0];
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return null;
    }

    @Override
    public void registrySingleton(String beanName, Object singletonObject) {

    }

    @Override
    public Object getSingleton(String beanName) {
        return null;
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return false;
    }

    @Override
    public String[] getSingletonNames() {
        return new String[0];
    }

    @Override
    public String[] getActiveProfiles() {
        return new String[0];
    }

    @Override
    public String[] getDefaultProfiles() {
        return new String[0];
    }

    @Override
    public boolean acceptsProfiles(String... profiles) {
        return false;
    }

    @Override
    public boolean containsProperty(String key) {
        return false;
    }

    @Override
    public String getProperty(String key) {
        return null;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return null;
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return null;
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return null;
    }

    @Override
    public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
        return null;
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return null;
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return null;
    }

    @Override
    public String resolvePlaceholders(String text) {
        return null;
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }


    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor){
        this.beanFactoryPostProcessors.add(beanFactoryPostProcessor);
    }

}

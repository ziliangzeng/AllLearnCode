package com.miniSpring.mvc.web;

import com.miniSpring.ioc.*;
import com.miniSpring.ioc.env.Environment;
import com.miniSpring.ioc.event.*;
import com.miniSpring.ioc.factory.BeanFactoryPostProcessor;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 居然是直接继承了ClassPathXmlApplicationContext 实现了，是我狭隘了哈哈哈
 *
 *
 * 后面又改了。继承AbstractApplicationContext
 */
public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;
    private WebApplicationContext parentApplicationContext;

    DefaultListableBeanFactory beanFactory;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();


    public AnnotationConfigWebApplicationContext(String fileName) {
//        super(fileName,null);
        this(fileName, null);
    }

    /**
     * parents 是load IOC的bean
     * <p>
     * this 是load springMVC的bean?
     *
     * @param fileName
     * @param parentApplicationContext
     */
    public AnnotationConfigWebApplicationContext(String fileName, WebApplicationContext parentApplicationContext) {
//        super(fileName);
        this.parentApplicationContext = parentApplicationContext;
        this.servletContext = this.parentApplicationContext.getServletContext();

        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         *
         * TODO
         * 不对吧，这样子不是有问题？!  第一次的parents的时候他不是scan的逻辑的
         * 他是xml的形式的啊，这样子证明注入的呢？都是只能注入这个ControllerName
         *
         * ContextLoaderListener 里面就是new AnnotationConfigWebApplicationContext()的形式的？
         * 按照下面的改动形式的话，难道这个是通用的？
         * 感觉下面是引入@Service了？
         *
         */
        List<String> packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        List<String> controllerNames = scanPackages(packageNames);
        //TODO 但是为什么引用的这个的呢？后面要回头看一下ClassPathXmlApplicationContext
        /**
         * 上面已经修改了，不继承ClassPathXmlApplicationContext了
         * 而是选择继承AbstractApplicationContext，自己实现一套IOC？
         *
         * 这里还挺重要的呢
         * public Object getBean(String beanName) throws BeansException {
         *   Object result = super.getBean(beanName);
         *   if (result == null) {
         *   result = this.parentBeanFactory.getBean(beanName);
         *   }
         *   return result;
         * }
         *
         */
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        this.beanFactory = bf;
        this.beanFactory.setParent(this.parentApplicationContext.getBeanFactory());
        //往beanFactory中注册bean
        /**
         * 这里还是loadController的那一套逻辑
         * 但是IOC不是走的这一套逻辑吧
         * 走的XML逻辑
         * 所以这里需要改动？？
         *
         * 不是哦，你要回头看一下 <beans.xml/>
         * 里面也是 定义了一个<bean/>而已
         * 那么就是问题了，这里的逻辑是一样的
         * 也是注册到BeanDefinition里面去
         *
         * 但是构造器，属性这些怎么注入的，需要回头看一下ClassPathXmlApplicationContext！
         * 现在就看
         *
         * 看了一下确实是缺少了property的定义属性的，这个应该是在下面进行迭代的吧
         *
         * 好吧，最后是通过new XmlWebApplicationContext() 来解决的.
         *
         */
        loadBeanDefinitions(controllerNames);

        if (true) {
            try {
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void loadBeanDefinitions(List<String> controllerNames) {
        for (String controllerName : controllerNames) {
            String beanId = controllerName;
            String beanClassName = controllerName;
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
            this.beanFactory.registerBeanDefinition(beanId, beanDefinition);
        }
    }




    public void setParent(WebApplicationContext parentApplicationContext) {
        this.parentApplicationContext = parentApplicationContext;
        this.beanFactory.setParent(this.parentApplicationContext.getBeanFactory());
    }


    private List<String> scanPackages(List<String> packages) {

        List<String> tempControllerNames = new ArrayList<>();

        for (String aPackage : packages) {

            tempControllerNames.addAll(scanPackage(aPackage));
        }

        return tempControllerNames;
    }

    /**
     * 理解
     * <p>
     * 为什么要进行这些格式的切换,因为在minisMVC-servlet.xml 定义的是扫描的目录路径,
     * 他的格式是//// 的
     * 而我们的类文件是 com.miniSpring.mvc.web.DispatcherServlet .....的
     * 所以要进行切换
     * 但是很明显,这里还没有区分哪个 Interface,emum @interface这些吧
     * 统一Class了
     * <p>
     * TODO 这里很值得学,目录扫描,进而拿到class 里面的方法,都是一环接一环的
     *
     * @param aPackage
     * @return
     */
    private List<String> scanPackage(String aPackage) {
        ArrayList<String> tempControllerNames = new ArrayList<>();
        URI uri = null;
        try {
            uri = this.getClass().getResource("/" + aPackage.replaceAll("\\.", "/")).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File dir = new File(uri);
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanPackage(aPackage + "." + file.getName());
            } else {
                String controllerName = aPackage + "." + file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }


    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
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

    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {

    }
}

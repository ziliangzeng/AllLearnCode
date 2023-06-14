package com.miniSpring.ioc;

/**
 * BeanDefinition
 * 用来描述bean的信息
 *
 * TODO interface 是一个BeanDefinition吗?
 * 那么扫描的时候 interface是定义成为什么的呢?
 * 例如OpenFeign中的个钟@FeignClient 的接口是如何注入的
 * 是不是说其实注入的还是他得到实现类的呢？
 *
 *
 */
public class BeanDefinition {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    private boolean lazyInit = false;
    private String[] dependsOn;
    private ArgumentValues constructorArgumentValues;
    private PropertyValues propertyValues;
    private String initMethodName;
    private volatile Object beanClass;

    private String id;
    private String className;


    private String scope = SCOPE_SINGLETON;


    /**
     * 暂时没有的方法,看后面的篇章
     * @return
     */
    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(this.scope);
    }
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(this.scope);
    }


    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }




    public String getId() {
        return id;
    }
    public String getClassName() {
        return className;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    public String getSCOPE_SINGLETON() {
        return SCOPE_SINGLETON;
    }

    public void setSCOPE_SINGLETON(String SCOPE_SINGLETON) {
        this.SCOPE_SINGLETON = SCOPE_SINGLETON;
    }

    public String getSCOPE_PROTOTYPE() {
        return SCOPE_PROTOTYPE;
    }

    public void setSCOPE_PROTOTYPE(String SCOPE_PROTOTYPE) {
        this.SCOPE_PROTOTYPE = SCOPE_PROTOTYPE;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String[] getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(String[] dependsOn) {
        this.dependsOn = dependsOn;
    }

    public ArgumentValues getConstructorArgumentValues() {
        return constructorArgumentValues;
    }

    public void setConstructorArgumentValues(ArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public Object getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Object beanClass) {
        this.beanClass = beanClass;
    }
}

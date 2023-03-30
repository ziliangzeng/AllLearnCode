package com.learn.code.minioSpring.com.minis;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: qsx00
 * @Date: 2023/3/27 22:24
 * @Description:
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry
        implements BeanFactory, BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private List<String> beanDefinitionNames = new ArrayList<>();
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    public void AbstractBeanFactory() {
    }
    
    public void refresh(){
        for (String beanDefinitionName : beanDefinitionNames) {
            getBean(beanDefinitionName);
        }
    }


    //前后置处理器
    abstract Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName);

    abstract Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName);


    /**
     * 生命周期:
     *
     * 1. 实例化 --> constructor()构造器方法
     * 2. 属性注入 --> 反射调用set Param方法 popularBean
     * 2.0.0 实现了BeanNameAware，BeanFactoryAware接口的setBeanName，setBeanFactory方法(暂无)
     * 3.0.0 postProcessBeforeInitialization bean前置处理器
     * 3.0.1 实现了InitializingBean接口的afterPropertiesSet方法（暂无）
     * 3. 初始化  --> init-method
     * 3.1.0 postProcessAfterInitialization bean后置处理器
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) {
        //先尝试从容器中拿到bean
        Object singleton = this.getSingleton(beanName);

        if (singleton == null) {
            //尝试从已完成了实例化的bean Map中拿到对应的bean
            singleton = earlySingletonObjects.get(beanName);
            if (singleton == null) {
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                if (beanDefinition == null) {
                    throw new RuntimeException("beanDefinition is null");
                }
                singleton = createBean(beanDefinition);
                registerBean(beanName, singleton);

                //step1: postProcessBeforeInitialization
                applyBeanPostProcessorBeforeInitialization(singleton, beanName);
                //step2: init-method 初始化方法吧
                if(beanDefinition.getInitMethodName() != null && !beanDefinition.equals("")){
                    invokeInitMethod(beanDefinition, singleton);
                }
                //step3: postProcessAfterInitialization
                applyBeanPostProcessorAfterInitialization(singleton, beanName);
            }
        }

        return singleton;
    }

    /**
     * 这里有点奇怪
     * 为什么是调用BeanDefinition的Class来获取method的？不应该所以singleton的class吗？
     * @param beanDefinition
     * @param singleton
     */
    private void invokeInitMethod(BeanDefinition beanDefinition, Object singleton) {
        Class<?> clz = beanDefinition.getClass();
        String initMethodName = beanDefinition.getInitMethodName();
        Method method = null;
        try {
            method = clz.getMethod(initMethodName);
            method.invoke(singleton);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean containsBean(String beanName){
        return beanDefinitionMap.containsKey(beanName);
    }

    public void registerBean(String beanName, Object obj) {
        registrySingleton(beanName, obj);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
        beanDefinitionNames.add(name);
        //TODO 有问题
        if(beanDefinition.isLazyInit()){
            getBean(name);
        }
    }

    @Override
    public void removeBeanDefinition(String beanName){
        beanDefinitionMap.remove(beanName);
        beanDefinitionNames.remove(beanName);
        removeSingleton(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName){
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) {
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



    private Object createBean(BeanDefinition beanDefinition) {

        //创建bean
        //实例化,
        // 实例化的object 放入earlySingletonObjects里面
        // 然后就是属性注入

        Object o = doCreateBean(beanDefinition);

        earlySingletonObjects.put(beanDefinition.getId(), o);

        String className = beanDefinition.getClassName();
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {

            throw new RuntimeException(e);
        }
        //属性注入
        populateBean(beanDefinition, clazz, o);

        return o;
    }



    private void populateBean(BeanDefinition beanDefinition, Class<?> clazz, Object o) {

        handleProperties(beanDefinition, clazz, o);

    }

    private void handleProperties(BeanDefinition beanDefinition, Class<?> clazz, Object o) {
        //属性注入,

        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            List<PropertyValue> propertyValueList = propertyValues.getPropertyValueList();
            for (PropertyValue propertyValue : propertyValueList) {

                //还有注解的注入呢
                /**
                 * 后置处理器需要拿到 beanName和objectName才能进行注入
                 * 问1.这里我是有一个疑惑的,就是earlySingletonObjects里面的bean,也已经是有这个Field[]的内容了的吗
                 * 答1.你这个说法有问题吧,实例化是对每一个属性赋初始值 基本类型为0,引用类型为null!! 串联起来了 哈哈哈哈
                 * 并且拿到Field[] 通过Class.getField()方法的,只不过在赋予给具体的值时候才会需要Obj.
                 * 问2.假如xml和Autowired同时注入了,这里需要处理哪个起作用的问题.
                 *
                 * TODO
                 */
                String name = propertyValue.getName();
                String type = propertyValue.getType();
                Object value = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();

                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];

                if (!isRef) {
                    if ("String".equals(type) || "java.lang.String".equals(type)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(type)) {
                        paramTypes[0] = int.class;
                    } else {
                        paramTypes[0] = String.class;
                    }
                    paramValues[0] = value;
                } else {
                    try {
                        paramTypes[0] = Class.forName((String) type);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    paramValues[0] = getBean((String) value);
                    //is ref,create the dependency bean
                }

                String methodName = "set" + name.substring(0,1).toUpperCase() + name.substring(1);

                try {
                    Method method = clazz.getMethod(methodName, paramTypes);
                    method.invoke(o, paramValues);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

//                try {
//                    Field field = clazz.getField(name);
//                } catch (NoSuchFieldException e) {
//                    throw new RuntimeException(e);
//                }
            }
        }
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Object o = null;
        ArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
        String className = beanDefinition.getClassName();
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //实例化
        if (!constructorArgumentValues.isEmpty()) {
            Class<?>[] paramTypes = new Class[constructorArgumentValues.getArgumentCount()];
            Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];

            for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                ArgumentValue indexedArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                String type = indexedArgumentValue.getType();
                Object value = indexedArgumentValue.getValue();
                if ("String".equals(type) || "java.lang.String".equals(type)) {
                    paramTypes[i] = String.class;
                    paramValues[i] = value;
                } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                    paramTypes[i] = Integer.class;
                    paramValues[i] = Integer.valueOf((String) value);
                } else if ("int".equals(type)) {
                    paramTypes[i] = int.class;
                    paramValues[i] = Integer.valueOf((String) value);
                } else {
                    paramTypes[i] = String.class;
                    paramValues[i] = value;
                }
            }
            try {
                Constructor constructor = clazz.getConstructor(paramTypes);
                o = constructor.newInstance(paramValues);
            } catch (NoSuchMethodException e) {

                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        } else {
            //调用无参构造方法
            try {
                o = clazz.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return o;
    }


}

package com.miniSpring.ioc;

import java.lang.reflect.Constructor;
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
        implements BeanFactory, BeanDefinitionRegistry, AutowiredCapableBeanFactory {

    public Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    public List<String> beanDefinitionNames = new ArrayList<>();
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    public void AbstractBeanFactory() {
    }

    public void refresh() {
        for (String beanDefinitionName : beanDefinitionNames) {
            getBean(beanDefinitionName);
        }
    }


    //TODO 注释这里的话，下面的getBean() 方法有问题,是不是需要实现AutowiredCapableBeanFactory接口?
    //TODO 我认为是这种写法的，当然可能有问题
    //前后置处理器
//    abstract Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName);
//
//    abstract Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName);


    /**
     * 生命周期:
     * <p>
     * 1. 实例化 --> constructor()构造器方法
     * 2. 属性注入 --> 反射调用set Param方法 popularBean
     * 2.0.0 实现了BeanNameAware，BeanFactoryAware接口的setBeanName，setBeanFactory方法(暂无)
     * 3.0.0 postProcessBeforeInitialization bean前置处理器
     * 3.0.1 实现了InitializingBean接口的afterPropertiesSet方法（暂无）
     * 3. 初始化  --> init-method
     * 3.1.0 postProcessAfterInitialization bean后置处理器
     *
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

                //这里自旋等待?
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

                if (beanDefinition == null) {
                    throw new RuntimeException("beanDefinition is null");
                }
                singleton = createBean(beanDefinition);
                registerBean(beanName, singleton);

                //step1: postProcessBeforeInitialization
                applyBeanPostProcessorsBeforeInitialization(singleton, beanName);
                //step2: init-method 初始化方法吧
                if (beanDefinition.getInitMethodName() != null && !beanDefinition.equals("")) {
                    invokeInitMethod(beanDefinition, singleton);
                }
                //step3: postProcessAfterInitialization
                applyBeanPostProcessorsAfterInitialization(singleton, beanName);
            }
        }

        return singleton;
    }

    /**
     * 这里有点奇怪
     * 为什么是调用BeanDefinition的Class来获取method的？不应该所以singleton的class吗？
     *
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
    public Boolean containsBean(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    public void registerBean(String beanName, Object obj) {
        registrySingleton(beanName, obj);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
        beanDefinitionNames.add(name);
        /**
         * TODO 有问题
         * 再细看,好像又没有问题哦,只是初始化当前的bean
         * 有问题的,因为{@link com/learn/code/minioSpring/com/minis/AbstractBeanFactory.java:239 }
         * 以来第三方bean,属性注入的时候,二次去获取bean,这时候有可能这时候该bean的 BeanDefinition 还没有生成呢?!所以是有问题的
         * 那么怎么解决?
         * 如何处理这种顺序问题的? 自旋等待?
         * 或者主动去遍历资源吗?
         * TODO 待解答
         *
         */
        if (beanDefinition.isLazyInit()) {
            getBean(name);
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
                        /**
                         * 调用forName("X")会导致名为X的类被初始化？
                         * 会导致类加载，类加载就会执行静态代码块。
                         * 如果你只想让某个类，只执行静态代码块，可以使用此方法。
                         */
                        paramTypes[0] = Class.forName(type);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    paramValues[0] = getBean((String) value);
                    //is ref,create the dependency bean
                }

                String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);

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
                boolean isRef = indexedArgumentValue.getIsRef();
                /**
                 * TODO 这里我有个问题,就是构造器注入是否可以注入其他bean的呢?
                 * 下面的逻辑都没有getbean的操作的,这里是不是有问题的呢?
                 *
                 * TODO 这里需要增加逻辑,对XML的第三方bean 的注入处理
                 * 然后就是使用@Autowired的形式对构造器bean注入的处理
                 * 这个打算作为自己来实现吧!
                 */
                if (!isRef) {
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

                } else {
                    //引用
                    try {
                        /*
                         * TODO 这里无法处理这个循环依赖问题
                         *  使用@Lazy解决循环依赖问题？
                         *
                         * */
                        paramTypes[i] = Class.forName(type);
                        paramValues[i] = getBean((String) value);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
            try {
                /**
                 * 如果是使用的@Service的形式的，没有使用xml，如何进行构造器注入的。
                 * 首先是@Autowired？ 然后就是拿到构造器的参数
                 * 参数获取到了之后进行对象的getBean？
                 * 最后调用构造器方法？
                 */
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

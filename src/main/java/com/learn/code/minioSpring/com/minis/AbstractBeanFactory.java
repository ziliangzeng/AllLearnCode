package com.learn.code.minioSpring.com.minis;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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


    @Override
    public Object getBean(String beanName) {
        //先尝试从容器中拿到bean
        Object singleton = this.getSingleton(beanName);
        if (singleton == null) {
            //尝试从毛坯房中拿到bean
            Object o = earlySingletonObjects.get(beanName);
            if (o == null) {
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                if (beanDefinition == null) {
                    throw new RuntimeException("beanDefinition is null");
                }
                o = createBean(beanDefinition);

            }
        }


        return null;
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

        //属性注入,

        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if(!propertyValues.isEmpty()){
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

                try {
                    Field field = clazz.getField(name);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }


            }


        }

    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Object o = null;
        ArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
        String className1 = beanDefinition.getClassName();
        Class clazz = null;
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
            String className = beanDefinition.getClassName();
            try {
                clazz = Class.forName(className);
                o = clazz.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return o;
    }


}

package com.miniSpring.ioc;

import com.miniSpring.ioc.test.AServiceImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 根据继承的DefaultSingletonBeanRegistry,可以看出这个类是一个单例注册表,用于存储bean
 * beanFactory,同时也是一个创建bean的工厂
 * BeanDefinitionRegistry,那么也是存储bean定义的地方罗
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private List<String> beanDefinitionNames = new ArrayList<>();

    /**
     * TODO 应该是待删除,跟上面的重复了
     */
//    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();
    //为什么用List？ 为什么不用set？
//   private List<String> beanNames = new ArrayList<String>();
//
//   private Map<String,Object> singletons = new HashMap<>();

    //3级缓存
    private Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(256);


    public SimpleBeanFactory() {
    }


    /**
     * 首先判断是否存在这个beanId？ 存在然后找bean定义，之后创建，存储进map 后面直接拿？
     * 这是懒加载？
     *
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        //尝试获取
        Object singleton = this.getSingleton(beanName);
        //获取不到bean，进行尝试获取bean定义，然后创建bean
        if (singleton == null) {

            if (!beanDefinitionMap.containsKey(beanName)) {
                throw new BeansException("No bean named " + beanName + " is defined");
            }
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
//                Class<?> aClass = Class.forName(beanDefinition.getClassName());
//                singleton = aClass.newInstance();

            singleton = createBean(beanDefinition);

            //注册bean
            registerBean(beanName, singleton);
        }
        return singleton;
    }

    /**
     * 懒加载？
     * 也不算懒加载吧，只是在这里进行了注册，之后直接拿
     * 调用这个refresh() 可以对当前所有的资源进行注册
     */
//    @Override
//    public void refresh(){
//        for (String beanName : beanDefinitionNames) {
//            try {
//                getBean(beanName);
//            } catch (BeansException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }





    /**
     * step1.获取bean定义
     * step2.通过class属性反射到Class类 ->其实在这里是不是就可以通过newInstance()拿到对象的?
     * 是可以,这样子就是无参构造器,那么就会出现xml里面的<constructor-arg>标签不起作用了
     * 这样子,可以继续通过方法反射,把属性值都给设置进去.
     * step3.通过构造器类设置构造器,实例化和初始化对象.
     * step4.通过反射属性的set方法,设置属性值
     *
     * @param beanDefinition
     * @return
     */
    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        Object obj = null;

        obj = doCreateBean(beanDefinition);
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        handleProperties(beanDefinition, clz, obj);

        return obj;
    }

    private void handleProperties(BeanDefinition beanDefinition, Class<?> clz, Object obj) {

        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        /**
         * 上面通过构造器进行实例化了，下一步所以要进行属性注入，这里是通过反射来进行属性注入
         * 通过Class获取属性，然后通过属性的set方法进行注入
         * 上一步Class，Object都拿到了。所以这一步只需要拿到反射方法的名称，参数类型，参数值进行方法反射调用即可
         * Method method = cla.getMethod("set" + name, paramTypes)
         * 验证，把他给注释掉，会发生什么呢 {@link AServiceImpl#setName(String)}
         * method.invoke(obj, paramValues)
         */
        if (!propertyValues.isEmpty()) {
            //TODO
            //还是不明白为什么属性要设置为final?
            //因为final的属性只能在构造器中初始化,不能在其他地方初始化,所以这里只能用反射来设置属性.
            //我不认同,因为反射是反射另外的bean的属性,而不是反射自己的属性.
            //
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                String type = propertyValue.getType();
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
                    //is ref,create the dependent beans
                    try {
                        paramTypes[0] = Class.forName(type);
                        //TODO getBean 和 createBean 还没有关联起来
                        /**
                         * 这里的资源读取器自动把ref的value值赋值为引用对象ref了
                         * {@link com/learn/code/minioSpring/com/minis/XmlBeanDefinitionReader.java:54}
                         */
                        paramValues[0] = getBean((String) value);

                        //反射
                        String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);

                        Method method = clz.getMethod(methodName, paramTypes);


                        method.invoke(obj, paramValues);

                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (BeansException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {

        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;

        try {
            clz = Class.forName(beanDefinition.getClassName());
            //构造器拿到生命周期第一步实例化的对象
            ArgumentValues argumentValues = beanDefinition.getConstructorArgumentValues();

            /**
             * 根据resource资源，顺序存储进两个数组里面 Class<?>[] Object<?>[]
             * con = Class.getConstructor(Class<?>... parameterTypes)
             * obj = con.newInstance(Object... initargs)
             *
             * 通过Class获取构造器，然后通过构造器实例化对象
             */
            if (!argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];

                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ArgumentValue indexedArgumentValue = argumentValues.getIndexedArgumentValue(i);
                    String type = indexedArgumentValue.getType();
                    if ("String".equals(type) || "java.lang.String".equals(type)) {
                        paramTypes[i] = String.class;
                        paramValues[i] = indexedArgumentValue.getValue();
                    } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) indexedArgumentValue.getValue());
                    } else if ("int".equals(type)) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) indexedArgumentValue.getValue());
                    } else {
                        paramTypes[i] = String.class;
                        paramValues[i] = indexedArgumentValue.getValue();
                    }
                }

                //clz.getConstructor(paramTypes)是获取指定参数类型的构造器,他需要提前在实体类中创建一个构造器吗？
                //不需要,因为这里是通过反射来获取的,所以不需要在实体类中创建一个构造器.
                //好像我查的源码告诉我，好像是需要在实体类中定义个构造器的.，否则是无法反射调用获取到这个构造器的吧？
                /**
                 *  TODO 很简单的验证，后面把AServiceImpl的构造器注释掉，因该是会报错的！？
                 * {@link com.miniSpring.ioc.test.AServiceImpl#AServiceImpl(java.lang.String, int)}
                 */
                con = clz.getConstructor(paramTypes);
                //TODO
                //构造器的newInstance(...) 和 Class的newInstance()有什么区别？
                //应该不需要在实体类创建一个按照顺序来的构造器吧？
                //Constructor 就是新创建一个构造器,构造函数.所以argumentValues.name不重要(不需要),因为对于用户来讲是透明的.

                //翻看Class#newInstance()的源码,发现是调用了Constructor#newInstance()的方法.
                //所以本质上是一个东西，只不过Class#newInstance()是默认调用无参构造器的.
                obj = con.newInstance(paramValues);
            } else {
                obj = clz.newInstance();
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }


    /**
     * 这里需要外部的资源reader调用，将bean定义注册进来
     *
     * @param beanName
     */
//    @Override
//    public void registerBeanDefinition(BeanDefinition beanDefinition) {
//        this.beanDefinitionMap.put(beanDefinition.getId(), beanDefinition);
//    }

    @Override
    public Boolean containsBean(String beanName) {
        return this.containsSingleton(beanName);
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        this.registrySingleton(beanName, obj);
    }

    @Override
    public boolean isSingleton(String beanName) {
        //TODO 这里有问题,暂时根据意思创建好方法,看后面的发展
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

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
        beanDefinitionNames.add(beanName);
        if (!beanDefinition.isLazyInit()) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
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



}

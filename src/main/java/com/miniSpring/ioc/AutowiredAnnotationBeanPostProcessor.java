package com.miniSpring.ioc;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    private AbstractAutowireCapableBeanFactory beanFactory;


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Object result = bean;
        Class<?> clazz = bean.getClass();

        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                boolean annotationPresent = field.isAnnotationPresent(Autowired.class);
                if (annotationPresent) {
                    //如果存在,那么就从beanFactory中拿到对应的bean进行注入
                    String fieldName = field.getName();
                    Object bean1 = beanFactory.getBean(fieldName);
                    //然后进行注入
                    try {
                        //TODO 反射需要总结出一个xmind出来,包括属性,方法 等, 目前已经遇到这两个
                        /**
                         * 1.属性
                         * Field field = obj.getClass().getDeclaredField(name); //获取属性
                         * filed.setAccessible(true); //设置访问权限
                         * file.set(obj, value); //设置属性值
                         *
                         * 2.方法
                         * Method method = obj.getClass().getMethod(methodName,paramType); // paramType是参数类型,这个方法的参数类型数组,应该是要顺序的?
                         * method.invoke(obj,paramValue); // 执行
                         *
                         * 为什么方法不需要设置访问权限呢?
                         * 方法的访问权限是public? 如果我设置为private呢?
                         *
                         * TODO 如果属性设置为public呢? 这些都需要去验证一下
                         * 我个人认为设置上述的访问权限之后,就不需要进行setAccessible(true)了.
                         *
                         * 使用fiele反射的形式进行注入,是因为,不一定有setParam()的方法?
                         * @Autowired 注解的属性,有一些是没有set方法的
                         *
                         */
                        field.setAccessible(true);
                        field.set(result, bean1);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }


    public AbstractAutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * TODO 这里应该是AbstractBeanFactory
     * 并且我记得在自定义后置处理器的时候，好像是不需要设置beanFactory的？
     *
     * @param beanFactory
     */
    @Override
    public void setBeanFactory(AbstractAutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}

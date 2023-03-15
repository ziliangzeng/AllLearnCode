package com.learn.code.minioSpring.com.minis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleBeanFactory implements BeanFactory{

    //为什么用List？ 为什么不用set？
   private List<BeanDefinition> beanDefinitions = new ArrayList<BeanDefinition>();
   private List<String> beanNames = new ArrayList<String>();

   private Map<String,Object> singletons = new HashMap<>();

    /**
     *  首先判断是否存在这个beanId？ 存在然后找bean定义，之后创建，存储进map 后面直接拿？
     *  这是懒加载？
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        //尝试获取
        Object o = singletons.get(beanName);
        if(o == null){
            int i = beanNames.indexOf(beanName);
            if(i == -1){
                throw new BeansException("No bean named " + beanName + " is defined");
            }

            BeanDefinition beanDefinition = beanDefinitions.get(i);
            try {
                Class<?> aClass = Class.forName(beanDefinition.getClassName());
                o = aClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            singletons.put(beanName,o);

        }
        return o;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitions.add(beanDefinition);
        this.beanNames.add(beanDefinition.getId());
    }
}

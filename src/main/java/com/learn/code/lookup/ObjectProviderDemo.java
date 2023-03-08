package com.learn.code.lookup;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 依赖查找 spring5.3 {@link ObjectProvider}
 * 继承于ObjectFactory bean托管给beanFactory,然后进行获取getObject()
 * @Auther: qsx00
 * @Date: 2023/3/7 22:09
 * @Description:
 */
public class ObjectProviderDemo {


    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ObjectProviderDemo.class);
        applicationContext.refresh();
        lookupByObjectProvider(applicationContext);
        applicationContext.close();
    }


    @Bean
    public String helloworld() {
        return "hello,world";
    }


    private static void lookupByObjectProvider(AnnotationConfigApplicationContext applicationContext) {
        ObjectProvider<String> beanProvider = applicationContext.getBeanProvider(String.class);
        String object = beanProvider.getObject();
        System.out.println(object);
    }
}

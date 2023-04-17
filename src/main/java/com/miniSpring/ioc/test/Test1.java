package com.miniSpring.ioc.test;

import com.miniSpring.ioc.BeansException;
import com.miniSpring.ioc.ClassPathXmlApplicationContext;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //为什么会调用到实现接口的类方法
        BaseService baseService = (BaseService) context.getBean("baseservice");
        baseService.sayHello();
    }
}

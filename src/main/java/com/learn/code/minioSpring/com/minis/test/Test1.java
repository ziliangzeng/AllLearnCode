package com.learn.code.minioSpring.com.minis.test;

import com.learn.code.minioSpring.com.minis.ClassPathXmlApplicationContext;

public class Test1 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //为什么会调用到实现接口的类方法
        BaseService baseService = (BaseService) context.getBean("baseservice");
        baseService.sayHello();
    }
}

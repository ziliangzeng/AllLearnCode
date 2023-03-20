package com.learn.code.minioSpring.com.minis.test;


public class AServiceImpl implements AService{

    /**
     * 注入操作的本质，就是给Bean的各个属性进行赋值
     */
    private String property1;
    private String property2;

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }


    private String name;
    private int level;

    public AServiceImpl(String name,int level){
        this.name = name;
        this.level = level;
    }


    @Override
    public void sayHello() {
        System.out.println("a service 1 say hello");
    }
}

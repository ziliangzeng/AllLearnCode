package com.learn.code.minioSpring.com.minis.test;


public class AServiceImpl implements AService{

    /**
     * 注入操作的本质，就是给Bean的各个属性进行赋值
     */
    private String property1;
    private String property2;

    private String name;
    private int level;

    public AServiceImpl(String name,int level){
        this.name = name;
        this.level = level;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public String getProperty1() {
        return property1;
    }

    public String getProperty2() {
        return property2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void sayHello() {
        System.out.println("a service 1 say hello");
    }
}

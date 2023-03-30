package com.learn.code.minioSpring.com.minis.test;

import com.learn.code.minioSpring.com.minis.Autowired;

public class BaseService {

    @Autowired
    private BaseBaseService basebaseservice;


    public BaseService(){}

    public void sayHello(){
        System.out.println("Base Service says hello!!");
    }

    public BaseBaseService getBasebaseservice() {
        return basebaseservice;
    }

    public void setBasebaseservice(BaseBaseService basebaseservice) {
        this.basebaseservice = basebaseservice;
    }
}

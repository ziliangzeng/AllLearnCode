package com.miniSpring.ioc.test;

import com.miniSpring.ioc.Autowired;

public class BaseService {

    /**
     * 这里使用名称跟类型基本一致的原因,是因为目前只实现了byName的形式来进行注解注入
     */
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

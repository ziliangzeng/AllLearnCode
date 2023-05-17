package com.miniSpring.ioc.test;

import com.miniSpring.ioc.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    /**
     * 这里使用名称跟类型基本一致的原因,是因为目前只实现了byName的形式来进行注解注入
     */
    @Autowired
    private BaseBaseService basebaseservice;

    private AService aService;


//    public BaseService(BaseBaseService basebaseservice){
//        this.basebaseservice = basebaseservice;
//    }
//
//    //多个构造器，会选择哪一个进行实例化呢？
//    //这里会选择参数最多的构造器进行实例化
//    public BaseService(BaseBaseService basebaseservice,AService aService){
//        this.aService = aService;
//        this.basebaseservice = basebaseservice;
//    }




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
